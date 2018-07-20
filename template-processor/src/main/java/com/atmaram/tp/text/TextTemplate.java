package com.atmaram.tp.text;

import com.atmaram.tp.Variable;
import com.atmaram.tp.common.VariableValueProcessor;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TextTemplate {


    List<Object> blocks;
    public List<Object> getBlocks() {
        return blocks;
    }
    public static TextTemplate parse(String text) throws TemplateParseException {
        TextTemplate textTemplate=new TextTemplate();
        textTemplate.blocks=new ArrayList<>();
        String strBlock="";
        int i=0;
        while(i<text.length()){
            if(text.length()>i+1 && text.substring(i,i+2).equals("${"))
            {
                if(!strBlock.equals(""))
                {
                    textTemplate.blocks.add(strBlock);
                    strBlock="";
                }
                i+=2;
                String variable="";
                while(text.charAt(i)!='}')
                {
                    variable+=text.charAt(i++);
                }
                TextVariable textVariable=new TextVariable();
                textVariable.name=variable;
                textTemplate.blocks.add(textVariable);
            } else if (text.length()>i+2 && text.substring(i,i+3).equals("{{#")){
                if(!strBlock.equals(""))
                {
                    textTemplate.blocks.add(strBlock);
                    strBlock="";
                }
                i+=3;
                String variable="";
                while(!text.substring(i,i+2).equals("}}"))
                {
                    variable+=text.charAt(i++);
                }
                i=i+2;
                String openingTag="{{#"+variable+"}}";
                String closingTag="{{/"+variable+"}}";
                int count=1;
                int start=i;
                while(count!=0) {
                    if(i>=text.length()){

                        throw new TemplateParseException("No closing Tag found for tag: "+openingTag);
                    }
                    if (text.length()>=i+openingTag.length() && text.substring(i, i + openingTag.length()).equals(openingTag)) {
                        i=i+openingTag.length();
                        count++;
                        continue;
                    } else if (text.length()>=i+closingTag.length() && text.substring(i, i + closingTag.length()).equals(closingTag)) {
                        i=i+closingTag.length();
                        count--;
                        continue;
                    }
                    i++;
                }
                String strInnerTemplate=text.substring(start,i-closingTag.length());
                TextLoop textLoop=new TextLoop();
                textLoop.variable=variable;
                textLoop.inner_template=TextTemplate.parse(strInnerTemplate);
                textTemplate.blocks.add(textLoop);
            } else {
                strBlock+=text.charAt(i);
            }
            i++;

        }
        if(!strBlock.equals(""))
        {
            textTemplate.blocks.add(strBlock);
        }
        return textTemplate;
    }
    public String fill(HashMap<String,Object> data){
        String result="";
        for(int i=0;i<blocks.size();i++){
            Object oBlock=blocks.get(i);
            if(oBlock instanceof String){
                result+=(String)oBlock;
            } else if(oBlock instanceof TextVariable){
                TextVariable textVariable=(TextVariable)oBlock;
                Object value;
                value=VariableValueProcessor.getValue(textVariable.name,data);
                result+=value;
            } else if(oBlock instanceof  TextLoop){
                TextLoop textLoop=(TextLoop)oBlock;
                List<Object> dataList=(List<Object>)data.get(textLoop.variable);
                for (Object inner_data:
                     dataList) {
                    String inner="";
                    if(inner_data instanceof HashMap) {
                        inner = textLoop.inner_template.fill((HashMap<String, Object>) inner_data);
                    } else {
                        HashMap<String,Object> inner_data_hm=new HashMap<>();
                        inner_data_hm.put("_this",inner_data);
                        inner = textLoop.inner_template.fill(inner_data_hm);
                    }
                    try {
                        TextTemplate newTextTemplate=TextTemplate.parse(inner);
                        String new_inner=newTextTemplate.fill(data);
                        result+=new_inner;
                    } catch (TemplateParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return result;
    }
    public List<Variable> getVariables(){
        List<Variable> returnValue=new ArrayList<>();
        for (Object oValue:
                blocks) {

            if(oValue instanceof String){

            } else if(oValue instanceof TextLoop){
                TextLoop tlValue=(TextLoop)oValue;
                Variable variable=new Variable();
                variable.setName(tlValue.variable);
                variable.setType("List");
                TextTemplate inner_tt=tlValue.inner_template;
                List<Variable> inner_variables=inner_tt.getVariables();
                List<Variable> inner_variables_excluding_this=new ArrayList<>();
                boolean found_this_variable=false;
                for (Variable inner_variable:inner_variables
                        ) {
                    if(inner_variable.getName().equals("_this")){
                        found_this_variable=true;
                    } else {
                        inner_variables_excluding_this.add(inner_variable);
                    }
                }
                if(found_this_variable){
                    returnValue.add(variable);
                    returnValue.addAll(inner_variables_excluding_this);
                } else {
                    variable.setInner_variables(inner_variables_excluding_this);
                    returnValue.add(variable);
                }

            } else if(oValue instanceof TextVariable){
                if(((TextVariable)oValue).name.startsWith("_") && !((TextVariable)oValue).name.equals("_this")){
                    //do nothing;
                } else{
                    Variable variable=new Variable();
                    variable.setName(((TextVariable)oValue).name);
                    variable.setType("String");
                    returnValue.add(variable);
                }

            }
        }
        return returnValue;
    }
}
