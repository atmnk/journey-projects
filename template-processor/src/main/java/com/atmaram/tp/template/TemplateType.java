package com.atmaram.tp.template;

public enum TemplateType {
    Text,
    Json,
    XML,
    SingleVariableText,
    Extractable;

    public static TemplateType fromString(String type) {
        if (type.trim().toLowerCase().equals("text")) {
            return TemplateType.Text;
        } else if (type.trim().toLowerCase().equals("json")) {
            return TemplateType.Json;
        } else if (type.trim().toLowerCase().equals("xml")) {
            return TemplateType.XML;
        } else if (type.trim().toLowerCase().equals("singletext")) {
            return TemplateType.SingleVariableText;
        } else {
            return TemplateType.Extractable;
        }
    }
}
