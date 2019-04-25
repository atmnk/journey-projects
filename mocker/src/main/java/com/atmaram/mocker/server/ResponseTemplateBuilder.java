package com.atmaram.mocker.server;

import com.atmaram.mocker.body.json.JsonBuilder;

public class ResponseTemplateBuilder {
    ResponseTemplate responseTemplate;

    public ResponseTemplateBuilder() {
        responseTemplate=new ResponseTemplate();
    }
    public static ResponseTemplateBuilder okJson(JsonBuilder body){
        ResponseTemplateBuilder responseTemplateBuilder=new ResponseTemplateBuilder();
        responseTemplateBuilder.responseTemplate.contentType=new StringTemplate("application/json");
        responseTemplateBuilder.responseTemplate.body=body;
        responseTemplateBuilder.responseTemplate.status=200;
        return responseTemplateBuilder;
    }
//    public static ResponseTemplateBuilder okJson(String body){
//        ResponseTemplateBuilder responseTemplateBuilder=new ResponseTemplateBuilder();
//        responseTemplateBuilder.responseTemplate.contentType=new StringTemplate("application/json");
//        responseTemplateBuilder.responseTemplate.body=new StringTemplate(body);
//        responseTemplateBuilder.responseTemplate.status=200;
//        return responseTemplateBuilder;
//    }
//    public static ResponseTemplateBuilder okXml(String body){
//        ResponseTemplateBuilder responseTemplateBuilder=new ResponseTemplateBuilder();
//        responseTemplateBuilder.responseTemplate.contentType=new StringTemplate("application/xml");
//        responseTemplateBuilder.responseTemplate.body=new StringTemplate(body);
//        responseTemplateBuilder.responseTemplate.status=200;
//        return responseTemplateBuilder;
//    }
//    public static ResponseTemplateBuilder okHtml(String body){
//        ResponseTemplateBuilder responseTemplateBuilder=new ResponseTemplateBuilder();
//        responseTemplateBuilder.responseTemplate.contentType=new StringTemplate("text/html");
//        responseTemplateBuilder.responseTemplate.body=new StringTemplate(body);
//        responseTemplateBuilder.responseTemplate.status=200;
//        return responseTemplateBuilder;
//    }
    public ResponseTemplate toResponseTemplate(){
        return responseTemplate;
    }
}
