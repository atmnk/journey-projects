package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import lombok.Data;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

@Data
public abstract class Unit {
    public JSONObject logObject=new JSONObject();
    public String name;
    public int wait=0;
    public abstract void eval(VariableStore variableStore) throws UnitConfigurationException;
    public abstract ValueStore execute(ValueStore valueStore,int index);
    public abstract Unit fill(ValueStore valueStore);
    public void printStartExecute(int index){
        logObject.put("name",this.name);
        String prefix="";
        for(int i=0;i<index;i++)
        {
            prefix+="\t";
        }
        System.out.println(prefix+"Starting Execution of Unit: "+name);
    }
    public void printDoneExecute(int index){
        String prefix="";
        for(int i=0;i<index;i++)
        {
            prefix+="\t";
        }
        System.out.println(prefix+"Done Execution of Unit: "+name);
    }

    public void print(int index,String message){
        String prefix="";
        for(int i=0;i<index;i++)
        {
            prefix+="\t";
        }
        System.out.println(prefix+message);
    }

}
