package com.atmaram.mocker.body.json;

import com.atmaram.mocker.data.Data;
import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.groupingBy;
public class DynamicArrayBuilder extends ArrayBuilder {
    JsonBuilder innerObjectBuilder;
    String object;


    public DynamicArrayBuilder(JsonBuilder innerObjectBuilder, String object) {
        this.innerObjectBuilder = innerObjectBuilder;
        this.object=object;
    }
    @Override
    public String toJson(DataPool dataPool, List<Tag> tags) {
        String finalStr="[";
        boolean first=true;
        List<Tag> newTagList=new ArrayList<>();
        newTagList.addAll(tags);
        newTagList.add(new Tag("@type",object));
//        newTagList.add(new Tag("@"+object+"@id",object));
        List<Data> rest=new ArrayList<>();
        List<Data> dataWithTag=dataPool.getDataWithTag("@"+object+"@id").narrow(tags,rest);
        //List<Data> data=dataPool.getDataWithTags(newTagList,"@"+object+"@id");
        List<Data> withoutTag=dataPool.getDataWithoutTag("@"+object+"@id");
        Map<Object,List<Data>> tuples=dataWithTag.stream().collect(groupingBy(innerData ->{return innerData.getTagValue("@"+object+"@id");} ));
        for (Object key :tuples.keySet()) {
            DataPool newDataPool=new DataPool(dataPool);
            newDataPool.addAll(tuples.get(key),false);
            newDataPool.addAll(rest,false);
            newDataPool.addAll(withoutTag,false);
            newDataPool.order(newTagList);
            if(!first) {
                finalStr+=(","+innerObjectBuilder.toJson(newDataPool,newTagList));
            } else {
                finalStr+=(innerObjectBuilder.toJson(newDataPool,newTagList));
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
        while (matcher.find()){
            JsonBuilder element=innerObjectBuilder;
            if(element.getVariable()!=null) {
                element.gerpBody(dataPool, tags, matcher.group(element.getVariable()));
            }
        }
    }

    @Override
    public String toRegex() {
        return "[\\s]*\\["+innerObjectBuilder.toRegex()+"\\][\\s]*";
    }
}
