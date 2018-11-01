package com.atmaram.tp.xml;

import com.atmaram.tp.ExtractableTemplate;
import com.atmaram.tp.Template;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.util.XMLTemplateParsingUtil;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface XMLTemplate extends ExtractableTemplate {
    public Object toXMLCompatibleObject();
    public static XMLTemplate from(Object xmlTemplate) {
        if(xmlTemplate instanceof Document){
            Document xmlTemplateDocument=(Document)xmlTemplate;
            Node innerNodes=xmlTemplateDocument.getFirstChild();
            return from(innerNodes);
        } else if(xmlTemplate instanceof Element){
            Element xmlTemplateElement=(Element)xmlTemplate;
            if(xmlTemplateElement.getTagName().equals(LoopTemplate.LOOP_TAG)){
                Node inner_object=xmlTemplateElement.getChildNodes().item(0);
                XMLTemplate inner_template=from(inner_object);
                return new LoopTemplate(xmlTemplateElement.getAttribute("variable"),inner_template);
            } else {
                NodeList innerNodes=xmlTemplateElement.getChildNodes();
                NodeTemplate nodeTemplate=new NodeTemplate(xmlTemplateElement.getTagName());
                NamedNodeMap attributes=xmlTemplateElement.getAttributes();
                HashMap<XMLTemplate,XMLTemplate> tAtrributes=new HashMap<>();
                List<XMLTemplate> childTemplates=new ArrayList<>();
                if(attributes!=null)
                for(int i=0;i<attributes.getLength();i++){
                    String name=attributes.item(i).getNodeName();
                    String value=attributes.item(i).getNodeValue();
                    tAtrributes.put(XMLTemplate.from(name),XMLTemplate.from(value));
                }
                if(innerNodes!=null)
                for(int i=0;i<innerNodes.getLength();i++){
                    childTemplates.add(XMLTemplate.from(innerNodes.item(i)));
                }
                nodeTemplate.setAttributes(tAtrributes);
                nodeTemplate.setChildNodes(childTemplates);
                return nodeTemplate;
            }
        } else if(xmlTemplate instanceof Text){
            String inside=((Text)xmlTemplate).getWholeText();
            if(Template.isVariable(inside)){
                return new VariableTemplate(Template.getVariableName(inside));
            } else if(Template.isTemplateVariable(inside)){
                return new TemplateVariableTemplate(Template.getVariableName(inside));
            } else {
                return new FilledVariableTemplate(inside);
            }
        } else if(xmlTemplate instanceof String){
            if(Template.isVariable((String)xmlTemplate)){
                return new VariableTemplate(Template.getVariableName((String)xmlTemplate));
            } else if(Template.isTemplateVariable((String)xmlTemplate)){
                return new TemplateVariableTemplate(Template.getVariableName((String)xmlTemplate));
            } else {
                return new FilledVariableTemplate(xmlTemplate);
            }
        } else {
            return new FilledVariableTemplate(xmlTemplate.toString());
        }
    }
    public static XMLTemplate parse(String template) throws TemplateParseException {
        if(Template.isVariable(template)){
            return new VariableTemplate(Template.getVariableName(template));
        }
        if(Template.isTemplateVariable(template)){
            return new TemplateVariableTemplate(Template.getVariableName(template));
        }
        template= XMLTemplateParsingUtil.replaceVariablesWithQuotedVariables(template);
        template= XMLTemplateParsingUtil.replaceLoopsWithTransformedXML(template);
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            InputSource is=new InputSource();
            is.setCharacterStream(new StringReader(template));
            Document doc=documentBuilder.parse(is);
            return XMLTemplate.from(doc);
        } catch (ParserConfigurationException ex){
            throw new TemplateParseException("Provided template is not valid xml: "+template);
        }catch (SAXException e) {
            throw new TemplateParseException("Provided template is not valid xml: "+template);
        } catch (IOException e) {
            throw new TemplateParseException("Provided template is not valid xml: "+template);
        }
    }
}
