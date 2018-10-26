package com.atmaram.tp.util;

import com.atmaram.tp.common.exceptions.TemplateParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONTemplateParsingUtil {
    public static String replaceLoopsWithTransformedJSON(String template) throws TemplateParseException{
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
            String innerJSON=replaceLoopsWithTransformedJSON(inner);
            JSONParser parser=new JSONParser();
            Object inner_object= null;
            try {
                inner_object = parser.parse(innerJSON);
            } catch (ParseException e) {
                throw new TemplateParseException("Inner template provided is not valid json: "+inner);
            }
            JSONObject newJSON=new JSONObject();
            newJSON.put("variable",variable);
            newJSON.put("template",inner_object);
            String newString=template.substring(0,template.indexOf(openingTag))+newJSON.toJSONString()+(template.length()==i?"":template.substring(i,template.length()));
            return replaceLoopsWithTransformedJSON(newString);
        }
        return template;
    }
    public static String replaceVariablesWithQuotedVariables(String template){
        String output=""+replaceQuotedVariablesWithUnQuotedVariables(template);
        final Pattern pattern = Pattern.compile("\\$\\{(.+?)}");
        final Matcher matcher = pattern.matcher(template);
        List<String> variables = new ArrayList<>();
        while (matcher.find()) {
            String variable=matcher.group(1);
            if(!variables.contains(variable))
                variables.add(variable);
        }
        for (String variable :
                variables) {
            output = output.replace("${" + variable + "}", "\"${" + variable + "}\"");
        }
        final Pattern pattern1 = Pattern.compile("#\\{(.+?)}");
        final Matcher matcher1 = pattern1.matcher(template);
        List<String> variables1 = new ArrayList<>();
        while (matcher1.find()) {
            String variable1=matcher1.group(1);
            if(!variables1.contains(variable1))
                variables1.add(variable1);
        }
        for (String variable1 :
                variables1) {
            output = output.replace("#{" + variable1 + "}", "\"#{" + variable1 + "}\"");
        }
        return output;
    }

    public static String replaceQuotedVariablesWithUnQuotedVariables(String template){
        String output=""+template;
        final Pattern pattern = Pattern.compile("\"\\$\\{(.+?)}\"");
        final Matcher matcher = pattern.matcher(template);
        List<String> variables = new ArrayList<>();
        while (matcher.find()) {
            String variable=matcher.group(1);
            if(!variables.contains(variable))
                variables.add(variable);
        }
        for (String variable :
                variables) {
            output = output.replace("\"${" + variable + "}\"", "${" + variable + "}");
        }
        final Pattern pattern1 = Pattern.compile("\"#\\{(.+?)}\"");
        final Matcher matcher1 = pattern1.matcher(template);
        List<String> variables1 = new ArrayList<>();
        while (matcher1.find()) {
            String variable1=matcher1.group(1);
            if(!variables1.contains(variable1))
                variables1.add(variable1);
        }
        for (String variable1 :
                variables1) {
            output = output.replace("\"#{" + variable1 + "}\"", "#{" + variable1 + "}");
        }
        return output;
    }
}
