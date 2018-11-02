package com.atmaram.tp;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Variable {
    String name;
    String type;
    List<Variable> inner_variables;
    public Variable clone(){
        Variable variable=new Variable();
        variable.name=this.name;
        variable.type=this.type;
        if(inner_variables!=null){
            variable.inner_variables=new ArrayList<>();
            for (Variable inner:
                 inner_variables) {
                variable.inner_variables.add(inner.clone());
            }
        }
        return variable;
    }
    public Variable mergeWith(Variable toVariable){
        if(name.equals(toVariable.name) && type.equals(toVariable.type) && type.equals("List")) {
            Variable variable=new Variable();
            variable.name=this.name;
            variable.type=this.type;
            List<Variable> inner=new ArrayList<>();
            if(inner_variables!=null) {
                for (int i = 0; i <inner_variables.size();i++)
                    if(inner_variables.get(i).type.equals("String"))
                        inner.add(inner_variables.get(i).clone());
            }
            if(toVariable.inner_variables!=null) {
                for (int i = 0; i <toVariable.inner_variables.size();i++)
                    if(toVariable.inner_variables.get(i).type.equals("String"))
                        inner.add(toVariable.inner_variables.get(i).clone());
            }
            if(inner_variables!=null){
                for (int i=0;i<inner_variables.size();i++){
                    Variable from=inner_variables.get(i);
                    if(from.type.equals("List")) {
                        if (toVariable.getInner_variables() != null) {
                            List<Variable> remaining=new ArrayList<>();
                            boolean found = false;
                            for (int j = 0; j < toVariable.inner_variables.size(); j++) {
                                Variable to = toVariable.inner_variables.get(j);
                                if (from.name.equals(to.name) && from.type.equals(to.type)) {
                                    found = true;
                                    inner.add(from.mergeWith(to));
                                    if(remaining.contains(to)){
                                        remaining.remove(to);
                                    }
                                } else {
                                    if(to.type.equals("List"))
                                        remaining.add(to);
                                }
                            }
                            if (!found) {
                                inner.add(from.clone());
                            }
                            for(int j=0;j<remaining.size();j++){
                                inner.add(remaining.get(j).clone());
                            }
                        } else {
                            inner.add(from.clone());
                        }
                    }
                }
            } else {
                if(toVariable.getInner_variables()!=null){
                    for(int j=0;j<toVariable.inner_variables.size();j++){
                        Variable to=toVariable.inner_variables.get(j);
                        if(to.type.equals("List"))
                            inner.add(to.clone());
                    }
                }
            }
            variable.inner_variables=inner;
            return variable;
        }
        return this.clone();
    }
}
