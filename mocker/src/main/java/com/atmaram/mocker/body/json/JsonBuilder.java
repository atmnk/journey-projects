package com.atmaram.mocker.body.json;

import com.atmaram.mocker.body.AbstractBuilder;
import com.atmaram.mocker.body.json.template.Template;
import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;
import com.atmaram.mocker.body.json.template.EvalTemplate;
import com.atmaram.mocker.body.json.template.TemplateBuilder;

import java.util.Arrays;
import java.util.List;
public abstract class JsonBuilder extends AbstractBuilder {
    JsonBuilder parent;
    public JsonBuilder getParent() {
        return parent;
    }
    public JsonBuilder setParent(JsonBuilder parent) {
        this.parent = parent;
        return this;
    }
    public JsonBuilder save(String variable){
        this.variable=variable;
        return this;
    }
//    public String toValue(){
////        if(variable==null){
//            return toJson();
////        } else {
////            return toJson(new DataHolder(variable));
////        }
//    }

    @Override
    public String toBody(DataPool dataPool, List<Tag> tags) {
        return toJson(dataPool,tags);
    }
    public abstract String toJson(DataPool dataPool, List<Tag> tags);

    public static ArrayBuilder array(JsonBuilder ... elements){
        ArrayBuilder arrayBuilder=new ArrayBuilder(elements);
        arrayBuilder.getElements().stream().map((element)->element.setParent(arrayBuilder));
        return arrayBuilder;
    }
    public static PrimitiveBuilder p(Object value){
        PrimitiveBuilder primitiveBuilder=new PrimitiveBuilder();
        primitiveBuilder.primitive=value;
        if(value instanceof Template){
            primitiveBuilder.save(((Template)value).getVariable());
        } else if(value instanceof TemplateBuilder){
            primitiveBuilder.save(((TemplateBuilder)value).toTemplate().getVariable());
        }
        return primitiveBuilder;
    }
    public static ArrayBuilder array(Object ... elements){
        ArrayBuilder arrayBuilder=new ArrayBuilder();
        for (Object element:
                elements) {
            if(element instanceof JsonBuilder){
                arrayBuilder.getElements().add(((JsonBuilder) element).setParent(arrayBuilder));
            } else {
                arrayBuilder.getElements().add(p(element));
            }
        }
        return arrayBuilder;
    }
    public static ObjectBuilder object(ObjectBuilder.PairBuilder... pairBuilders){
        ObjectBuilder objectBuilder=new ObjectBuilder();
        objectBuilder.pairs.addAll(Arrays.asList(pairBuilders));
        objectBuilder.pairs.stream().map((pairBuilder -> pairBuilder.pair.value.setParent(objectBuilder)));
        return objectBuilder;
    }
    public static ObjectBuilder.PairBuilder pair(String key, JsonBuilder value){
        ObjectBuilder.Pair pair=new ObjectBuilder.Pair(key,value);
        ObjectBuilder.PairBuilder pairBuilder=new ObjectBuilder.PairBuilder(pair);
        return pairBuilder;
    }
    public static ObjectBuilder.PairBuilder pair(String key, Object value){
        ObjectBuilder.Pair pair=new ObjectBuilder.Pair(key,p(value));
        ObjectBuilder.PairBuilder pairBuilder=new ObjectBuilder.PairBuilder(pair);
        return pairBuilder;
    }
    public static ArrayBuilder loop(int times,JsonBuilder jsonBuilder){
        ArrayBuilder arrayBuilder=new ArrayBuilder();
        for(int i=0;i<times;i++){
            arrayBuilder.getElements().add(jsonBuilder);
        }
        return arrayBuilder;
    }
    public static DynamicArrayBuilder all(JsonBuilder jsonBuilder,String object){
        DynamicArrayBuilder dynamicArrayBuilder=new DynamicArrayBuilder(jsonBuilder,object);
        return dynamicArrayBuilder;
    }
    public static ArrayBuilder loop(int times,Object object){
        ArrayBuilder arrayBuilder=new ArrayBuilder();
        for(int i=0;i<times;i++){
            arrayBuilder.getElements().add(p(object).setParent(arrayBuilder));
        }
        return arrayBuilder;
    }
    public static TemplateBuilder e(String template){
        TemplateBuilder builder=new TemplateBuilder(new EvalTemplate(template));
        return builder;
    }
}
