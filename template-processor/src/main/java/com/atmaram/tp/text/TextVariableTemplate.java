package com.atmaram.tp.text;

import com.atmaram.tp.Template;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.ExpressionProcessor;
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
        String filledValue=ExpressionProcessor.process(variableName,data).toString();
        if(Template.isVariable(filledValue)){
            return this;
        } else {
            return new FilledTextTemplate(filledValue);
        }
    }

    @Override
    public String toStringTemplate() {
        return "${"+variableName+"}";
    }

    @Override
    public List<Variable> getVariables() {
        return ExpressionProcessor.getVariables(variableName);
//        if(variableName.startsWith("_") && !variableName.equals("_this")){
//            return Arrays.asList();
//        } else {
//            Variable variable = new Variable();
//            variable.setName(variableName);
//            variable.setType("String");
//            return Arrays.asList(variable);
//        }
    }

}
