package com.atmaram.tp.common;

import java.util.HashMap;
import java.util.List;

public class ExpressionTree {
    Operation rootProcessor;
    Object constant=null;
    String variable=null;
    List<ExpressionTree> args;
    public ExpressionTree() {
    }

    public Object getConstant() {
        return constant;
    }

    public void setConstant(Object constant) {
        this.constant = constant;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public List<ExpressionTree> getArgs() {
        return args;
    }

    public void setArgs(List<ExpressionTree> args) {
        this.args = args;
    }

    public Object toValue(HashMap<String,Object> context){
        if(constant!=null){
            return constant;
        }
        if(variable!=null){
            if(context.containsKey(variable))
                return context.get(variable);
            else {
                return "${"+variable+"}";
            }
        }
        return rootProcessor.toValue(args,context);
    }
}
