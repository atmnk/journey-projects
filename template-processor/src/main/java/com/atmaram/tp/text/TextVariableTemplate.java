package com.atmaram.tp.text;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.VariableValueProcessor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class TextVariableTemplate implements TextTemplate {
    String variableName;

    public TextVariableTemplate(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public TextTemplate fill(HashMap<String, Object> data) {
        String filledValue=VariableValueProcessor.getValue(variableName,data).toString();
        if(TextTemplate.isVariable(filledValue)){
            return this;
        } else {
            return new FilledTextTemplate(filledValue);
        }
    }

    @Override
    public List<Variable> getVariables() {
        Variable variable=new Variable();
        variable.setName(variableName);
        variable.setType("String");
        return Arrays.asList(variable);
    }

    @Override
    public String toValue() {
        return "${"+variableName+"}";
    }
}
