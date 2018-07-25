package com.atmaram.tp.text;

import com.atmaram.tp.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ArrayTextTemplate implements TextTemplate{
    List<TextTemplate> blocks;

    public ArrayTextTemplate() {
        blocks=new ArrayList<>();
    }
    public void add(TextTemplate block){
        blocks.add(block);
    }
    @Override
    public TextTemplate fill(HashMap<String, Object> data) {
        ArrayTextTemplate ret=new ArrayTextTemplate();
        for (TextTemplate block:
             blocks) {
            ret.add(block.fill(data));
        }
        return ret;
    }

    @Override
    public List<Variable> getVariables() {
        List<Variable> variableList=new ArrayList<>();
        for (TextTemplate block:
                blocks) {
            variableList.addAll(block.getVariables());
        }
        return variableList;
    }

    @Override
    public String toValue() {
        String ret="";
        for (TextTemplate block:
             blocks) {
            ret+=block.toValue();
        }
        return ret;
    }
}
