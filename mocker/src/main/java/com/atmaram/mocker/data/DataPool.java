package com.atmaram.mocker.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DataPool {
    DataPool parent=null;
    List<Data> dataList=new ArrayList<>();

    public DataPool(DataPool parent) {
        this.parent=parent;
    }

    public DataPool() {
    }
    public List<Data> narrow(List<Tag> tags,List<Data> rest){
        long maxMatching=dataList.stream().map((d)->d.matchingTagCount(tags)).max(Long::compareTo).orElse(0L);
        rest.addAll(dataList.stream().filter((d)->d.matchingTagCount(tags)<maxMatching).collect(Collectors.toList()));
        return dataList.stream().filter((d)->d.matchingTagCount(tags)>=maxMatching).collect(Collectors.toList());

    }
    public void add(Data data,boolean addToParent){
        dataList.add(data);
        if(parent!=null && addToParent){
            parent.add(data,addToParent);
        }
    }
    public void addAll(List<Data> data,boolean addToParent){
        dataList.addAll(data);
        if(parent!=null && addToParent){
            parent.addAll(data,addToParent);
        }
    }
    public Data getSingleDataWithTags(String variable,List<Tag> tags){
        Optional<Data> value= dataList.stream().filter((data)-> {
            boolean selectThis=true;
            selectThis=selectThis && data.variable.equals(variable);
            for (Tag tag:data.tags
            ) {
                if(tag.matchesNone(tags)){
                    selectThis=false;
                    break;
                }
            }
            return selectThis;
        }).findFirst();
        if(value.isPresent()){
            return value.get();
        }
        return null;

    }

    public List<Data> getDataWithTags(List<Tag> tags){
        return dataList.stream().filter((data)-> {
            boolean selectThis=true;
            //electThis=selectThis && data.variable.equals(variable);
            for (Tag tag:tags
                 ) {
                if(tag.matchesNone(data.tags)){
                    selectThis=false;
                    break;
                }
            }
            return selectThis;
        }).collect(Collectors.toList());
    }
    public List<Data> getAllDataWithTags(List<Tag> tags){
        return dataList.stream().filter((data)-> {
            boolean selectThis=true;
            for (Tag tag:tags
            ) {
                if(tag.matchesNone(data.tags)){
                    selectThis=false;
                    break;
                }
            }
            return selectThis;
        }).collect(Collectors.toList());
    }
    public List<Data> getDataWithoutTag(String tagName){
        return dataList.stream().filter((data -> !data.hasTag(tagName))).collect(Collectors.toList());
    }
    public DataPool getDataWithTag(String tagName){
        DataPool dataPool=new DataPool();
        dataPool.addAll(dataList.stream().filter((data -> data.hasTag(tagName))).collect(Collectors.toList()),false);
        return dataPool;
    }
    public Object getData(String variable){
        return dataList.stream().filter((data)->data.variable.equals(variable)).findFirst().get().getValue();
    }
    public void order(final List<Tag> tagList){
        dataList.sort((data1,data2)->{
            return data1.matchingTagCount(tagList)<data2.matchingTagCount(tagList)?
                    1:data1.matchingTagCount(tagList)>data2.matchingTagCount(tagList)?-1:0;
        });
    }
}
