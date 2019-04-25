package com.atmaram.mocker.server;

public class StringTemplate {
    public String content;

    public StringTemplate(String content) {
        this.content = content;
    }
    public boolean match(String target){
        return target.equals(content);
    }
}
