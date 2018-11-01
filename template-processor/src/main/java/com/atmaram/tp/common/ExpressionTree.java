package com.atmaram.tp.common;

import com.atmaram.tp.Variable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ExpressionTree {
    Operation rootProcessor;
    Object constant=null;
    String variable=null;
    List<ExpressionTree> args;
    public ExpressionTree() {
    }
    public List<Variable> getVariables(){
        if(constant!=null){
            return Arrays.asList();
        } else if(variable!=null){
            Variable vVariable = new Variable();
            vVariable.setName(variable);
            vVariable.setType("String");
            return Arrays.asList(vVariable);
        } else {
            List<Variable> list=new ArrayList<>();
            for(ExpressionTree childET:args){
                list.addAll(childET.getVariables());
            }
            return list;
        }
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
