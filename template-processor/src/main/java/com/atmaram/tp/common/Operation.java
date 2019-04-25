package com.atmaram.tp.common;

import java.text.SimpleDateFormat;
import java.util.*;

public enum Operation implements OperationEvaluator{
    ADD("plus",(args)->{
        Object result=0;
        for (int i=0;i<args.size();i++){
            Object value=args.get(i);
            result = CalculationUtil.calculate(CalculationUtil.Operation.ADD,result,value);
        }
        return result;
    }),
    DAY("day",(args)->{
        Calendar calendar=Calendar.getInstance();
        if(args!=null && args.size()==1){
            Date date=(Date)args.get(0);
            calendar.setTime(date);
        }
        return calendar.get(Calendar.DAY_OF_MONTH);
    }),
    MONTH("month",(args)->{
        Calendar calendar=Calendar.getInstance();
        if(args!=null && args.size()==1){
            Date date=(Date)args.get(0);
            calendar.setTime(date);
        }
        return calendar.get(Calendar.MONTH)+1;
    }),
    YEAR("year",(args)->{
        Calendar calendar=Calendar.getInstance();
        if(args!=null && args.size()==1){
            Date date=(Date)args.get(0);
            calendar.setTime(date);
        }
        return calendar.get(Calendar.YEAR);
    }),
    FORMAT_DATE("format",(args)->{
        SimpleDateFormat formatter=new SimpleDateFormat((String)args.get(1));
        return formatter.format((Date)args.get(0));
    }),
    NOW("now",(args)->{
        Date date=new Date();
        if(args==null || args.size()==0){
            return date;
        }
        SimpleDateFormat formatter=new SimpleDateFormat((String)args.get(0));
        return formatter.format(date);
    }),
    ADD_MONTHS("add_months",(args)->{
        Date date=(Date)args.get(0);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Object oToAdd=args.get(1);
        int iToAdd=0;
        if(oToAdd instanceof String){
            iToAdd=Integer.parseInt((String)oToAdd);
        } else if(oToAdd instanceof Integer){
            iToAdd=((Integer)oToAdd);
        }
        calendar.add(Calendar.MONTH,iToAdd);
        Date monthAddedDate=calendar.getTime();
        return monthAddedDate;
    }),
    ADD_YEAR("add_year",(args)->{
        Date date=(Date)args.get(0);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Object oToAdd=args.get(1);
        int iToAdd=0;
        if(oToAdd instanceof String){
            iToAdd=Integer.parseInt((String)oToAdd);
        } else if(oToAdd instanceof Integer){
            iToAdd=((Integer)oToAdd);
        }
        calendar.add(Calendar.YEAR,iToAdd);
        Date monthAddedDate=calendar.getTime();
        return monthAddedDate;
    }),
    ADD_DAYS("add_days",(args)->{
        Date date=(Date)args.get(0);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Object oToAdd=args.get(1);
        int iToAdd=0;
        if(oToAdd instanceof String){
            iToAdd=Integer.parseInt((String)oToAdd);
        } else if(oToAdd instanceof Integer){
            iToAdd=((Integer)oToAdd);
        }
        calendar.add(Calendar.DATE,iToAdd);
        Date monthAddedDate=calendar.getTime();
        return monthAddedDate;
    }),
    CONCAT("concat",(args)->{
        String result="";
        for (int i=0;i<args.size();i++){
            Object value=args.get(i);
            result+=value.toString();
        }
        return result;
    }),
    SUBSTRACT("minus",(args)->{
        Object result=args.get(0);
        for (int i=1;i<args.size();i++){
            Object value=args.get(i);
            result = CalculationUtil.calculate(CalculationUtil.Operation.Substract,result,value);
        }
        return result;
    }),
    TIMESTAMP("timestamp",(args)->{
        return new Date().getTime();
    }),
    EVAL("eval",(args)->{
        return ExpressionProcessor.getVal((String)args.get(0));
    }),
    UUID("uuid",(args)->{
        return java.util.UUID.randomUUID().toString();
    }),
    FIRST("first",(args)->{
        String str=(String)args.get(0);
        Integer len=(Integer) args.get(1);
        return str.substring(0,len);
    }),
    LAST("last",(args)->{
        String str=(String)args.get(0);
        Integer len=(Integer) args.get(1);
        return str.substring(str.length()-len);
    }),
    MID("mid",(args)->{
        String str=(String)args.get(0);
        int from=(Integer) args.get(1);
        int len=(Integer) args.get(2);
        return str.substring(from,from+len);
    }),
    Multiply("mul",(args)->{
        Object result=1;
        for (int i=0;i<args.size();i++){
            Object value=args.get(i);
            result = CalculationUtil.calculate(CalculationUtil.Operation.Multiply,result,value);
        }
        return result;
    }),
    Devide("divide",(args)->{
        Object result=args.get(0);
        for (int i=1;i<args.size();i++){
            Object value=args.get(i);
            result = CalculationUtil.calculate(CalculationUtil.Operation.Devide,result,value);
        }
        return result;
    }),
    Len("len",(args)->{
        String result=(String) args.get(0);
        return result.length();
    }),
    UPPER("upper",(args)->{
        String result=(String) args.get(0);
        return result.toUpperCase();
    }),
    LOWER("lower",(args)->{
        String result=(String) args.get(0);
        return result.toLowerCase();
    }),
    RANDOM("rand",(args)->{
        Integer lower=(Integer) args.get(0);
        Integer upper=(Integer) args.get(1);
        Random random=new Random();
        if(upper==lower){
            return lower;
        }
        int num=random.nextInt(upper-lower+1);
        return num+lower;
    }),
    size("size",(args)->{
        List result=(List) args.get(0);
        return result.size();
    }),
    get("get",(args)->{
        HashMap<String,Object> result=(HashMap) args.get(0);
        String key=(String) args.get(1);
        return result.get(key);
    }),
    pathGet("pathGet",(args)->{
        HashMap<String,Object> result=(HashMap) args.get(0);
        HashMap<String,Object> target=result;
        String path=(String) args.get(1);
        String[] keys=path.split(".");
        for (String key:
             keys) {
            target=(HashMap)target.get(key);
        }
        return target;
    }),
    isEqual("isEqual",(args)->{
        Object one=args.get(0);
        Object two=args.get(1);
        return one.equals(two);
    });
    public OperationEvaluator evaluator;
    public String function;
    Operation(final String function,final OperationEvaluator evaluator){
        this.function=function;
        this.evaluator=evaluator;
    }
    @Override
    public Object toValue(List<Object> args){
        return evaluator.toValue(args);
    }

}
