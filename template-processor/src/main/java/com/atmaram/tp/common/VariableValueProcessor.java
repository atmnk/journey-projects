package com.atmaram.tp.common;

import java.util.*;

public class VariableValueProcessor {
    private Executable executable;
    private Matchable matchable;

    public VariableValueProcessor(Matchable matchable,Executable executable) {
        this.executable = executable;
        this.matchable = matchable;
    }

    static VariableValueProcessor timestamp=new VariableValueProcessor((String expression)->expression.equals("_timestamp"),(String expression)->new Date().getTime());
    static VariableValueProcessor eval=new VariableValueProcessor((String expression)->expression.startsWith("_eval"),(String expression)->getVal(expression.substring(6,expression.length()-1)));
    static VariableValueProcessor uuid=new VariableValueProcessor((String expression)->expression.equals("_uuid"), (String expression)->UUID.randomUUID().toString());
    static List<VariableValueProcessor> allProcessors=Arrays.asList(timestamp,eval,uuid);
    public static void addProcessor(VariableValueProcessor variableValueProcessor){
        allProcessors.add(variableValueProcessor);
    }
    public static Object getValue(String name, HashMap<String,Object> data) {
        if(name.startsWith("_")){
            if(name.equals("_this")){
                return data.get("_this");
            } else {
                for (VariableValueProcessor variableValueProcessor:
                     allProcessors) {
                    if(variableValueProcessor.matchable.match(name)){
                        return variableValueProcessor.executable.execute(name);
                    }
                }
            }
        }
        if(data.containsKey(name)){
            return data.get(name);
        } else {
            return "${"+name+"}";
        }

    }
    private static String getVal(String pattern){
        if(pattern.equals(""))
            return "";
        if(pattern.contains("(") && pattern.contains(")")){
            String newpat=pattern.substring(1,pattern.length()-1);
            String start=newpat.split(",")[0];
            String end=newpat.split(",")[1];
            return between(start,end);
        } else if(pattern.contains("[") && pattern.contains("]")){
            String newpat=pattern.substring(1,pattern.length()-2);
            return among(newpat);
        }else {
            String value=pattern;
            Random random=new Random();
            while(value.contains("#"))
            {
                value=value.replaceFirst("#", Integer.toString(random.nextInt(9)));
            }
            return value;
        }
    }
    public static String between(String start,String end){
        start=getVal(start);
        end=getVal(end);
        int int1=Integer.parseInt(start);
        int int2=Integer.parseInt(end);
        return Integer.toString(new Random().nextInt(int2-int1)+int1);

    }
    public static String among(String list){
        String [] sList=list.split(",");
        int index=new Random().nextInt(sList.length-1);
        return getVal(sList[index]);
    }
}
