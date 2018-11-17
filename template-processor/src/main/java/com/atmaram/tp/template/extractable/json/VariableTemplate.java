package com.atmaram.tp.template.extractable.json;

import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.ExpressionProcessor;
import com.atmaram.tp.common.ExpressionTree;
import org.json.simple.JSONAware;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class VariableTemplate implements JSONTemplate {
    String variableName;

    public VariableTemplate(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public List<Variable> getVariables() {
        return ExpressionProcessor.getVariables(variableName);
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
    public HashMap<String,Object> extract(Object from) {
        HashMap<String,Object> ret=new HashMap<>();
        ExpressionTree tree=ExpressionProcessor.toTree(variableName);
        if(tree.getVariable()!=null){
            ret.put(variableName,from);
        }
        return ret;
    }

    @Override
    public Object toJSONCompatibleObject() {
        String jsonTemplate="${"+variableName+"}";
        return jsonTemplate;
    }

    @Override
    public String toStringTemplate() {
        return (String) toJSONCompatibleObject();
    }
}
