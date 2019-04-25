package com.atmaram.mocker.server;

import com.atmaram.mocker.body.AbstractBuilder;

public class RequestTemplateBuilder {
    RequestTemplate requestTemplate;
    public RequestTemplateBuilder withUrl(String url){
        requestTemplate.urlTemplate=new StringTemplate(url);
        return this;
    }
    public RequestTemplateBuilder withBody(AbstractBuilder body){
        requestTemplate.bodyTemplate=body;
        requestTemplate.contentType=new StringTemplate("application/json");
        return this;
    }

    public RequestTemplateBuilder(String type) {
        this.requestTemplate=new RequestTemplate();
        this.requestTemplate.methodTemplate=new StringTemplate(type);
    }

    public static RequestTemplateBuilder get(String url){
        RequestTemplateBuilder requestTemplateBuilder=new RequestTemplateBuilder("GET");
        return requestTemplateBuilder.withUrl(url);
    }
    public static RequestTemplateBuilder post(String url){
        RequestTemplateBuilder requestTemplateBuilder=new RequestTemplateBuilder("POST");
        return requestTemplateBuilder.withUrl(url);
    }
    public RequestTemplate toRequestTemplate(){
        return requestTemplate;
    }
}
