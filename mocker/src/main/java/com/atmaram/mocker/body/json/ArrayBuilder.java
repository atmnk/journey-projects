package com.atmaram.mocker.body.json;

import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayBuilder extends JsonBuilder {
    private List<JsonBuilder> elements=new ArrayList<>();

    public List<JsonBuilder> getElements() {
        return elements;
    }

    public ArrayBuilder(JsonBuilder ...  elements) {
        this.elements.addAll(Arrays.asList(elements));
    }

    public ArrayBuilder() {
    }
    public ArrayBuilder(List<JsonBuilder> elements) {
        this.elements = elements;
    }

    @Override
    public String toJson(DataPool dataPool, List<Tag> tags) {
        String finalStr="[";
        boolean first=true;
        for (JsonBuilder element:elements) {
            if(!first) {
                finalStr+=(","+element.toJson(dataPool,tags));
            } else {
                finalStr+=(element.toJson(dataPool,tags));
                first=false;
            }
        }
        finalStr+="]";
        return finalStr;
    }

    @Override
    public void gerpBody(DataPool dataPool, List<Tag> tags, String body) throws Exception {
        Pattern pattern=Pattern.compile(toRegex());
        Matcher matcher=pattern.matcher(body);
        if (matcher.find()){
            for (JsonBuilder element:
                    elements) {
                if(element.getVariable()!=null) {
                    element.gerpBody(dataPool, tags, matcher.group(element.getVariable()));
                }
            }
        } else {
            throw new Exception("Body Not Matching");
        }
    }

    @Override
    public String toRegex() {
        String finalStr="[\\s]*\\[";
        boolean first=true;
        for (JsonBuilder element:elements) {
            if(!first) {
                finalStr+=(","+element.toRegex());
            } else {
                finalStr+=(element.toRegex());
                first=false;
            }
        }
        finalStr+="\\][\\s]*";
        return finalStr;
    }
    //    @Override
//    public String toJson(DataHolder parent) {
//        String finalStr="[";
//        boolean first=true;
//        for (JsonBuilder element:elements) {
//            if(!first) {
//                finalStr+=(","+element.toJson());
//            } else {
//                finalStr+=(element.toJson());
//                first=false;
//            }
//        }
//        finalStr+="]";
//        return finalStr;
//        return null;
//    }
}
