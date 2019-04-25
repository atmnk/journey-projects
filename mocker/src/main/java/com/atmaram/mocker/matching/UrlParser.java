package com.atmaram.mocker.matching;

import com.atmaram.mocker.data.Data;
import com.atmaram.mocker.data.DataPool;
import com.atmaram.mocker.data.Tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParser {
    public static List<Tag> extract(DataPool dataPool,String pattern, String url) throws Exception{
        HashMap<String,String> tempVariable=new HashMap<>();
        TempVar tempVar=new TempVar();
        String copy=String.valueOf(pattern);
        List<Tag> tags=new ArrayList<>();
        final Pattern pPattern = Pattern.compile("\\$\\{(.+?)}");
        final Matcher matcher = pPattern.matcher(pattern);
        List<String> variables = new ArrayList<>();
        while (matcher.find()) {
            String variable=matcher.group(1);
            if(!variables.contains(variable))
                variables.add(variable);
        }
        for (String variable :
                variables) {
            if(variable.contains(".")){
                String strTempVar=tempVar.newTemp(variable);
                tempVariable.put(variable,strTempVar);
                copy = copy.replace("${" + variable + "}", "(?<"+strTempVar+">.*)");
            } else {
                copy = copy.replace("${" + variable + "}", "(?<" + variable + ">.*)");
            }
        }
        final Pattern dataPatterns=Pattern.compile(copy);
        final Matcher dataMatcher=dataPatterns.matcher(url);
        if(dataMatcher.find()){
            if(dataMatcher.groupCount()==variables.size()){
                for(int i=0;i<variables.size();i++){
                    String originalVariable=variables.get(i);
                    if(originalVariable.contains(".")){
                        String[] split=originalVariable.split("\\.");
                        if(split.length==2){
                            String tagStringValue=dataMatcher.group(tempVariable.get(originalVariable));
                            List<Tag> narrowTag=new ArrayList<>();
                            narrowTag.add(new Tag("@type",split[0]));
                            List<Data> dataList=dataPool.narrow(narrowTag,new ArrayList<>());
                            tags.add(dataList.stream().filter((d)->d.getVariable().equals(split[1]) && d.getValue().toString().equals(tagStringValue)).findFirst().get().getTagWithName("@"+split[0]+"@id"));
                        }
                    } else {
                        tags.add(new Tag(originalVariable, dataMatcher.group(originalVariable)));
                    }
                }
                return tags;
            } else {
                throw new Exception("Url Not Match");
            }
        } else {
            throw new Exception("Url Not Match");
        }
    }
    public static void extractJSONBody(String pattern, String url) throws Exception{
        String copy=String.valueOf(pattern);
        final Pattern pPattern = Pattern.compile(pattern);
        final Matcher matcher = pPattern.matcher(url);
        while (matcher.find()) {
            System.out.println(matcher.group("name"));
            System.out.println(matcher.group("country"));
        }
    }
    public static class TempVar{
        public List<String> temps=new ArrayList<>();
        public String newTemp(String name){
            Random rn=new Random();
            int suffix=rn.nextInt(Integer.MAX_VALUE) + 1;
            String newTemp=name.replaceAll("[^A-Za-z0-9]","")+suffix;
            if(temps.contains(newTemp)){
                return newTemp(name);
            } else {
                temps.add(newTemp);
                return newTemp;
            }
        }
    }

}
