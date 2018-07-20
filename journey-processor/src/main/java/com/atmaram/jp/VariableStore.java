package com.atmaram.jp;

import com.atmaram.tp.Variable;

import java.util.ArrayList;
import java.util.List;

public class VariableStore {
    private List<Variable> variables;

    public List<Variable> getVariables() {
        return variables;
    }

    private List<Variable> resolvedVariables;
    public VariableStore() {
        variables=new ArrayList<>();
        resolvedVariables=new ArrayList<>();
    }
    public void add(List<Variable> toAdd){
        for (Variable variable:
             toAdd) {
            if(variables.contains(variable))
                continue;
            else
                variables.add(variable);
        }
        resolve(resolvedVariables);
    }
    public void resolve(List<Variable> toResolve){
        for(int outer=0;outer< toResolve.size();outer++){
            Variable variable=toResolve.get(outer);
            for(int i=0;i<variables.size();i++){
                Variable qVariable=variables.get(i);
                boolean resolved=false;
                if(variable.getName().equals(qVariable.getName()) && qVariable.getType().equals("String")){
                    variables.remove(qVariable);
                    resolved=true;
                    i--;
                } else if (variable.getName().equals(qVariable.getName()) && qVariable.getType().equals("List") && variable.getType().equals("List")){
                    if(variable.getInner_variables()==null && qVariable.getInner_variables()==null){
                        variables.remove(qVariable);
                        resolved=true;
                        i--;

                    } else if(variable.getInner_variables()==null && qVariable.getInner_variables()!=null){
                        variables.addAll(qVariable.getInner_variables());
                        variables.remove(qVariable);
                        resolved=true;

                        i--;
                    } else if(variable.getInner_variables()!=null && qVariable.getInner_variables()==null){
                        variables.remove(qVariable);
                        i--;
                    } else {
                        VariableStore variableStore = new VariableStore();
                        variableStore.add(qVariable.getInner_variables());
                        variableStore.resolve(variable.getInner_variables());
                        variableStore.resolve(resolvedVariables);
                        qVariable.setInner_variables(variableStore.variables);
                        if (qVariable.getInner_variables().size() == 0) {
                            variables.remove(qVariable);
                            resolved=true;
                            i--;
                        }
                    }

                }
                if(qVariable.getType().equals("List") && !resolved && qVariable.getInner_variables()!=null && qVariable.getInner_variables().size()>0){
                    VariableStore variableStore=new VariableStore();
                    variableStore.add(qVariable.getInner_variables());
                    variableStore.resolve(resolvedVariables);
                    if(variableStore.variables.size()==0)
                    {
                        variables.remove(qVariable);
                        i--;
                    } else {
                        qVariable.setInner_variables(variableStore.getVariables());
                    }
                }
            }
            if(!resolvedVariables.contains(variable))
                resolvedVariables.add(variable);
        }
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
                variables) {
            if (variable.getName().equals(name) && variable.getType().equals(type))
                return variable;

        }
        return null;
    }


}
