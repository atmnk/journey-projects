package com.atmaram.mocker.server;

import javax.servlet.http.HttpServletRequest;

public class Mapping {
    RequestTemplate requestTemplate;
    ResponseTemplate responseTemplate;

    public Mapping(RequestTemplate requestTemplate, ResponseTemplate responseTemplate) {
        this.requestTemplate = requestTemplate;
        this.responseTemplate = responseTemplate;
    }

}
