package com.atmaram.mocker.body.json.template;

public class TemplateBuilder {
    Template template;

    public TemplateBuilder(Template template) {
        this.template = template;
    }
    public TemplateBuilder save(String variable){
        this.template.variable=variable;
        return this;
    }
    public Template toTemplate(){
        return template;
    }
}
