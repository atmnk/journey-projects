package com.atmaram.tp.common;

import java.text.SimpleDateFormat;
import java.util.*;

public enum Operation implements OperationEvaluator{
    ADD("plus",(args)->{
        Integer result=0;
        for (int i=0;i<args.size();i++){
            Object value=args.get(i);
            if(value instanceof Integer) {
                result += (Integer) value;
            } else if(value instanceof String){
                result+=Integer.parseInt((String)value);
            }
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
        Integer result=(Integer) args.get(0);
        result-=(Integer) args.get(1);
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
        Integer result=(Integer) args.get(0);
        result*=(Integer) args.get(1);
        return result;
    }),
    Devide("divide",(args)->{
        Integer result=(Integer) args.get(0);
        result/=(Integer) args.get(1);
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
