package com.atmaram.tp.xml;

import com.atmaram.tp.Template;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.ExpressionProcessor;
import org.w3c.dom.Node;

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
        return ExpressionProcessor.getVariables(variableName);
    }

    @Override
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        Object putValue=ExpressionProcessor.process(variableName,data);
        if(putValue instanceof String && Template.isVariable((String)putValue)){
            return new TemplateVariableTemplate(Template.getVariableName((String)putValue));
        } else if(putValue instanceof Node){
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
