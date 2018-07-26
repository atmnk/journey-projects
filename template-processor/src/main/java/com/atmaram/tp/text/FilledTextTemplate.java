package com.atmaram.tp.text;

import com.atmaram.tp.Variable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
class FilledTextTemplate implements TextTemplate {
    String value;

    public FilledTextTemplate(String value) {
        this.value = value;
    }

    @Override
    public TextTemplate fill(HashMap<String, Object> data) {
        return this;
    }

    @Override
    public List<Variable> getVariables() {
        return Arrays.asList();
    }

    @Override
    public String toValue() {
        return value;
    }

    public void merge(FilledTextTemplate toAdd){
        this.value=this.value+toAdd.value;
    }
}
