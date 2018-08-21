package com.atmaram.jp.cli;

import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.CommandConfigurationException;
import com.atmaram.jp.model.*;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.json.JSONTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException, ParseException, CommandConfigurationException, TemplateParseException {
        JSONObject jsonObject = new JSONObject();

            List<String> lEnv = new ArrayList<>();
            List<String> lFilter = new ArrayList<>();
            for (String arg :
                    args) {
                if (isEnv(arg)) {
                    lEnv.add(arg);
                } else {
                    lFilter.add(arg);
                }
            }

            Scanner input = new Scanner(System.in);

            List<String> commands = getCommands(Paths.get("config/commands"));
            List<String> filtered_commands;
            if (args.length == 0) {
                filtered_commands = commands;
            } else {
                List<Integer> ranks=commands.stream().map((command)->{
                    int rank=0;
                    for (int i = 0; i < lFilter.size(); i++) {
                        if (command.toLowerCase().contains(lFilter.get(i).toLowerCase()))
                            rank++;
                    }
                    return rank;
                }).collect(Collectors.toList());
                int topRank=ranks.stream().reduce(0,(one,two)->one>two?one:two);
                filtered_commands = new ArrayList<>();
                for(int i=0;i<commands.size();i++){
                    if(ranks.get(i)==topRank){
                        filtered_commands.add(commands.get(i));
                    }
                }
            }
            int iCommand = 0;
            if (filtered_commands.size() > 1) {
                System.out.println("Select command:");
                for (int i = 0; i < filtered_commands.size(); i++) {
                    System.out.println(i + ") " + filtered_commands.get(i));
                }
                iCommand = Integer.parseInt(input.nextLine());
            }
            if (filtered_commands.size() == 0) {
                System.out.println("No Matching command");
                return;
            }
            System.out.println("Running Command: " + filtered_commands.get(iCommand));
            System.out.println("On Environments " + lEnv.stream().reduce((s, out) -> s + " " + out).get());

            List<Environment> environments = new ArrayList<>();
            for (int i = 0; i < lEnv.size(); i++) {
                Path baseEnvFile = Paths.get("config/env/" + lEnv.get(i));
                Environment environment = readEnv(baseEnvFile);
                environments.add(environment);
            }

            Path baseCommandDir = Paths.get("config/commands/" + filtered_commands.get(iCommand));

            Command command = readCommand(baseCommandDir);
            ValueStore valueStore = new ValueStore();
            VariableStore variableStore = new VariableStore();
            readDats(baseCommandDir, valueStore, variableStore, lEnv);
            command.eval(variableStore, environments);
            List<Variable> variables = variableStore.getVariables();


            for (Variable variable :
                    variables) {
                valueStore.add(variable.getName(), readVariable(variable));

            }
        try
        {
                command.execute(environments, valueStore);
                List<String> opVars = readOPVariables(baseCommandDir);
                for (String var :
                    opVars) {

                    jsonObject.put(var, valueStore.getValues().get(var));
                }
        } catch (Exception ex){
            List<String> opVars = readOPVariables(baseCommandDir);
            for (String var :
                    opVars) {

                jsonObject.put(var, valueStore.getValues().get(var));
            }
            System.out.println(jsonObject);
            throw ex;
        }

        System.out.println(jsonObject);

    }
    public static boolean isEnv(String name){
        Path baseEnvFile = Paths.get("config/env/" + name);
        File file=baseEnvFile.toFile();
        if (file.exists()){
            return true;
        }
        return false;
    }
    public static void transformJSONToVariablesAndValues(String name,JSONAware data,ValueStore valueStore,VariableStore variableStore){
        Variable variable=transformObjectToVariable(name,data);
        variableStore.resolve(Arrays.asList(variable));
        valueStore.add(name,data);
    }
    public static Variable transformObjectToVariable(String name,Object data){
        Variable variable=new Variable();
        variable.setName(name);
        if(data instanceof JSONArray || data instanceof List){
            variable.setType("List");
            JSONObject obj=(JSONObject)((JSONArray)data).get(0);
            List<Variable> inner=new ArrayList<>();
            for (Object key:
                    obj.keySet()) {
                Variable variable1=transformObjectToVariable((String)key,obj.get(key));
                inner.add(variable1);
            }
            variable.setInner_variables(inner);
        } else {
            variable.setType("String");
        }
        return variable;
    }
    public static void readDat(File file,ValueStore valueStore,VariableStore variableStore) throws IOException,TemplateParseException {
        String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
        JSONTemplate template=JSONTemplate.parse(text);
        JSONTemplate filledTemplate=template.fill(valueStore.getValues());

        JSONAware data= (JSONAware) filledTemplate.toJSONCompatibleObject();
        String name=file.getName().substring(0,file.getName().length()-5);
        transformJSONToVariablesAndValues(name,data,valueStore,variableStore);
    }
    public static void readDats(Path baseCommandDir,ValueStore valueStore,VariableStore variableStore,List<String> envs) throws IOException, ParseException,TemplateParseException {
        File dir=baseCommandDir.toFile();
        File[] datFolders=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return envs.contains(name);
            }
        });
        for (File datFolder:
             datFolders) {
            readDats(datFolder.toPath(),valueStore,variableStore,envs);
        }
        File[] datFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".json");
            }
        });
        Arrays.sort(datFiles);
        for (File datFile:
                datFiles) {
            readDat(datFile,valueStore,variableStore);
        }
    }
    public static List<String> readOPVariables(Path baseCommandDir) throws FileNotFoundException {
        File dir=baseCommandDir.toFile();
        List<String> variables=new ArrayList<>();
        File[] varFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".list");
            }
        });
        Arrays.sort(varFiles);
        for (File file:
             varFiles) {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNextLine()){
                String line=scanner.nextLine().trim();
                if(!variables.contains(line))
                    variables.add(line);
            }
        }
        return variables;
    }

    public static List<String> getCommands(Path baseCommandsDir){
        List<String> commands=new ArrayList<>();
        File commandsDir=baseCommandsDir.toFile();
        for (File file:
             commandsDir.listFiles()) {
            if(file.isDirectory()){
                commands.add(file.getName());
            }
        }
        return commands;
    }
    public static Object readVariable(Variable variable){
        if(variable.getType().equals("String"))
        {
            Scanner input=new Scanner(System.in);
            System.out.println("Enter "+variable.getName()+":");
            return input.nextLine();
        } else {
            return "";
        }
    }
    public static Environment readEnv(Path baseEnvFile) throws FileNotFoundException {
        File file=baseEnvFile.toFile();
        Scanner fileScanner=new Scanner(file);
        Environment environment=new Environment();
        environment.setName(file.getName());
        environment.setVariables(new ArrayList<>());
        while (fileScanner.hasNextLine()){
            String line=fileScanner.nextLine();
            String[] split=line.split("=");
            environment.getVariables().add(new EnvironmentVariable(split[0],split[1]));
        }
        return environment;
    }
    public static Command readCommand(Path baseCommandDir) throws FileNotFoundException, ParseException {
        File dir=baseCommandDir.toFile();
        List<Unit> units=new ArrayList<>();
        File[] varFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".var");
            }
        });
        Arrays.sort(varFiles);
        List<String> allowedVerbs=Arrays.asList(".get",".post",".delete",".put",".patch",".block",".poll");
        File[] files=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                for(int i=0;i<allowedVerbs.size();i++){
                    if(name.endsWith(allowedVerbs.get(i)))
                        return true;
                }
                return false;
            }
        });
        Arrays.sort(files,new Comparator()
        {
            @Override
            public int compare(Object f1, Object f2) {
            return ((File) f1).getName().compareTo(((File) f2).getName());
        }
        });
        for(File file:files){
            units.add(UnitBuilder.buildFromFile(file));
        }
        Command command=new Command();
        command.setVariables(UnitBuilder.getCommandVariables(varFiles));
        command.setName(dir.getName());
        command.setUnits(units);
        return command;
    }
}
