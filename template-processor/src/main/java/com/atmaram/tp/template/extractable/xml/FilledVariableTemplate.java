package com.atmaram.tp.template.extractable.xml;


import com.atmaram.tp.template.Variable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class FilledVariableTemplate implements XMLTemplate {
    Object variableValue;

    public FilledVariableTemplate(Object variableValue) {
        this.variableValue = variableValue;
    }

    @Override
    public List<Variable> getVariables() {
        return Arrays.asList();
    }

    @Override
    public List<Variable> getTemplateVariables() {
        return Arrays.asList();
    }

    @Override
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public XMLTemplate fill(HashMap<String, Object> data,boolean lazy) {
        return this;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        return new HashMap<>();
    }

    @Override
    public Object toXMLCompatibleObject() {
        return variableValue;
    }
    @Override
    public String toStringTemplate() {
        return toXMLCompatibleObject().toString();
    }
}
