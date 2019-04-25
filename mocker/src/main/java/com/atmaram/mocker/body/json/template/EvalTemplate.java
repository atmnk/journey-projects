package com.atmaram.mocker.body.json.template;
import com.atmaram.mocker.data.DataPool;

import java.util.Random;


public class EvalTemplate extends Template{
    public EvalTemplate(String template) {
        super(template);
    }

    @Override
    public Object toValue(DataPool dataPool) {
        if(template.startsWith("$")){
            String var=template.substring(1);
            return dataPool.getData(var);
        }
        Random random=new Random();
        return random.nextInt();
    }

    @Override
    public String toRegex() {
        if(template.startsWith("$")){
            String var=template.substring(1);
            return "[\\s]*(?<"+var+">.*)[\\s]*";
        } else {
            return template;
        }
    }
}
