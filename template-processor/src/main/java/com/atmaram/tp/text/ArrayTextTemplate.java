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
        if(blocks.size()>0 && blocks.get(blocks.size()-1) instanceof FilledTextTemplate && block instanceof FilledTextTemplate){
            ((FilledTextTemplate)blocks.get(blocks.size()-1)).merge((FilledTextTemplate)block);
        } else {
            blocks.add(block);
        }
    }
    @Override
    public TextTemplate fill(HashMap<String, Object> data) {
        ArrayTextTemplate ret=new ArrayTextTemplate();
        for (TextTemplate block:
             blocks) {
            ret.add((TextTemplate)block.fill(data));
        }
        return ret;
    }

    @Override
    public String toStringTemplate() {
        String ret="";
        for (TextTemplate block:
                blocks) {
            ret+=block.toStringTemplate();
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
}
