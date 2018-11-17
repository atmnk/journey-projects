package com.atmaram.tp.common;

import com.atmaram.tp.template.Variable;

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

    public ExpressionTree solve(HashMap<String,Object> context){
        if(constant!=null){
            return this;
        }
        if(variable!=null){
            if(context.containsKey(variable)){
                ExpressionTree expressionTree=new ExpressionTree();
                expressionTree.constant=context.get(variable);
                return expressionTree;
            } else {
                return this;
            }
        }
        List<ExpressionTree> newArgs=new ArrayList<>();
        List<Object> newObjArgs=new ArrayList<>();
        boolean unsolved=false;
        for (int i=0;i<args.size();i++) {
            ExpressionTree newArg=args.get(i).solve(context);
            newArgs.add(newArg);
            if(newArg.constant==null) {
                unsolved = true;
            } else {
                newObjArgs.add(newArg.constant);
            }
        }
        if(unsolved) {
            ExpressionTree expressionTree = new ExpressionTree();
            expressionTree.rootProcessor=this.rootProcessor;
            expressionTree.args=newArgs;
            return expressionTree;
        } else {
            ExpressionTree expressionTree=new ExpressionTree();
            expressionTree.constant= rootProcessor.toValue(newObjArgs);
            return expressionTree;
        }
    }

    public Object toExpression(){
        if(constant!=null){
            if(constant instanceof String){
                return "'"+constant+"'";
            }
            if(constant instanceof Integer){
                return ((Integer)constant).toString();
            }
            return constant;
        }
        if(variable!=null){
            return variable;
        }
        if(rootProcessor!=null){
            List<String> inner_expressions=new ArrayList<>();
            if(args==null || args.size()==0){
                return "_"+rootProcessor.function;
            }
            for (int i=0;i<args.size();i++){
                String inner_expression=args.get(i).toExpression().toString();
                inner_expressions.add(inner_expression);
            }

            return "_"+rootProcessor.function+"("+String.join(",",inner_expressions)+")";
        }
        return "";
    }
}
