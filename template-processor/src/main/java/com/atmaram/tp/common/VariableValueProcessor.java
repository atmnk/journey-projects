package com.atmaram.tp.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class VariableValueProcessor {
    public static Object getValue(String name, HashMap<String,Object> data) {
        if(name.startsWith("_")){
            if(name.equals("_this")){
                return data.get("_this");
            } else if(name.equals("_timestamp")){
                return new Date().getTime();
            } else if(name.startsWith("_eval")){
                return getVal(name.substring(6,name.length()-1));
            } else if(name.equals("_uuid")){
                return UUID.randomUUID().toString();
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
