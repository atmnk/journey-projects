package com.atmaram.tp.common;

import java.util.*;

public class VariableValueProcessor {
    private Executable executable;
    private Matchable matchable;

    public VariableValueProcessor(Matchable matchable,Executable executable) {
        this.executable = executable;
        this.matchable = matchable;
    }

    static VariableValueProcessor timestamp=new VariableValueProcessor((String expression)->expression.startsWith("_timestamp"),(String expression,HashMap<String,Object> data)->new Date().getTime());
    static VariableValueProcessor eval=new VariableValueProcessor((String expression)->expression.startsWith("_eval"),(String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return getVal(newExpr.substring(6,newExpr.length()-1));
    });
    static VariableValueProcessor uuid=new VariableValueProcessor((String expression)->expression.startsWith("_uuid"), (String expression,HashMap<String,Object> data)->UUID.randomUUID().toString());
    static VariableValueProcessor concat=new VariableValueProcessor((String expression)->expression.startsWith("_+"), (String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return concat(newExpr.substring(3,newExpr.length()-1).split(","),data);
    });
    static VariableValueProcessor mid=new VariableValueProcessor((String expression)->expression.startsWith("_mid"), (String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return mid(newExpr.substring(5,newExpr.length()-1).split(","),data);
    });
    static VariableValueProcessor last=new VariableValueProcessor((String expression)->expression.startsWith("_last"), (String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return last(newExpr.substring(6,newExpr.length()-1).split(","),data);
    });
    static VariableValueProcessor first=new VariableValueProcessor((String expression)->expression.startsWith("_first"), (String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return first(newExpr.substring(7,newExpr.length()-1).split(","),data);
    });
    static VariableValueProcessor len=new VariableValueProcessor((String expression)->expression.startsWith("_len"), (String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return len(newExpr.substring(5,newExpr.length()-1).split(","),data);
    });
    static VariableValueProcessor plus=new VariableValueProcessor((String expression)->expression.startsWith("_plus"), (String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return plus(newExpr.substring(6,newExpr.length()-1).split(","),data);
    });
    static VariableValueProcessor minus=new VariableValueProcessor((String expression)->expression.startsWith("_minus"), (String expression,HashMap<String,Object> data)->{
        String newExpr=expression.split(">")[0];
        return minus(newExpr.substring(7,newExpr.length()-1).split(","),data);
    });
    public static String plus(String[] args,HashMap<String,Object> data){
        int one=Integer.parseInt((String)getValue(args[0],data));
        int two=Integer.parseInt((String)getValue(args[1],data));
        return Integer.toString(one+two);
    }
    public static String minus(String[] args,HashMap<String,Object> data){
        int one=Integer.parseInt((String)getValue(args[0],data));
        int two=Integer.parseInt((String)getValue(args[1],data));
        return Integer.toString(one-two);
    }
    public static String mid(String[] args,HashMap<String,Object> data){
        String str=(String)getValue(args[0],data);
        int from=Integer.parseInt((String)getValue(args[1],data));
        int len=Integer.parseInt((String)getValue(args[2],data));
        return str.substring(from,from+len);
    }
    public static String first(String[] args,HashMap<String,Object> data){
        String str=(String)getValue(args[0],data);
        int len=Integer.parseInt((String)getValue(args[1],data));
        return str.substring(0,len);
    }
    public static String last(String[] args,HashMap<String,Object> data){
        String str=(String)getValue(args[0],data);
        int len=Integer.parseInt((String)getValue(args[1],data));
        return str.substring(str.length()-len,str.length());
    }
    public static String len(String[] args,HashMap<String,Object> data){
        return Integer.toString(((String)getValue(args[0],data)).length());
    }
    public static String concat(String[] args,HashMap<String,Object> data){
        String res="";
        for(int i=0;i<args.length;i++){
            res+=getValue(args[i],data);
        }
        return res;
    }
    static List<VariableValueProcessor> allProcessors=Arrays.asList(timestamp,eval,uuid,concat,first,mid,last,len,plus,minus);
    public static void addProcessor(VariableValueProcessor variableValueProcessor){
        allProcessors.add(variableValueProcessor);
    }
    public static Object getValue(String name, HashMap<String,Object> data) {
        return ExpressionProcessor.process(name,data);
    }
    public static String getVal(String pattern){
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
