package com.atmaram.tp.template.extractable.text;

import com.atmaram.tp.common.ExpressionProcessor;
import com.atmaram.tp.common.ExpressionTree;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.template.extractable.ExtractableTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SingleVariableStringTemplate implements SingleVariableTemplate {
    String variableName;

    public SingleVariableStringTemplate(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public HashMap<String, Object> extract(Object from) {
        HashMap<String,Object> data=new HashMap<>();
        data.put(variableName,from);
        return data;
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
    public String toStringTemplate() {
        return "${"+variableName+"}";
    }

    @Override
    public List<Variable> getVariables() {
        Variable variable=new Variable();
        variable.setName(variableName);
        variable.setType("String");
        return Arrays.asList(variable);
    }

    @Override
    public Template fill(HashMap<String, Object> data,boolean lazy) {
        if(data.containsKey(variableName)){
            return new FilledSingleVariableTextTemplate(data.get(variableName));
        }
        return this;
    }
}
