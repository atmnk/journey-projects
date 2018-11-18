package com.atmaram.jp.cli;

import com.atmaram.jp.model.RequestHeader;
import com.atmaram.jp.model.ResponseHeader;
import com.atmaram.tp.template.TemplateType;
import lombok.Data;

import java.util.List;

@Data
public class Request {
    String url;
    TemplateType bodyType;
    TemplateType responseType;
    String body;
    String response;
    List<RequestHeader> requestHeaders;
    List<ResponseHeader> responseHeaders;
    int wait=0;

//    public Request() {
//    }

//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getBody() {
//        return body;
//    }
//
//    public void setBody(String body) {
//        this.body = body;
//    }
//
//    public String getResponse() {
//        return response;
//    }
//
//    public void setResponse(String response) {
//        this.response = response;
//    }
//
//    public List<RequestHeader> getRequestHeaders() {
//        return requestHeaders;
//    }
//
//    public void setRequestHeaders(List<RequestHeader> requestHeaders) {
//        this.requestHeaders = requestHeaders;
//    }
//
//    public List<ResponseHeader> getResponseHeaders() {
//        return responseHeaders;
//    }
//
//    public void setResponseHeaders(List<ResponseHeader> responseHeaders) {
//        this.responseHeaders = responseHeaders;
//    }
//
//    public int getWait() {
//        return wait;
//    }
//
//    public void setWait(int wait) {
//        this.wait = wait;
//    }
}
