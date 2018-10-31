package com.atmaram.tp.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemplateParsingUtil {
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
