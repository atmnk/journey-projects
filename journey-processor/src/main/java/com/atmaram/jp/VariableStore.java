package com.atmaram.jp;

import com.atmaram.tp.Variable;

import java.util.ArrayList;
import java.util.List;

public class VariableStore {
    private List<Variable> finalVariables;

    public List<Variable> getVariables() {
        return finalVariables;
    }

    private List<Variable> resolvedVariables;
    public VariableStore() {
        finalVariables=new ArrayList<>();
        resolvedVariables=new ArrayList<>();
    }
    public void add(List<Variable> toAdd){
        for(int i=0;i<toAdd.size();i++){
            Variable varToAdd=toAdd.get(i);
            if(finalVariables.contains(varToAdd))
                continue;
            else{
                boolean found=false;
                for(int j=0;j<resolvedVariables.size();j++){
                    Variable resolvedVariable=resolvedVariables.get(j);

                    if(resolvedVariable.getName().equals(varToAdd.getName()) && resolvedVariable.getType().equals(varToAdd.getType())){
                        found=true;
                        if(varToAdd.getType().equals("List")){
                            VariableStore variableStore=new VariableStore();
                            variableStore.resolve(resolvedVariable.getInner_variables());
                            variableStore.add(varToAdd.getInner_variables());
                            List<Variable> new_inner=variableStore.getVariables();
                            if(new_inner.size()!=0){
                                Variable finalVar=getFinalVariableWithNameAndType(varToAdd.getName(),varToAdd.getType());
                                if(finalVar!=null){
                                    finalVar.setInner_variables(new_inner);
                                }
                            }
                        }
                        break;
                    }
                }
                if(!found){
                    finalVariables.add(varToAdd.clone());
                }
            }
        }
    }
    public void resolve(List<Variable> toResolve){
        for (int i=0;i<toResolve.size();i++){
            Variable varToResolve=toResolve.get(i);
            if(resolvedVariables.contains(varToResolve))
                continue;
            else{
                Variable allreadyResolvedVar=getResolvedVariableWithNameAndType(varToResolve.getName(),varToResolve.getType());
                if(allreadyResolvedVar!=null){
                    if(allreadyResolvedVar.getType().equals("List")){
                        VariableStore newVariableStore=new VariableStore();
                        newVariableStore.resolve(allreadyResolvedVar.getInner_variables());
                        newVariableStore.resolve(varToResolve.getInner_variables());
                        allreadyResolvedVar.setInner_variables(newVariableStore.resolvedVariables);
                    }
                    continue;
                } else {
                    resolvedVariables.add(varToResolve.clone());
                }
            }
        }
        for(int i=0;i<finalVariables.size();i++){
            Variable finalVar=finalVariables.get(i);
            if(finalVar.getType().equals("List")){
                if(finalVar.getInner_variables()==null || finalVar.getInner_variables().size()==0) {
                    finalVariables.remove(finalVar);
                    i--;
                }
            }
        }
    }
    private Variable getResolvedVariableWithNameAndType(String Name,String Type) {
        for(int i=0;i<resolvedVariables.size();i++){
            Variable resolvedVariable=resolvedVariables.get(i);
            if(resolvedVariable.getName().equals(Name) && resolvedVariable.getType().equals(Type)){
                return resolvedVariable;
            }
        }
        return null;
    }
    private Variable getFinalVariableWithNameAndType(String Name,String Type) {
        for(int i=0;i<finalVariables.size();i++){
            Variable finalVariable=finalVariables.get(i);
            if(finalVariable.getName().equals(Name) && finalVariable.getType().equals(Type)){
                return finalVariable;
            }
        }
        return null;
    }
//    public void add(List<Variable> toAdd){
//        for (Variable variable:
//             toAdd) {
//            if(variables.contains(variable))
//                continue;
//            else
//                variables.add(variable);
//        }
//        resolve(resolvedVariables);
//    }
//    public void resolve(List<Variable> toResolve){
//        for(int outer=0;outer< toResolve.size();outer++){
//            Variable variable=toResolve.get(outer);
//            for(int i=0;i<variables.size();i++){
//                Variable qVariable=variables.get(i);
//                boolean resolved=false;
//                if(variable.getName().equals(qVariable.getName()) && qVariable.getType().equals("String")){
//                    variables.remove(qVariable);
//                    resolved=true;
//                    i--;
//                } else if (variable.getName().equals(qVariable.getName()) && qVariable.getType().equals("List") && variable.getType().equals("List")){
//                    if(variable.getInner_variables()==null && qVariable.getInner_variables()==null){
//                        variables.remove(qVariable);
//                        resolved=true;
//                        i--;
//
//                    } else if(variable.getInner_variables()==null && qVariable.getInner_variables()!=null){
//                        variables.addAll(qVariable.getInner_variables());
//                        variables.remove(qVariable);
//                        resolved=true;
//
//                        i--;
//                    } else if(variable.getInner_variables()!=null && qVariable.getInner_variables()==null){
//                        variables.remove(qVariable);
//                        i--;
//                    } else {
//                        VariableStore variableStore = new VariableStore();
//                        variableStore.add(qVariable.getInner_variables());
//                        variableStore.resolve(variable.getInner_variables());
//                        variableStore.resolve(resolvedVariables);
//                        qVariable.setInner_variables(variableStore.variables);
//                        if (qVariable.getInner_variables().size() == 0) {
//                            variables.remove(qVariable);
//                            resolved=true;
//                            i--;
//                        }
//                    }
//
//                }
//                if(qVariable.getType().equals("List") && !resolved && qVariable.getInner_variables()!=null && qVariable.getInner_variables().size()>0){
//                    VariableStore variableStore=new VariableStore();
//                    variableStore.add(qVariable.getInner_variables());
//                    variableStore.resolve(resolvedVariables);
//                    if(variableStore.variables.size()==0)
//                    {
//                        variables.remove(qVariable);
//                        i--;
//                    } else {
//                        qVariable.setInner_variables(variableStore.getVariables());
//                    }
//                }
//            }
//            if(!resolvedVariables.contains(variable))
//                resolvedVariables.add(variable);
//        }
//    }
    public Variable getResolved(String name,String type){
        for (Variable variable:
             resolvedVariables) {
            if (variable.getName().equals(name) && variable.getType().equals(type))
                return variable;

        }
        return null;
    }
    public Variable getUnResolved(String name,String type){
        for (Variable variable:
                finalVariables) {
            if (variable.getName().equals(name) && variable.getType().equals(type))
                return variable;

        }
        return null;
    }


}
