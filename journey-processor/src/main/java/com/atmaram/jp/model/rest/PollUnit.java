package com.atmaram.jp.model.rest;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.jp.model.Unit;
import lombok.Data;

@Data
public class PollUnit extends Unit {
    public Unit pollThis;
    public String pollVariableName;
    public Object pollVariableValue;

    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        pollThis.eval(variableStore);
    }

    @Override
    public ValueStore execute(ValueStore valueStore, int index) {
        this.printStartExecute(index);
        int count=1;
        while(!valueStore.getValues().containsKey(pollVariableName) || !valueStore.getValues().get(pollVariableName).equals(pollVariableValue)){
            this.print(index+1,"Polling "+count);
            pollThis.execute(valueStore,index+2);
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
    public Unit fill(ValueStore valueStore) {
        PollUnit pollUnit=new PollUnit();
        pollUnit.setPollVariableName(pollVariableName);
        pollUnit.setPollVariableValue(pollVariableValue);
        pollUnit.setPollThis(pollThis.fill(valueStore));
        pollUnit.setName(this.name);
        pollUnit.setWait(this.wait);
        return pollUnit;
    }
}
