package com.atmaram.jp;

import com.atmaram.tp.Variable;

import java.util.ArrayList;
import java.util.List;

public class VariableStore {
    private List<Variable> finalVariables;

    public List<Variable> getVariables() {
        return finalVariables;
    }

    public List<Variable> getResolvedVariables() {
        return resolvedVariables;
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
                                    finalVar.getInner_variables().addAll(new_inner);
                                } else {
                                    Variable newVarToToAdd=new Variable();
                                    newVarToToAdd.setName(varToAdd.getName());
                                    newVarToToAdd.setType("List");
                                    newVarToToAdd.setInner_variables(new_inner);
                                    finalVariables.add(newVarToToAdd);
                                }
                            }
                        }
                        break;
                    }
                }
                if(!found){
                    Variable existingFinal=getFinalVariableWithNameAndType(varToAdd.getName(),varToAdd.getType());
                    if(existingFinal!=null){
                        Variable newFinal=existingFinal.mergeWith(varToAdd);
                        finalVariables.remove(existingFinal);
                        finalVariables.add(newFinal);
                    } else {
                        finalVariables.add(varToAdd.clone());
                    }
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
                        Variable newResolvedVariable=allreadyResolvedVar.mergeWith(varToResolve);
                        resolvedVariables.remove(allreadyResolvedVar);
                        resolvedVariables.add(newResolvedVariable);
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
