package com.atmaram.tp.template.extractable.json;


import com.atmaram.tp.template.Variable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class FilledVariableTemplate implements JSONTemplate {
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
    public JSONTemplate fillTemplateVariables(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public JSONTemplate fill(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        return new HashMap<>();
    }

    @Override
    public Object toJSONCompatibleObject() {
//        if(variableValue instanceof JSONAware || variableValue instanceof String || variableValue instanceof List || variableValue instanceof Map){
//            return variableValue;
//        }
        return variableValue;
    }

    @Override
    public String toStringTemplate() {
        return toJSONCompatibleObject().toString();
    }
}
