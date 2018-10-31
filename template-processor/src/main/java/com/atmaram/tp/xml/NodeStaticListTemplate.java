package com.atmaram.tp.xml;

import com.atmaram.tp.Variable;
import com.atmaram.tp.xml.helpers.NodeFormer;
import org.json.simple.JSONArray;
import org.w3c.dom.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeStaticListTemplate implements XMLTemplate{
    List<XMLTemplate> memberTemplates=new ArrayList<>();

    public NodeStaticListTemplate() {
    }

    public void add(XMLTemplate memberTemplate){
        memberTemplates.add(memberTemplate);
    }
    @Override
    public List<Variable> getVariables() {
        List<Variable> variables=new ArrayList<>();
        for (XMLTemplate memberTemplate:
                memberTemplates) {
            variables.addAll(memberTemplate.getVariables());
        }
        return variables;
    }

    @Override
    public List<Variable> getTemplateVariables() {
        List<Variable> variables=new ArrayList<>();
        for (XMLTemplate memberTemplate:
                memberTemplates) {
            variables.addAll(memberTemplate.getTemplateVariables());
        }
        return variables;
    }

    @Override
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        NodeStaticListTemplate staticArrayTemplate=new NodeStaticListTemplate();
        for (XMLTemplate memberTemplate:
                memberTemplates) {
            staticArrayTemplate.add((XMLTemplate)memberTemplate.fillTemplateVariables(data));
        }
        return staticArrayTemplate;
    }

    @Override
    public XMLTemplate fill(HashMap<String, Object> data) {
        NodeStaticListTemplate staticArrayTemplate=new NodeStaticListTemplate();
        for (XMLTemplate memberTemplate:
                memberTemplates) {
            staticArrayTemplate.add((XMLTemplate)memberTemplate.fill(data));
        }
        return staticArrayTemplate;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        HashMap<String,Object> retData=new HashMap<>();
        JSONArray jsonResult=(JSONArray)from;
        for(int i=0;i<memberTemplates.size();i++){
            XMLTemplate oValue=memberTemplates.get(i);
            retData.putAll(oValue.extract(jsonResult.get(i)));
        }
        return retData;
    }

    @Override
    public Object toXMLCompatibleObject() {
        Document document= NodeFormer.freshDocument();
        Element node = NodeFormer.createNodeForTagInDocument(document,"XMLStatic");
        for (XMLTemplate childNode:
                memberTemplates) {
            Object data=childNode.toXMLCompatibleObject();
            if(data instanceof String){
                Text textNode=document.createTextNode((String)data);
                node.appendChild(textNode);
            } else if(data instanceof Element){
                Element dataE=(Element)data;
                if(dataE.getTagName().equals("XMLStatic")){
                    NodeList staticChildNodes=dataE.getChildNodes();
                    for(int i=0;i<staticChildNodes.getLength();i++){
                        Node newNode=document.importNode(staticChildNodes.item(i),true);
                        node.appendChild(newNode);
                    }
                }
                Node newNode=document.importNode((Node)data,true);
                node.appendChild(newNode);
            }
        }
        return node;
    }
    @Override
    public String toStringTemplate() {
        throw new NotImplementedException();
    }
}
