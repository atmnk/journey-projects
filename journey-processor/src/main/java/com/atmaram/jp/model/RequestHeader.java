package com.atmaram.jp.model;

import lombok.Data;

@Data
public class RequestHeader {
    String name;
    String valueTemplate;
    @Override
    public String toString(){
        return name+":"+valueTemplate;
    }
}
