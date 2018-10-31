package com.atmaram.tp.xml;

import com.atmaram.tp.Template;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.VariableValueProcessor;
import com.atmaram.tp.json.JSONTemplate;
import org.json.simple.JSONAware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TemplateVariableTemplate implements XMLTemplate{
    String variableName;

    public TemplateVariableTemplate(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public List<Variable> getVariables() {
        return Arrays.asList();
    }

    @Override
    public List<Variable> getTemplateVariables() {
        if(variableName.startsWith("_") && !variableName.equals("_this")){
            return Arrays.asList();
        } else {
            Variable variable = new Variable();
            variable.setName(variableName);
            variable.setType("String");
            return Arrays.asList(variable);
        }
    }

    @Override
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        Object putValue=VariableValueProcessor.getValue(variableName,data);
        if(putValue instanceof String && Template.isVariable((String)putValue)){
            return this;
        } else if(putValue instanceof JSONAware){
            return XMLTemplate.from(putValue);
        } else {
            return new FilledVariableTemplate(putValue);
        }
    }

    @Override
    public XMLTemplate fill(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        return new HashMap<>();
    }

    @Override
    public Object toXMLCompatibleObject() {
        String xmlTemplate="#{"+variableName+"}";
        return xmlTemplate;
    }
    @Override
    public String toStringTemplate() {
        return (String)toXMLCompatibleObject();
    }
}
