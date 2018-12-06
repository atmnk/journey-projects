package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.Runtime;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.Unit;
import lombok.Data;
import org.json.simple.JSONArray;

@Data
public class PollUnit extends Unit {
    JSONArray stepLogObject=new JSONArray();
    public Unit pollThis;
    public String pollVariableName;
    public Object pollVariableValue;

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        pollThis.eval(variableStore);
    }

    @Override
    public ValueStore execute(ValueStore valueStore, int index) {
        JSONArray prevLogObject= Runtime.currentLogObject;
        prevLogObject.add(logObject);
        logObject.put("iterations",stepLogObject);
        logObject.put("type","poll");
        this.printStartExecute(index);
        int count=1;
        while(!valueStore.getValues().containsKey(pollVariableName) || !valueStore.getValues().get(pollVariableName).equals(pollVariableValue)){
            Runtime.currentLogObject=stepLogObject;
            this.print(index+1,"Polling "+count);
            pollThis.fill(valueStore,false).execute(valueStore,index+2);
            this.print(index+1,"Polling Done "+count++);
        }
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.printDoneExecute(index);
        Runtime.currentLogObject=prevLogObject;
        return valueStore;
    }

    @Override
    public Unit fill(ValueStore valueStore,boolean lazy) {
        PollUnit pollUnit=new PollUnit();
        pollUnit.setPollVariableName(pollVariableName);
        pollUnit.setPollVariableValue(pollVariableValue);
        pollUnit.setPollThis(pollThis.fill(valueStore,true));
        pollUnit.setName(this.name);
        pollUnit.setWait(this.wait);
        return pollUnit;
    }
}
