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
}
