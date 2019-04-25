package com.atmaram.mocker.server;

import com.atmaram.mocker.matching.UrlParser;
import com.atmaram.mocker.body.AbstractBuilder;
import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RequestTemplate {
    StringTemplate urlTemplate;
    AbstractBuilder bodyTemplate=null;
    StringTemplate methodTemplate;
    StringTemplate contentType;

    public RequestTemplate() {
    }

    public RequestTemplate(StringTemplate urlTemplate, StringTemplate methodTemplate) {
        this.urlTemplate = urlTemplate;
        this.methodTemplate=methodTemplate;
    }

    public RequestTemplate(StringTemplate urlTemplate,StringTemplate methodTemplate, AbstractBuilder bodyTemplate) {
        this(urlTemplate,methodTemplate);
        this.bodyTemplate = bodyTemplate;
    }
    public List<Tag> grepRequest(HttpServletRequest servletRequest,DataPool dataPool) throws Exception{
        boolean matched=true;
        List<Tag> finalTags=new ArrayList<>();
        String strQueryString="";
        try{
            strQueryString=servletRequest.getQueryString();
        } catch (Exception e){

        }
        if(strQueryString==null){
            strQueryString="";
        }
        String urq=servletRequest.getRequestURI()+(strQueryString.equals("")?"":"?"+strQueryString);
        finalTags.addAll(UrlParser.extract(dataPool,this.urlTemplate.content,urq));
        //matched=matched && urlTemplate.match(servletRequest.getRequestURI());
        matched=matched && methodTemplate.match(servletRequest.getMethod());
        if(bodyTemplate!=null){
//            finalTags.addAll(UrlParser.extract(this.urlTemplate.content,servletRequest.getRequestURI()));
            matched=matched && contentType.match(servletRequest.getContentType());
            if(matched){
                String body = new BufferedReader(new InputStreamReader(servletRequest.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));
                bodyTemplate.gerpBody(dataPool,finalTags,body);// servletRequest.getInputStream())
            }
        }
        if(!matched)
            throw new Exception("Not Matching");
        return finalTags;
    }
}
