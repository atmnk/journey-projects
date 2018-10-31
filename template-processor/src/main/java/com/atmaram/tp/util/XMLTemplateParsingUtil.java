package com.atmaram.tp.util;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import com.atmaram.tp.xml.LoopTemplate;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XMLTemplateParsingUtil extends TemplateParsingUtil {
    public static String replaceLoopsWithTransformedXML(String template) throws TemplateParseException{
        String output=""+template;
        final Pattern pattern = Pattern.compile("\\{\\{#(.+?)\\}\\}");
        final Matcher matcher = pattern.matcher(template);
        if(matcher.find()){
            String variable=matcher.group(1);
            String openingTag="{{#"+variable+"}}";
            String closingTag="{{/"+variable+"}}";
            int count=1;
            int i=template.indexOf(openingTag)+1;
            while(count!=0) {
                if(i>=template.length()){
                    throw new TemplateParseException("No closing tag found for Tag:"+openingTag);
                }
                if (template.length()>i+openingTag.length() && template.substring(i, i + openingTag.length()).equals(openingTag)) {
                    i=i+openingTag.length();
                    count++;
                    continue;
                } else if (template.length()>i+openingTag.length() && template.substring(i, i + closingTag.length()).equals(closingTag)) {
                    i=i+closingTag.length();
                    count--;
                    continue;
                }
                i++;
            }
            String inner=template.substring(template.indexOf(openingTag)+openingTag.length(),i-closingTag.length());
            String innerXML=replaceLoopsWithTransformedXML(inner);
            String openingLoopTag="<"+ LoopTemplate.LOOP_TAG+" variable=\""+variable+"\">";
            String closingLoopTag="</"+ LoopTemplate.LOOP_TAG+">";
            String newString=template.substring(0,template.indexOf(openingTag))+openingLoopTag+innerXML+closingLoopTag+(template.length()==i?"":template.substring(i));
            return replaceLoopsWithTransformedXML(newString);
        }
        return template;
    }
}
