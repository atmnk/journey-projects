package com.atmaram.tp.text;

import com.atmaram.tp.Template;
import com.atmaram.tp.Variable;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import java.util.HashMap;
import java.util.List;

public interface TextTemplate extends Template {
    public static TextTemplate parse(String text) throws TemplateParseException {
        ArrayTextTemplate arrayTextTemplate=new ArrayTextTemplate();
        String strBlock="";
        int i=0;
        while(i<text.length()){
            if(text.length()>i+1 && text.substring(i,i+2).equals("${"))
            {
                if(!strBlock.equals(""))
                {
                    arrayTextTemplate.add(new FilledTextTemplate(strBlock));
                    strBlock="";
                }
                i+=2;
                String variable="";
                while(text.charAt(i)!='}')
                {
                    variable+=text.charAt(i++);
                }
                TextVariableTemplate textVariableTemplate=new TextVariableTemplate(variable);
                arrayTextTemplate.add(textVariableTemplate);
            } else if (text.length()>i+2 && text.substring(i,i+3).equals("{{#")){
                if(!strBlock.equals(""))
                {
                    arrayTextTemplate.add(new FilledTextTemplate(strBlock));
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
                TextLoopTemplate textLoopTemplate=new TextLoopTemplate(variable,TextTemplate.parse(strInnerTemplate));
                arrayTextTemplate.add(textLoopTemplate);
            } else {
                strBlock+=text.charAt(i);
            }
            i++;

        }
        if(!strBlock.equals(""))
        {
            arrayTextTemplate.add(new FilledTextTemplate(strBlock));
        }
        if(arrayTextTemplate.blocks.size()==1){
            return arrayTextTemplate.blocks.get(0);
        } else {
            return arrayTextTemplate;
        }
    }
}
