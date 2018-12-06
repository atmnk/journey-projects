package com.atmaram.tp.template.extractable.xml;

import com.atmaram.tp.template.Template;
import com.atmaram.tp.template.Variable;
import com.atmaram.tp.common.ExpressionProcessor;
import com.atmaram.tp.common.ExpressionTree;
import org.w3c.dom.Node;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class VariableTemplate implements XMLTemplate {
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
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public XMLTemplate fill(HashMap<String, Object> data,boolean lazy) {
        Object putValue=ExpressionProcessor.process(variableName,data,lazy);
        if(putValue instanceof String && Template.isVariable((String)putValue)){
            return new VariableTemplate(Template.getVariableName((String)putValue));
        } else if(putValue instanceof Node){
            return XMLTemplate.from(putValue);
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
    public Object toXMLCompatibleObject() {
        String xmlTemplate="${"+variableName+"}";
        return xmlTemplate; }

    @Override
    public String toStringTemplate() {
        return (String)toXMLCompatibleObject();
    }
}
