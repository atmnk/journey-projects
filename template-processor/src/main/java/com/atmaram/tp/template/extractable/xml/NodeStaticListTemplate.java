package com.atmaram.tp.template.extractable.xml;

import com.atmaram.tp.template.Variable;
import com.atmaram.tp.template.extractable.xml.helpers.NodeFormer;
import org.w3c.dom.*;

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
    public XMLTemplate fill(HashMap<String, Object> data,boolean lazy) {
        NodeStaticListTemplate staticArrayTemplate=new NodeStaticListTemplate();
        for (XMLTemplate memberTemplate:
                memberTemplates) {
            staticArrayTemplate.add((XMLTemplate)memberTemplate.fill(data,lazy));
        }
        return staticArrayTemplate;
    }

    @Override
    public HashMap<String,Object> extract(Object from) {
        HashMap<String, Object> retData = new HashMap<>();
        Element parentFrom=(Element)from;
        for (XMLTemplate memmber:
             memberTemplates) {
            if(memmber instanceof NodeTemplate){
                NodeTemplate nodeMember=(NodeTemplate)memmber;
                NodeList matching=parentFrom.getElementsByTagName(nodeMember.Tag);
                for(int i=0;i<matching.getLength();i++) {
                    retData.putAll(nodeMember.extract(matching.item(i)));
                }
            } else if(memmber instanceof LoopTemplate){
                LoopTemplate loopMember=(LoopTemplate)memmber;
                retData.putAll(loopMember.extract(parentFrom));
            } else if(memmber instanceof NodeStaticListTemplate){
                retData.putAll(memmber.extract(parentFrom));
            }
        }
        return retData;
    }

    @Override
    public Object toXMLCompatibleObject() {
        Document document= NodeFormer.freshDocument();
        Element node = NodeFormer.createNodeForTagInDocument(document,"XMLStatic");
        NodeTemplate.processChildNodes(document, node, memberTemplates);
        return node;
    }



    @Override
    public String toStringTemplate() {
        Node node=(Node)toXMLCompatibleObject();
        return NodeFormer.nodeToString(node);
    }
}
