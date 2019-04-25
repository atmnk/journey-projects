package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.Runtime;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.Unit;
import lombok.Data;
import org.json.simple.JSONArray;

import java.util.HashMap;

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
        logObject.put("iterations",stepLogObject);
        logObject.put("type","poll");
        this.printStartExecute(index);
        int count=1;
        while(!valueStore.getValues().containsKey(pollVariableName) || !valueStore.getValues().get(pollVariableName).equals(pollVariableValue)){
            this.print(index+1,"Polling "+count);
            Unit filledUnit=pollThis.fill(valueStore,false);
            filledUnit.parentLogObject=stepLogObject;
            filledUnit.execute(valueStore,index+2);
            this.print(index+1,"Polling Done "+count++);
        }
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.printDoneExecute(index);
        return valueStore;
    }

    @Override
    public Unit fill(ValueStore valueStore,boolean lazy) {
        PollUnit pollUnit=new PollUnit();
        Unit filledThisPollUnit=pollThis.fill(valueStore,true);
        filledThisPollUnit.setParentLogObject(this.parentLogObject);
        pollUnit.setPollVariableName(pollVariableName);
        pollUnit.setPollVariableValue(pollVariableValue);
        pollUnit.setPollThis(filledThisPollUnit);
        pollUnit.setName(this.name);
        pollUnit.setWait(this.wait);
        pollUnit.parentLogObject=this.parentLogObject;
        return pollUnit;
    }
}
