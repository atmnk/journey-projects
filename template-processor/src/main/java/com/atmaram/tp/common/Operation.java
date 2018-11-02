package com.atmaram.tp.common;

import java.text.SimpleDateFormat;
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
    DAY("day",(args,context)->{
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }),
    MONTH("month",(args,context)->{
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }),
    YEAR("year",(args,context)->{
        Date date=new Date();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }),
    FORMAT_DATE("format",(args,context)->{
        SimpleDateFormat formatter=new SimpleDateFormat((String)args.get(1).toValue(context));
        return formatter.format((Date)args.get(0).toValue(context));
    }),
    NOW("now",(args,context)->{
        Date date=new Date();
        if(args.size()==0){
            return date;
        }
        SimpleDateFormat formatter=new SimpleDateFormat((String)args.get(0).toValue(context));
        return formatter.format(date);
    }),
    ADD_MONTHS("add_months",(args,context)->{
        Date date=(Date)args.get(0).toValue(context);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Object oToAdd=args.get(1).toValue(context);
        int iToAdd=0;
        if(oToAdd instanceof String){
            iToAdd=Integer.parseInt((String)oToAdd);
        } else if(oToAdd instanceof Integer){
            iToAdd=((Integer)oToAdd);
        } else {
            try{
                iToAdd=(int)oToAdd;
            }catch (Exception ex){

            }
        }
        calendar.add(Calendar.MONTH,iToAdd);
        Date monthAddedDate=calendar.getTime();
        return monthAddedDate;
    }),
    ADD_YEAR("add_year",(args,context)->{
        Date date=(Date)args.get(0).toValue(context);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Object oToAdd=args.get(1).toValue(context);
        int iToAdd=0;
        if(oToAdd instanceof String){
            iToAdd=Integer.parseInt((String)oToAdd);
        } else if(oToAdd instanceof Integer){
            iToAdd=((Integer)oToAdd);
        } else {
            try{
                iToAdd=(int)oToAdd;
            }catch (Exception ex){

            }
        }
        calendar.add(Calendar.YEAR,iToAdd);
        Date monthAddedDate=calendar.getTime();
        return monthAddedDate;
    }),
    ADD_DAYS("add_days",(args,context)->{
        Date date=(Date)args.get(0).toValue(context);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Object oToAdd=args.get(1).toValue(context);
        int iToAdd=0;
        if(oToAdd instanceof String){
            iToAdd=Integer.parseInt((String)oToAdd);
        } else if(oToAdd instanceof Integer){
            iToAdd=((Integer)oToAdd);
        } else {
            try{
                iToAdd=(int)oToAdd;
            }catch (Exception ex){

            }
        }
        calendar.add(Calendar.DAY_OF_MONTH,iToAdd);
        Date monthAddedDate=calendar.getTime();
        return monthAddedDate;
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
