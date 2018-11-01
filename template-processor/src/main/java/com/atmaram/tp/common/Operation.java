package com.atmaram.tp.common;

import java.util.*;

public enum Operation implements OperationEvaluator{
    ADD("plus",(args,context)->{
        List<Object> argumentValues=new ArrayList<>();
        Integer result=0;
        for (int i=0;i<args.size();i++){
            Object value=args.get(i).toValue(context);
            argumentValues.add(value);
            result+=(Integer) value;
        }
        return result;
    }),
    CONCAT("concat",(args,context)->{
        List<Object> argumentValues=new ArrayList<>();
        String result="";
        for (int i=0;i<args.size();i++){
            Object value=args.get(i).toValue(context);
            argumentValues.add(value);
            result+=value.toString();
        }
        return result;
    }),
    SUBSTRACT("minus",(args,context)->{
        Integer result=(Integer) args.get(0).toValue(context);
        result-=(Integer) args.get(1).toValue(context);
        return result;
    }),
    TIMESTAMP("timestamp",(args,context)->{
        return new Date().getTime();
    }),
    EVAL("eval",(args,context)->{
        return ExpressionProcessor.getVal((String)args.get(0).toValue(context));
    }),
    UUID("uuid",(args,context)->{
        return java.util.UUID.randomUUID().toString();
    }),
    FIRST("first",(args,context)->{
        String str=(String)args.get(0).toValue(context);
        Integer len=(Integer) args.get(1).toValue(context);
        return str.substring(0,len);
    }),
    LAST("last",(args,context)->{
        String str=(String)args.get(0).toValue(context);
        Integer len=(Integer) args.get(1).toValue(context);
        return str.substring(str.length()-len);
    }),
    MID("mid",(args,context)->{
        String str=(String)args.get(0).toValue(context);
        int from=(Integer) args.get(1).toValue(context);
        int len=(Integer) args.get(2).toValue(context);
        return str.substring(from,from+len);
    }),
    Multiply("mul",(args,context)->{
        Integer result=(Integer) args.get(0).toValue(context);
        result*=(Integer) args.get(1).toValue(context);
        return result;
    }),
    Devide("divide",(args,context)->{
        Integer result=(Integer) args.get(0).toValue(context);
        result/=(Integer) args.get(1).toValue(context);
        return result;
    }),
    Len("len",(args,context)->{
        String result=(String) args.get(0).toValue(context);
        return result.length();
    });
    public OperationEvaluator evaluator;
    public String function;
    Operation(final String function,final OperationEvaluator evaluator){
        this.function=function;
        this.evaluator=evaluator;
    }
    @Override
    public Object toValue(List<ExpressionTree> args,HashMap<String,Object> context){
        return evaluator.toValue(args,context);
    }

}
