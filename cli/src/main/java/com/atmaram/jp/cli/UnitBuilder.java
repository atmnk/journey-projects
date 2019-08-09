package com.atmaram.jp.cli;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.command.Command;
import com.atmaram.jp.model.*;
import com.atmaram.jp.model.rest.*;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.TemplateType;
import com.atmaram.tp.template.extractable.json.JSONTemplate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class UnitBuilder {

    private static VerbProcessor<GetUnit> get=new VerbProcessor<>(".get",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        Request request=buildRequestFromFile(file,false);
        GetUnit getUnit = new GetUnit(RestClient.get());
        getUnit.setUrlTemplate(request.getUrl());
        getUnit.setResponseTemplate(request.getResponse());
        getUnit.setRequestHeaders(request.getRequestHeaders());
        getUnit.setResponseHeaders(request.getResponseHeaders());
        getUnit.setWait(request.getWait());
        getUnit.setName(file.getName());
        getUnit.setResponseTemplateType(request.responseType);
        return getUnit;
    });
    private static VerbProcessor<FileUnit> file=new VerbProcessor<>(".file",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        FileUnit fileUnit=buildFileUnit(file,valueStore,variableStore,lEnv);
        return fileUnit;
    });
    private static VerbProcessor<PostUnit> post=new VerbProcessor<>(".post",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        Request request=buildRequestFromFile(file,true);
        PostUnit postUnit = new PostUnit(RestClient.get());
        postUnit.setUrlTemplate(request.url);
        postUnit.setRequestTemplate(request.body);
        postUnit.setResponseTemplate(request.response);
        postUnit.setRequestHeaders(request.requestHeaders);
        postUnit.setResponseHeaders(request.responseHeaders);
        postUnit.setWait(request.wait);
        postUnit.setName(file.getName());
        postUnit.setResponseTemplateType(request.responseType);
        postUnit.setRequestTemplateType(request.bodyType);
        return postUnit;
    });
    private static VerbProcessor<PutUnit> put=new VerbProcessor<>(".put",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        Request request=buildRequestFromFile(file,true);
        PutUnit putUnit = new PutUnit(RestClient.get());
        putUnit.setUrlTemplate(request.url);
        putUnit.setRequestTemplate(request.body);
        putUnit.setResponseTemplate(request.response);
        putUnit.setRequestHeaders(request.requestHeaders);
        putUnit.setResponseHeaders(request.responseHeaders);
        putUnit.setWait(request.wait);
        putUnit.setName(file.getName());
        putUnit.setResponseTemplateType(request.responseType);
        putUnit.setRequestTemplateType(request.bodyType);
        return putUnit;
    });
    private static VerbProcessor<PatchUnit> patch=new VerbProcessor<>(".patch",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        Request request=buildRequestFromFile(file,true);
        PatchUnit patchUnit = new PatchUnit(RestClient.get());
        patchUnit.setUrlTemplate(request.url);
        patchUnit.setRequestTemplate(request.body);
        patchUnit.setResponseTemplate(request.response);
        patchUnit.setRequestHeaders(request.requestHeaders);
        patchUnit.setResponseHeaders(request.responseHeaders);
        patchUnit.setWait(request.wait);
        patchUnit.setName(file.getName());
        patchUnit.setResponseTemplateType(request.responseType);
        patchUnit.setRequestTemplateType(request.bodyType);
        return patchUnit;
    });
    private static VerbProcessor<DeleteUnit> delete=new VerbProcessor<>(".delete",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        Request request=buildRequestFromFile(file,true);
        DeleteUnit deleteUnit = new DeleteUnit(RestClient.get());
        deleteUnit.setUrlTemplate(request.url);
        deleteUnit.setRequestTemplate(request.body);
        deleteUnit.setResponseTemplate(request.response);
        deleteUnit.setRequestHeaders(request.requestHeaders);
        deleteUnit.setResponseHeaders(request.responseHeaders);
        deleteUnit.setWait(request.wait);
        deleteUnit.setName(file.getName());
        deleteUnit.setResponseTemplateType(request.responseType);
        deleteUnit.setRequestTemplateType(request.bodyType);
        return deleteUnit;
    });
    private static VerbProcessor<BlockUnit> block=new VerbProcessor<>(".block",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        if(file.isDirectory() && file.getName().endsWith(".block")){
            return readBlockUnit(file,valueStore,variableStore,lEnv);
        } else {
            return null;
        }
    });
    private static VerbProcessor<StaticLoopUnit> loop=new VerbProcessor<>(".loop",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        if(file.isDirectory() && file.getName().endsWith(".loop")){
            return readLoopUnit(file,valueStore,variableStore,lEnv);
        } else {
            return null;
        }
    });
    private static VerbProcessor<PollUnit> poll=new VerbProcessor<>(".poll",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        if(file.isDirectory() && file.getName().endsWith(".poll")){
            return readPollUnit(file,valueStore,variableStore,lEnv);
        } else {
            return null;
        }
    });
    private static VerbProcessor<CommandUnit> command=new VerbProcessor<>(".journey",(File file, ValueStore valueStore,VariableStore variableStore,List<String> lEnv)->{
        if(file.isFile() && file.getName().endsWith(".journey")){
            try {
                return readCommandUnit(file,valueStore,variableStore,lEnv);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateParseException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            return null;
        }
    });
    public static List<VerbProcessor> verbProcessors=Arrays.asList(get,post,delete,patch,put,block,loop,poll,command,file);
    public static Request buildRequestFromFile(File file,boolean withBody) throws FileNotFoundException, ParseException {
        Request request=new Request();
        Scanner scanner = new Scanner(file);
        request.setUrl(scanner.nextLine());
        String body = "";
        TemplateType requestTemplateType=TemplateType.Text;
        TemplateType responseTemplateType=TemplateType.Extractable;
        if (withBody) {
            String[] bodyData=scanner.nextLine().split(Main.saparator);
            if(bodyData.length==3){
                requestTemplateType=TemplateType.fromString(bodyData[1]);
                body=bodyData[2];
            } else if(bodyData.length==2) {
                requestTemplateType=TemplateType.Extractable;
                body=bodyData[1];
            } else {
                if(!bodyData[0].trim().equalsIgnoreCase("request")) {
                    body = bodyData[0];
                }
            }
        }
        request.setBodyType(requestTemplateType);
        request.setBody(body);
        String requestHeader = scanner.nextLine().split(Main.saparator)[1];
        JSONParser jsonParser = new JSONParser();
        JSONObject rqjo = (JSONObject) jsonParser.parse(requestHeader);
        List<RequestHeader> requestHeaders = new ArrayList<>();
        for (Object key :
                rqjo.keySet()) {
            RequestHeader rh = new RequestHeader();
            rh.setName((String) key);
            rh.setValueTemplate((String) rqjo.get(key));
            requestHeaders.add(rh);
        }
        requestHeaders.addAll(Main.globalHeaders);
        request.setRequestHeaders(requestHeaders);
        String[] responseData=scanner.nextLine().split(Main.saparator);
        String response="";
        if(responseData.length==3){
            responseTemplateType=TemplateType.fromString(responseData[1]);
            response=responseData[2];
        } else if(responseData.length==2) {
            responseTemplateType=TemplateType.Extractable;
            response=responseData[1];
        } else {
            responseTemplateType=TemplateType.Extractable;
            if(!responseData[0].trim().equalsIgnoreCase("response")) {
                response = responseData[0];
            }
        }
        request.setResponseType(responseTemplateType);
        request.setResponse(response);

        String responseHeader = scanner.nextLine().split(Main.saparator)[1];
        JSONObject rsjo = (JSONObject) jsonParser.parse(responseHeader);
        List<ResponseHeader> responseHeaders = new ArrayList<>();
        for (Object key :
                rsjo.keySet()) {
            ResponseHeader rh = new ResponseHeader();
            rh.setName((String) key);
            rh.setVariable((String) rsjo.get(key));
            responseHeaders.add(rh);
        }
        request.setResponseHeaders(responseHeaders);
        int wait = 0;
        if (scanner.hasNextLine()) {
            wait = Integer.parseInt(scanner.nextLine().split(Main.saparator)[1]);
        }
        request.setWait(wait);
        return request;
    }
    public static Unit buildFromFile(File file,ValueStore valueStore,VariableStore variableStore,List<String> lEnv) throws FileNotFoundException, ParseException {
        for (VerbProcessor<Unit> processor:
                verbProcessors) {
            if(file.getName().endsWith(processor.verb)){
                return processor.transformer.transform(file,valueStore,variableStore,lEnv);
            }
        }
        return null;
    }
    private static BlockUnit readBlockUnit(File dir,ValueStore valueStore,VariableStore variableStore,List<String> lEnv) throws FileNotFoundException, ParseException {
        BlockUnit blockUnit=new BlockUnit();
        blockUnit.setName(dir.getName());
        File[] infoFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".info");
            }
        });
        Arrays.sort(infoFiles);
        for (File file:
                infoFiles) {
            Scanner scanner=new Scanner(file);
            blockUnit.setCounterVariable(scanner.nextLine().split(Main.saparator)[1]);
            if (scanner.hasNextLine()){
                blockUnit.setWait(Integer.parseInt(scanner.nextLine().split(Main.saparator)[1]));
            }
            if (scanner.hasNextLine()){
                blockUnit.setFilter(scanner.nextLine().split(Main.saparator)[1]);
            }
            if (scanner.hasNextLine()){
                blockUnit.setSort((JSONArray) JSONTemplate.stringToJSON(scanner.nextLine().split(Main.saparator)[1]));
            }
        }
        List<Unit> units=new ArrayList<>();
        File[] varFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".var");
            }
        });
        Arrays.sort(varFiles);

        File[] files=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {

                return isCommand(name);
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
            units.add(buildFromFile(file,valueStore,variableStore,lEnv));
        }
        blockUnit.setUnits(units);
        blockUnit.setVariables(getCommandVariables(varFiles));
        return blockUnit;
    }
    private static FileUnit buildFileUnit(File file,ValueStore valueStore,VariableStore variableStore,List<String> lEnv) throws FileNotFoundException, ParseException {
        FileUnit fileUnit=new FileUnit();
        fileUnit.setName(file.getName());

            Scanner scanner=new Scanner(file);
            fileUnit.setCounterVariable(scanner.nextLine().split(Main.saparator)[1]);
            fileUnit.setLineTemplate(scanner.nextLine().split(Main.saparator)[1]);
            if (scanner.hasNextLine()){
                fileUnit.setFilename(scanner.nextLine().split(Main.saparator)[1]);
            }
            if (scanner.hasNextLine()){
                fileUnit.setWait(Integer.parseInt(scanner.nextLine().split(Main.saparator)[1]));
            }
            if (scanner.hasNextLine()){
                fileUnit.setFilter(scanner.nextLine().split(Main.saparator)[1]);
            }
            if (scanner.hasNextLine()){
                fileUnit.setSort((JSONArray) JSONTemplate.stringToJSON(scanner.nextLine().split(Main.saparator)[1]));
            }

        return fileUnit;
    }
    private static StaticLoopUnit readLoopUnit(File dir,ValueStore valueStore,VariableStore variableStore,List<String> lEnv) throws FileNotFoundException, ParseException {
        StaticLoopUnit staticLoopUnit=new StaticLoopUnit();
        staticLoopUnit.setName(dir.getName());
        File[] infoFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".info");
            }
        });
        Arrays.sort(infoFiles);
        for (File file:
                infoFiles) {
            Scanner scanner=new Scanner(file);
            staticLoopUnit.setCounterVariable(scanner.nextLine().split(Main.saparator)[1]);
            if (scanner.hasNextLine()){
                staticLoopUnit.setTimes(scanner.nextLine().split(Main.saparator)[1]);
            }
            if (scanner.hasNextLine()){
                staticLoopUnit.setWait(Integer.parseInt(scanner.nextLine().split(Main.saparator)[1]));
            }
        }
        List<Unit> units=new ArrayList<>();
        File[] varFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".var");
            }
        });
        Arrays.sort(varFiles);

        File[] files=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {

                return isCommand(name);
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
            units.add(buildFromFile(file,valueStore,variableStore,lEnv));
        }
        staticLoopUnit.setUnits(units);
        staticLoopUnit.setVariables(getCommandVariables(varFiles));
        return staticLoopUnit;
    }

    private static PollUnit readPollUnit(File dir,ValueStore valueStore,VariableStore variableStore,List<String> lEnv) throws FileNotFoundException, ParseException {
        PollUnit pollUnit=new PollUnit();
        pollUnit.setName(dir.getName());
        File[] infoFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".info");
            }
        });
        Arrays.sort(infoFiles);
        for (File file:
                infoFiles) {
            Scanner scanner=new Scanner(file);
            String[] pollArgs=scanner.nextLine().split(Main.saparator);
            pollUnit.setPollVariableName(pollArgs[0]);
            pollUnit.setPollVariableValue(pollArgs[1]);

            if (scanner.hasNextLine()){
                pollUnit.setWait(Integer.parseInt(scanner.nextLine().split(Main.saparator)[1]));
            }
        }
        File[] varFiles=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".var");
            }
        });
        Arrays.sort(varFiles);

        File[] files=dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return isCommand(name);
            }
        });
        Arrays.sort(files,new Comparator()
        {
            @Override
            public int compare(Object f1, Object f2) {
                return ((File) f1).getName().compareTo(((File) f2).getName());
            }
        });
        if(files.length>0){
            pollUnit.setPollThis(buildFromFile(files[0],valueStore,variableStore,lEnv));
        }
        return pollUnit;
    }
    private static CommandUnit readCommandUnit(File file,ValueStore valueStore,VariableStore variableStore,List<String> lEnv) throws IOException, ParseException, TemplateParseException {
        CommandUnit commandUnit=new CommandUnit();
        Scanner scanner = new Scanner(file);
        commandUnit.setName(file.getName());
        String[] commandArgs=scanner.nextLine().split(Main.saparator);
        String commandDir=commandArgs[1];
        Path baseCommandDir = Paths.get("config/commands/"+commandDir.trim());
        Command command=Main.readCommand(baseCommandDir,valueStore,variableStore,lEnv);
        commandUnit.setCommand(command);
        return commandUnit;
    }
    public static List<EnvironmentVariable> getCommandVariables(File[] files) throws FileNotFoundException {
        List<EnvironmentVariable> variables=new ArrayList<>();
        for (File file:
                files) {
            Scanner scanner=new Scanner(file);
            while (scanner.hasNextLine()){
                String line=scanner.nextLine();
                String[] strings=line.split(Main.saparator);
                EnvironmentVariable environmentVariable=new EnvironmentVariable(strings[0],strings[1]);
                variables.add(environmentVariable);
            }
        }
        return variables;
    }
    public static boolean isCommand(String name){
        for(int i=0;i<UnitBuilder.verbProcessors.size();i++){
            if(name.endsWith(UnitBuilder.verbProcessors.get(i).verb))
                return true;
        }
        return false;
    }
}
