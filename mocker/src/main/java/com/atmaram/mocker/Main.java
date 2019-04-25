package com.atmaram.mocker;

import com.atmaram.mocker.server.Http;
import com.atmaram.mocker.server.HttpServerContext;

public class Main {
    public static void main(String[] args){
        Http http=new Http(new HttpServerContext(8080));
    }
}
