package com.atmaram.tp.xml.helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;

public class NodeFormer {
    public static Document freshDocument(){
        DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder= null;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            return document;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static Element createNodeForTagInDocument(Document document,String TagName){
        Element element=document.createElement(TagName);
        return element;
    }
}
