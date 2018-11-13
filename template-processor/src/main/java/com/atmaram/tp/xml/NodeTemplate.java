package com.atmaram.tp.xml;

import com.atmaram.tp.Variable;
import com.atmaram.tp.xml.helpers.NodeFormer;
import org.w3c.dom.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NodeTemplate implements XMLTemplate {
    String Tag;
    HashMap<XMLTemplate,XMLTemplate> attributes;
    List<XMLTemplate> childNodes;

    public NodeTemplate(String tag) {
        Tag = tag;
    }

    public HashMap<XMLTemplate, XMLTemplate> getAttributes() {
        return attributes;
    }

    public void setAttributes(HashMap<XMLTemplate, XMLTemplate> attributes) {
        this.attributes = attributes;
    }

    public List<XMLTemplate> getChildNodes() {
        return childNodes;
    }

    public void setChildNodes(List<XMLTemplate> childNodes) {
        this.childNodes = childNodes;
    }

    @Override
    public List<Variable> getVariables() {
        List<Variable> variables=new ArrayList<>();
        for (XMLTemplate attributeKey:
             attributes.keySet()) {
            variables.addAll(attributeKey.getVariables());
            variables.addAll(attributes.get(attributeKey).getVariables());
        }
        for (XMLTemplate childNode:childNodes){
            variables.addAll(childNode.getVariables());
        }
        return variables;
    }

    @Override
    public List<Variable> getTemplateVariables() {
        List<Variable> variables=new ArrayList<>();
        for (XMLTemplate attributeKey:
                attributes.keySet()) {
            variables.addAll(attributeKey.getTemplateVariables());
            variables.addAll(attributes.get(attributeKey).getTemplateVariables());
        }
        for (XMLTemplate childNode:childNodes){
            variables.addAll(childNode.getTemplateVariables());
        }
        return variables;
    }

    @Override
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        NodeTemplate resultantNodeTemplate=new NodeTemplate(this.Tag);
        HashMap<XMLTemplate,XMLTemplate> rAttr=new HashMap<>();
        List<XMLTemplate> rChilds=new ArrayList<>();
        for (XMLTemplate attributeKey:
                attributes.keySet()) {
            rAttr.put((XMLTemplate) attributeKey.fillTemplateVariables(data),(XMLTemplate)attributes.get(attributeKey).fillTemplateVariables(data));
        }
        for (XMLTemplate childNode:childNodes){
            rChilds.add((XMLTemplate)childNode.fillTemplateVariables(data));
        }
        resultantNodeTemplate.childNodes=rChilds;
        resultantNodeTemplate.attributes=rAttr;
        return resultantNodeTemplate;
    }

    @Override
    public XMLTemplate fill(HashMap<String, Object> data) {
        NodeTemplate newNodeTemplate=new NodeTemplate(Tag);
        HashMap<XMLTemplate,XMLTemplate> newAttributes=new HashMap<>();
        for (XMLTemplate key:
             attributes.keySet()) {
            XMLTemplate newKey=(XMLTemplate) key.fill(data);
            XMLTemplate newValue=(XMLTemplate)attributes.get(key).fill(data);
            newAttributes.put(newKey,newValue);
        }
        List<XMLTemplate> newChildNodes=new ArrayList<>();
        for (XMLTemplate child:
             childNodes) {
            newChildNodes.add((XMLTemplate)child.fill(data));
        }
        newNodeTemplate.setChildNodes(newChildNodes);
        newNodeTemplate.setAttributes(newAttributes);
        return newNodeTemplate;
    }

    @Override
    public HashMap<String, Object> extract(Object from) {
        HashMap<String,Object> retData=new HashMap<>();
        if(from instanceof Element){
            Element elementFrom=(Element)from;
            if(elementFrom.getTagName().equals(this.Tag)){
                //Extract Attributes
                for (XMLTemplate attributeKey:
                     attributes.keySet()) {
                    if (attributeKey instanceof FilledVariableTemplate){
                          XMLTemplate attributeValueTemplate=attributes.get(attributeKey);
                          if(attributeValueTemplate instanceof VariableTemplate){
                              retData.putAll(attributeValueTemplate.extract(elementFrom.getAttribute(attributeKey.toStringTemplate())));
                          }
                    }
                }
                //Extract Text Children
                if(childNodes.size()==1 && childNodes.get(0) instanceof VariableTemplate){
                    retData.putAll(childNodes.get(0).extract(elementFrom.getTextContent()));
                    return retData;
                }

                //Extract all Children
                for (XMLTemplate childNode:
                     childNodes) {
                    if(childNode instanceof NodeTemplate){
                        NodeTemplate nChild=(NodeTemplate)childNode;
                        NodeList list=elementFrom.getElementsByTagName(nChild.Tag);
                        for(int i=0;i<list.getLength();i++){
                            retData.putAll(nChild.extract(list.item(i)));
                        }
                    } else if(childNode instanceof LoopTemplate){
                        LoopTemplate loopChild=(LoopTemplate) childNode;
                        retData.putAll(loopChild.extract(elementFrom));
                    } else if(childNode instanceof NodeStaticListTemplate){
                        NodeStaticListTemplate slChild=(NodeStaticListTemplate) childNode;
                        retData.putAll(slChild.extract(elementFrom));
                    }
                }

            }
            return retData;
        }
        return retData;
    }
    public static void processChildNodes(Document document, Element node, List<XMLTemplate> memberTemplates) {
        for (XMLTemplate childNode:
                memberTemplates) {
            Object data=childNode.toXMLCompatibleObject();
            processChildNode(document, node, data);
        }
    }

    public static void processChildNode(Document document, Element node, Object data) {
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
            } else {
                Node newNode = document.importNode((Node) data, true);
                node.appendChild(newNode);
            }
        }
    }

    @Override
    public Object toXMLCompatibleObject() {
        Document document=NodeFormer.freshDocument();
        Element node = NodeFormer.createNodeForTagInDocument(document,Tag);
        for (XMLTemplate key:
             attributes.keySet()) {
            node.setAttribute((String)key.toXMLCompatibleObject(),(String)attributes.get(key).toXMLCompatibleObject());
        }
        processChildNodes(document, node, childNodes);
        return node;
    }
    @Override
    public String toStringTemplate() {
        Node node=(Node)toXMLCompatibleObject();
        return NodeFormer.nodeToString(node);
    }
}
