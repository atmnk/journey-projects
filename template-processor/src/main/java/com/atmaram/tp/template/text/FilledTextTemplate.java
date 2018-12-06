package com.atmaram.tp.template.text;

import com.atmaram.tp.template.Variable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
class FilledTextTemplate implements TextTemplate {
    String value;

    public FilledTextTemplate(String value) {
        this.value = value;
    }

    @Override
    public TextTemplate fill(HashMap<String, Object> data,boolean lazy) {
        return this;
    }

    @Override
    public String toStringTemplate() {
        return value;
    }

    @Override
    public List<Variable> getVariables() {
        return Arrays.asList();
    }

    public void merge(FilledTextTemplate toAdd){
        this.value=this.value+toAdd.value;
    }
}
