package com.atmaram.mocker.data;

import com.atmaram.mocker.body.json.ObjectBuilder;

import java.util.List;

public class Tag {
    String variable;
    Object value;

    public Tag(String variable, Object value) {
        this.variable = variable;
        this.value = value;
    }
    public boolean matchesNone(List<Tag> tags){
        for (Tag tag:
             tags) {
            if(this.matches(tag))
                return false;
        }
        return true;
    }
    public boolean matches(Tag tag){
        if(variable.equals(tag.variable) && value.equals(tag.value)){
            return true;
        }
        return false;
    }
    @Override
    public boolean equals(Object tag){
        if(tag instanceof Tag) {
            if (this.variable.equals(((Tag)tag).variable) && this.value.equals(((Tag)tag).value)) {
                return true;
            }
        }
        return false;
    }
}
