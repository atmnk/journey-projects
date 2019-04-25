package com.atmaram.mocker.body.json;

import com.atmaram.mocker.body.json.template.Template;
import com.atmaram.mocker.data.Data;
import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;
import com.atmaram.mocker.body.json.template.TemplateBuilder;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PrimitiveBuilder extends JsonBuilder {
    Object primitive;
    @Override
    public String toJson(DataPool dataPool,List<Tag> tagList) {
        return string(dataPool,primitive,tagList);
    }

    private static String string(DataPool dataPool,Object object,List<Tag> tags){
        if(object instanceof String){
            return "\""+object.toString()+"\"";
        } if(object instanceof TemplateBuilder){
            TemplateBuilder templateBuilder=(TemplateBuilder)object;
            Object value=templateBuilder.toTemplate().toValue(dataPool,tags);
            if(value instanceof String){
                return "\""+value.toString()+"\"";
            }
            return value.toString();
        }
        return object.toString();
    }

    @Override
    public String toRegex() {
        if(primitive instanceof TemplateBuilder){
            TemplateBuilder templateBuilder=(TemplateBuilder)primitive;
            return templateBuilder.toTemplate().toRegex();
        }
        return "[\\s]*(?<"+variable+">.*)[\\s]*";
    }
    @Override
    public void gerpBody(DataPool dataPool, List<Tag> tags, String body) throws Exception {
        Pattern pattern=Pattern.compile(toRegex());
        Matcher matcher=pattern.matcher(body);
        if (matcher.find()){
            Data data;
            Object toStore=null;
            if(primitive instanceof Template) {
                toStore=matcher.group(0);
            } else if(primitive instanceof String){
                toStore=matcher.group(0).substring(1,matcher.group(0).length()-1);
            } else if(primitive instanceof Integer){
                toStore=Integer.parseInt(matcher.group(0));
            }else if(primitive instanceof Long){
                toStore=Long.parseLong(matcher.group(0));
            }else if(primitive instanceof Float){
                toStore=Float.parseFloat(matcher.group(0));
            }else if(primitive instanceof Double){
                toStore=Double.parseDouble(matcher.group(0));
            }
            data = new Data(variable, toStore, tags);
            dataPool.add(data,true);
        } else {
            throw new Exception("Value not found");
        }
    }
}
