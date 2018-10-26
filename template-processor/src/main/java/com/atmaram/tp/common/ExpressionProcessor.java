package com.atmaram.tp.common;

import java.util.*;

public class ExpressionProcessor {
    public static Object process(String expression, HashMap<String,Object> context){
        String[] processorargs=expression.split(">");
        Object value;
        if(processorargs.length>1){
            value=toTree(processorargs[0]).toValue(context);
            context.put(processorargs[1],value);
            return value;
        }
        return toTree(processorargs[0]).toValue(context);
    }
    public static ExpressionTree toTree(String passedExpression){
        String expression=passedExpression.trim();
        ExpressionTree tree=new ExpressionTree();
        if(expression.startsWith("'")){
            ExpressionTree faET=null;
            ExpressionTree saET=null;
            for(int i=1;i<expression.length();i++){
                if(faET!=null) {
                    if (expression.charAt(i) == '+') {
                        tree.rootProcessor = Operation.ADD;
                    } else if (expression.charAt(i) == '-'){
                        tree.rootProcessor = Operation.SUBSTRACT;
                    } else if (expression.charAt(i) == '*'){
                        tree.rootProcessor = Operation.Multiply;
                    } else if (expression.charAt(i) == '/'){
                        tree.rootProcessor = Operation.Devide;
                    }
                    saET=toTree(expression.substring(i+1));
                    break;
                }
                if(expression.charAt(i)=='\'')
                {
                    String firstArg=expression.substring(1,i);
                    faET=new ExpressionTree();
                    faET.constant=firstArg;
                }
            }
            if(saET==null){
                return faET;
            } else {
                tree.setArgs(Arrays.asList(faET,saET));
                return tree;
            }
        } else if(Character.isDigit(expression.charAt(0))){
            ExpressionTree faET=null;
            ExpressionTree saET=null;
            for(int i=0;i<expression.length();i++){

                if (expression.charAt(i) == '+') {
                    tree.rootProcessor = Operation.ADD;
                    if(faET==null) {
                        Integer firstArg = Integer.parseInt(expression.substring(0, i));
                        faET = new ExpressionTree();
                        faET.constant = firstArg;
                    }
                        saET=toTree(expression.substring(i+1));
                    break;
                } else if (expression.charAt(i) == '-'){
                    tree.rootProcessor = Operation.SUBSTRACT;
                    if(faET==null) {
                        Integer firstArg = Integer.parseInt(expression.substring(0, i));
                        faET = new ExpressionTree();
                        faET.constant = firstArg;
                    }
                        saET=toTree(expression.substring(i+1));
                    break;
                } else if (expression.charAt(i) == '*'){
                    tree.rootProcessor = Operation.Multiply;
                    if(faET==null) {
                        Integer firstArg = Integer.parseInt(expression.substring(0, i));
                        faET = new ExpressionTree();
                        faET.constant = firstArg;
                    }

                        saET=toTree(expression.substring(i+1));
                    break;
                } else if (expression.charAt(i) == '/'){
                    tree.rootProcessor = Operation.Devide;
                    if(faET==null) {
                        Integer firstArg = Integer.parseInt(expression.substring(0, i));
                        faET = new ExpressionTree();
                        faET.constant = firstArg;
                    }

                        saET=toTree(expression.substring(i+1));
                    break;
                }


                if(!Character.isDigit(expression.charAt(i)))
                {
                    Integer firstArg=Integer.parseInt(expression.substring(0,i));
                    faET=new ExpressionTree();
                    faET.constant=firstArg;
                }
            }
            if(faET==null){
                Integer firstArg=Integer.parseInt(expression);
                faET=new ExpressionTree();
                faET.constant=firstArg;
            }
            if(saET==null){
                return faET;
            } else {
                tree.setArgs(Arrays.asList(faET,saET));
                return tree;
            }
        } else if(expression.startsWith("_")){
            if(expression.startsWith("_this")){
                ExpressionTree faET=new ExpressionTree();
                faET.setVariable("_this");
                ExpressionTree saET=null;
                for(int i=5;i<expression.length();i++){
                    if (expression.charAt(i) == '+') {
                        tree.rootProcessor = Operation.ADD;
                    } else if (expression.charAt(i) == '-') {
                        tree.rootProcessor = Operation.SUBSTRACT;
                    } else if (expression.charAt(i) == '*') {
                        tree.rootProcessor = Operation.Multiply;
                    } else if (expression.charAt(i) == '/') {
                        tree.rootProcessor = Operation.Devide;
                    }
                        saET = toTree(expression.substring(i+1));
                }
                if(saET==null){
                    return faET;
                } else {
                    tree.setArgs(Arrays.asList(faET,saET));
                    return tree;
                }
            } else {
                ExpressionTree faET=null;
                ExpressionTree saET=null;
                List<ExpressionTree> args=new ArrayList<>();
                for(int i=1;i<expression.length();i++){

                    if (expression.charAt(i) == '+') {
                        tree.rootProcessor = Operation.ADD;
                        if(faET==null) {
                            faET = new ExpressionTree();
                            ExpressionTree finalFaET = faET;
                            EnumSet.allOf(Operation.class).forEach(operation -> {
                                if (expression.startsWith("_" + operation.function)) {
                                    finalFaET.rootProcessor = operation;
                                    finalFaET.setArgs(new ArrayList<>());
                                }
                            });
                        }

                        saET = toTree(expression.substring(i+1));
                        break;
                    } else if (expression.charAt(i) == '-') {
                        tree.rootProcessor = Operation.SUBSTRACT;
                        if(faET==null) {
                            faET = new ExpressionTree();
                            ExpressionTree finalFaET = faET;
                            EnumSet.allOf(Operation.class).forEach(operation -> {
                                if (expression.startsWith("_" + operation.function)) {
                                    finalFaET.rootProcessor = operation;
                                    finalFaET.setArgs(new ArrayList<>());
                                }
                            });
                        }
                        saET = toTree(expression.substring(i+1));
                        break;
                    } else if (expression.charAt(i) == '*') {
                        tree.rootProcessor = Operation.Multiply;
                        if(faET==null) {
                            faET = new ExpressionTree();
                            ExpressionTree finalFaET = faET;
                            EnumSet.allOf(Operation.class).forEach(operation -> {
                                if (expression.startsWith("_" + operation.function)) {
                                    finalFaET.rootProcessor = operation;
                                    finalFaET.setArgs(new ArrayList<>());
                                }
                            });
                        }
                        saET = toTree(expression.substring(i+1));
                        break;
                    } else if (expression.charAt(i) == '/') {
                        tree.rootProcessor = Operation.Devide;
                        if(faET==null) {
                            faET = new ExpressionTree();
                            ExpressionTree finalFaET = faET;
                            EnumSet.allOf(Operation.class).forEach(operation -> {
                                if (expression.startsWith("_" + operation.function)) {
                                    finalFaET.rootProcessor = operation;
                                    finalFaET.setArgs(new ArrayList<>());
                                }
                            });
                        }
                        saET = toTree(expression.substring(i+1));
                        break;
                    }
                    if(expression.charAt(i)=='('){
                        final int prev=i;
                        faET=new ExpressionTree();
                        ExpressionTree finalFaET = faET;
                        EnumSet.allOf(Operation.class).forEach(operation -> {
                            if(expression.startsWith("_"+operation.function)) {
                                finalFaET.rootProcessor= operation;
                            }
                        });
                        List<ExpressionTree> innerArgs=new ArrayList<>();
                        //Create Argument Expression Tree
                        int bracketCount = 1;
                        int last = prev + 1;
                        for (int j = last; j < expression.length(); j++) {
                            if (expression.charAt(j) == '(')
                                bracketCount += 1;
                            if (expression.charAt(j) == ',' && bracketCount == 1) {
                                innerArgs.add(toTree(expression.substring(last, j)));
                                last = j+1;
                            }
                            if (expression.charAt(j) == ')' && bracketCount == 1) {
                                innerArgs.add(toTree(expression.substring(last, j)));
                                last = j+1;
                                finalFaET.setArgs(innerArgs);
                            }
                            if (expression.charAt(j) == ')')
                                bracketCount -= 1;
                        }
                        i=last-1;
                    }
                }
                if(args.size()==0) {
                    if(faET==null) {
                        faET = new ExpressionTree();
                        ExpressionTree finalFaET = faET;
                        EnumSet.allOf(Operation.class).forEach(operation -> {
                            if (expression.startsWith("_" + operation.function)) {
                                finalFaET.rootProcessor = operation;
                                finalFaET.setArgs(new ArrayList<>());
                            }
                        });
                    }
                    if (saET == null) {
                        return faET;
                    } else {
                        tree.setArgs(Arrays.asList(faET, saET));
                        return tree;
                    }
                } else {
                    tree.setArgs(args);
                    return tree;
                }
            }

        } else {
            ExpressionTree faET=null;
            ExpressionTree saET=null;
            for(int i=1;i<expression.length();i++) {
                if (expression.charAt(i) == '+') {
                    tree.rootProcessor = Operation.ADD;
                    String firstArg = expression.substring(1, i);
                    faET = new ExpressionTree();
                    faET.variable = firstArg;
                    saET = toTree(expression.substring(i+1));
                } else if (expression.charAt(i) == '-') {
                    tree.rootProcessor = Operation.SUBSTRACT;
                    String firstArg = expression.substring(1, i);
                    faET = new ExpressionTree();
                    faET.variable = firstArg;
                    saET = toTree(expression.substring(i+1 ));
                } else if (expression.charAt(i) == '*') {
                    tree.rootProcessor = Operation.Multiply;
                    String firstArg = expression.substring(1, i);
                    faET = new ExpressionTree();
                    faET.variable = firstArg;
                    saET = toTree(expression.substring(i+1 ));
                } else if (expression.charAt(i) == '/') {
                    tree.rootProcessor = Operation.Devide;
                    String firstArg = expression.substring(1, i);
                    faET = new ExpressionTree();
                    faET.variable = firstArg;
                    saET = toTree(expression.substring(i +1));
                }

            }
            if(faET==null){
                faET = new ExpressionTree();
                faET.variable = expression.trim();
            }
            if(saET==null){
                return faET;
            } else {
                tree.setArgs(Arrays.asList(faET,saET));
                return tree;
            }

        }
    }
}
