package com.atmaram.mocker.data;

import java.util.ArrayList;
import java.util.List;

public class Data {
    String variable;
    Object value;
    List<Tag> tags;

    public boolean hasTag(String tagName){
        for (Tag tag:
             tags) {
            if(tag.variable.equals(tagName))
                return true;
        }
        return false;
    }
    public boolean hasTag(Tag serachTag){
        for (Tag tag:
                tags) {
            if(tag.variable.equals(serachTag.variable) && tag.value.equals(serachTag.value))
                return true;
        }
        return false;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public List<Tag> getTags() {
        return tags;
    }
    public Tag getTagWithName(String name){
        for (Tag tag:
             tags) {
            if(tag.variable.equals(name)){
                return tag;
            }
        }
        return null;
    }
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Data(String variable, Object value, List<Tag> tags) {
        this.variable = variable;
        this.value = value;
        this.tags = tags;
    }
    public Object getTagValue(String tag){
        return tags.stream().filter((toMatch)->toMatch.variable.equals(tag)).findFirst().get();
    }
    public long matchingTagCount(List<Tag> searchTags){
        long count=0;
        List<Tag> mathingTags=new ArrayList<>();
        for (Tag tag:
             tags) {
            for (Tag searchTag:
                 searchTags) {
                if(matchTagName(tag,searchTag)){
                    if(!mathingTags.contains(tag)) {
                        mathingTags.add(tag);
                    }
                    break;
                }
            }
        }
        return mathingTags.size();
    }
    public static boolean matchTagName(Tag tag1,Tag tag2){
        if(tag1.variable.equals(tag2.variable))
            return true;
        return false;
    }
}
