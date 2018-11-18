package com.atmaram.tp.template.extractable.text;


import com.atmaram.tp.template.Variable;
import com.atmaram.tp.template.extractable.ExtractableTemplate;
import com.atmaram.tp.template.extractable.json.JSONTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class FilledSingleVariableTextTemplate implements SingleVariableTemplate {
    Object variableValue;

    public FilledSingleVariableTextTemplate(Object variableValue) {
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
    public ExtractableTemplate fillTemplateVariables(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public ExtractableTemplate fill(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        return new HashMap<>();
    }


    @Override
    public String toStringTemplate() {
        return variableValue.toString();
    }
}
