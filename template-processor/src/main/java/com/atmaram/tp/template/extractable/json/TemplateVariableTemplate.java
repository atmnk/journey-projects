package com.atmaram.tp.template.extractable.json;

import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.ExpressionProcessor;
import org.json.simple.JSONAware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TemplateVariableTemplate implements JSONTemplate{
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
        return ExpressionProcessor.getVariables(variableName);
    }

    @Override
    public JSONTemplate fillTemplateVariables(HashMap<String, Object> data) {
        Object putValue=ExpressionProcessor.process(variableName,data);
        if(putValue instanceof String && Template.isVariable((String)putValue)){
            return this;
        } else if(putValue instanceof JSONAware){
            return JSONTemplate.from(putValue);
        } else {
            return new FilledVariableTemplate(putValue);
        }
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
        String jsonTemplate="#{"+variableName+"}";
        return jsonTemplate;
    }

    @Override
    public String toStringTemplate() {
        return (String)toJSONCompatibleObject();
    }
}
