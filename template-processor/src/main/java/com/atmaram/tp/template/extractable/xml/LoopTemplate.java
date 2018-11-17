package com.atmaram.tp.template.extractable.xml;

import com.atmaram.tp.template.Variable;
import com.atmaram.tp.template.extractable.xml.helpers.NodeFormer;

import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoopTemplate implements XMLTemplate {
    String variableName;
    XMLTemplate pattern;
    public static String LOOP_TAG="XMLLoop";

    public LoopTemplate(String variableName, XMLTemplate pattern) {
        this.variableName = variableName;
        this.pattern = pattern;
    }

    @Override
    public List<Variable> getVariables() {
        List<Variable> returnValue=new ArrayList<>();
        Variable variable=new Variable();
        variable.setName(variableName);
        variable.setType("List");
        List<Variable> inner_variables=pattern.getVariables();
        Variable this_variable=new Variable();
        this_variable.setName("_this");
        this_variable.setType("String");
        List<Variable> inner_variables_excluding_this=new ArrayList<>();
        boolean found_this_variable=false;
        for (Variable inner_variable:inner_variables
        ) {
            if(inner_variable.getName().equals("_this")){
                found_this_variable=true;
            } else {
                inner_variables_excluding_this.add(inner_variable);
            }
        }
        if(found_this_variable){
            returnValue.add(variable);
            returnValue.addAll(inner_variables_excluding_this);
        } else {
            variable.setInner_variables(inner_variables_excluding_this);
            returnValue.add(variable);
        }

        return returnValue;
    }

    @Override
    public List<Variable> getTemplateVariables() {
        List<Variable> inner_variables=pattern.getTemplateVariables();
        return inner_variables;
    }

    @Override
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        return new LoopTemplate(variableName,(XMLTemplate) pattern.fillTemplateVariables(data));
    }

    @Override
    public XMLTemplate fill(HashMap<String, Object> data) {
        if(data.containsKey(variableName)){
            NodeStaticListTemplate staticArrayTemplate=new NodeStaticListTemplate();
            HashMap<String,Object> localVariables;
            if(data.get(variableName) instanceof List){
                List<Object> filling_list=(List<Object>)data.get(variableName);
                for (Object list_object:
                        filling_list) {
                    if(list_object instanceof HashMap) {
                        localVariables = (HashMap<String, Object>) list_object;
                    }else{
                        localVariables=new HashMap<>();
                        localVariables.put("_this",list_object);
                    }
                    staticArrayTemplate.add((XMLTemplate)pattern.fill(localVariables).fill(data));
                }
                return staticArrayTemplate;
            } else {
                return new LoopTemplate(variableName,(XMLTemplate)pattern.fill(data));
            }
        } else {
            return new LoopTemplate(variableName,(XMLTemplate)pattern.fill(data));
        }
    }

    @Override
    public HashMap<String, Object> extract(Object from) {
        HashMap<String, Object> retData = new HashMap<>();
        List lst = new ArrayList();
        if(pattern instanceof NodeTemplate) {
            NodeTemplate nodePattern=(NodeTemplate)pattern;
            NodeList resultList = ((Element)from).getElementsByTagName(nodePattern.Tag);
            for (int i = 0; i < resultList.getLength(); i++) {
                Object oValue = resultList.item(i);
                HashMap<String, Object> memberExtractedData = pattern.extract(oValue);
                if (memberExtractedData.containsKey("_this") && memberExtractedData.keySet().size() == 1) {
                    lst.add(memberExtractedData.get("_this"));
                } else {
                    lst.add(memberExtractedData);
                }
            }
            retData.put(variableName, lst);
        }
        return retData;
    }

    @Override
    public Object toXMLCompatibleObject() {
        Document document= NodeFormer.freshDocument();
        Element node = NodeFormer.createNodeForTagInDocument(document,LOOP_TAG);
        node.setAttribute("variable",variableName);
        Object data=pattern.toXMLCompatibleObject();
        NodeTemplate.processChildNode(document, node, data);
        return node;
    }

    @Override
    public String toStringTemplate() {
        Node node=(Node)toXMLCompatibleObject();
        return NodeFormer.nodeToString(node);
    }
}
