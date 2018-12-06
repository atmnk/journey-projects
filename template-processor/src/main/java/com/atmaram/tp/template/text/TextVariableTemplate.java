package com.atmaram.tp.template.text;

import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.ExpressionProcessor;

import java.util.HashMap;
import java.util.List;

class TextVariableTemplate implements TextTemplate {
    String variableName;

    public TextVariableTemplate(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public TextTemplate fill(HashMap<String, Object> data,boolean lazy) {
        String filledValue=ExpressionProcessor.process(variableName,data,lazy).toString();
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
