package com.atmaram.jp.model;

import lombok.Data;

@Data
public class EnvironmentVariable {
    String name;
    String valueTemplate;

    public EnvironmentVariable(String name, String valueTemplate) {
        this.name = name;
        this.valueTemplate = valueTemplate;
    }
}
