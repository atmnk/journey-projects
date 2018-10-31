package com.atmaram.tp.xml;

import com.atmaram.tp.Variable;
import com.atmaram.tp.xml.helpers.NodeFormer;
import org.w3c.dom.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
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
        throw new NotImplementedException();
    }

    @Override
    public List<Variable> getTemplateVariables() {
        throw new NotImplementedException();
    }

    @Override
    public XMLTemplate fillTemplateVariables(HashMap<String, Object> data) {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    @Override
    public Object toXMLCompatibleObject() {
        Document document=NodeFormer.freshDocument();
        Element node = NodeFormer.createNodeForTagInDocument(document,Tag);
        for (XMLTemplate key:
             attributes.keySet()) {
            node.setAttribute((String)key.toXMLCompatibleObject(),(String)attributes.get(key).toXMLCompatibleObject());
        }
        for (XMLTemplate childNode:
             childNodes) {
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
                } else {
                    Node newNode = document.importNode((Node) data, true);
                    node.appendChild(newNode);
                }
            }
        }
        return node;
    }
    @Override
    public String toStringTemplate() {
        Node node=(Node)toXMLCompatibleObject();
        Document document=NodeFormer.freshDocument();
        Node newNode=document.importNode(node,true);
        document.appendChild(newNode);
        try {
            StringWriter writer=new StringWriter();
            Transformer transformer= TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(new DOMSource(document), new StreamResult(writer));
            return writer.toString();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return null;
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
