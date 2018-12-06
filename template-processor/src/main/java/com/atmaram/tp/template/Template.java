package com.atmaram.tp.template;

import com.atmaram.tp.util.TemplateParsingUtil;

import java.util.HashMap;
import java.util.List;

public interface Template {
    public static boolean isVariable(String strValue){
        return ((strValue.startsWith("${") && strValue.endsWith("}")) || (strValue.startsWith("\"${") && strValue.endsWith("}\"")));
    }
    public static String getVariableName(String strValue){
        if(strValue.startsWith("\"${") ||strValue.startsWith("\"#{")) {
            return strValue.substring(3, strValue.length() - 2);
        }else {
            return strValue.substring(2, strValue.length() - 1);
        }
    }
    public static boolean isTemplateVariable(String strValue){
        return ((strValue.startsWith("#{") && strValue.endsWith("}"))||(strValue.startsWith("\"#{") && strValue.endsWith("}\"")));
    }
    public String toStringTemplate();
    default public String toValue(){
        return TemplateParsingUtil.replaceVariablesWithNulls(toStringTemplate());
    }
    public List<Variable> getVariables();
    public Template fill(HashMap<String, Object> data,boolean lazy);
    public default Template fill(HashMap<String, Object> data){
        return fill(data,false);
    }
}
