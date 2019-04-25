package com.atmaram.mocker.body.json;

import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectBuilder  extends JsonBuilder{
    static HashMap<String,Long> ids=new HashMap<>();
    List<PairBuilder> pairs=new ArrayList<>();
    public static class PairBuilder{
        Pair pair;

        public PairBuilder(Pair pair) {
            this.pair = pair;
        }
        public String toRegex(){
            String regex="[\\s]*\""+pair.key+"\"[\\s]*:"+pair.value.toRegex()+"[\\s]*";
            return regex;
        }
    }
    public static class Pair{
        String key;
        JsonBuilder value;

        public Pair(String key, JsonBuilder value) {
            this.key = key;
            this.value = value;
        }
        public String toPartialJson(DataPool dataPool,List<Tag> tags){
            return "\""+key+"\":"+value.toJson(dataPool,tags);
        }
    }

    @Override
    public String toJson(DataPool dataPool, List<Tag> tags) {
        String ret="{";
        boolean first=true;
        for (PairBuilder pairBuilder:
             pairs) {
            List<Tag> newList=new ArrayList<>();
            newList.addAll(tags);
            if(this.variable!=null){
                newList.add(new Tag("@type",this.variable));
                newList.add(new Tag("@"+this.variable+"@id",id(this.variable)));
            }
            if(!first) {
                ret+=(","+pairBuilder.pair.toPartialJson(dataPool, newList));
            } else {
                ret+=(pairBuilder.pair.toPartialJson(dataPool, newList));
                first=false;
            }
        }
        ret+="}";
        return ret;
    }
    public static long id(String type){
        if(ids.containsKey(type)){
            long current=ids.get(type);
            ids.put(type,current+1);
            return current+1;
        } else {
            ids.put(type,0L);
            return 0;
        }
    }
    @Override
    public void gerpBody(DataPool dataPool, List<Tag> tags, String body) throws Exception {
        Pattern pattern=Pattern.compile(toRegex());
        Matcher matcher=pattern.matcher(body);
        if (matcher.find()){
            if(this.variable!=null){
                tags.add(new Tag("@type",this.variable));
                tags.add(new Tag("@"+this.variable+"@id",id(this.variable)));
            }
            for (PairBuilder pairBuilder:
                 pairs) {
                if(pairBuilder.pair.value.getVariable()!=null) {
                    pairBuilder.pair.value.gerpBody(dataPool, tags, matcher.group(pairBuilder.pair.value.getVariable()));
                }
            }

        } else {
            throw new Exception("Body Not Matching");
        }
    }

    @Override
    public String toRegex() {
        String ret="";
        ret+="[\\s]*\\{";
        boolean first=true;
        for (PairBuilder pairBuilder:
                pairs) {

            if(!first) {
                ret+=(","+pairBuilder.toRegex());
            } else {
                ret+=(pairBuilder.toRegex());
                first=false;
            }
        }
        ret+="\\}[\\s]*";
        return ret;
    }
}
