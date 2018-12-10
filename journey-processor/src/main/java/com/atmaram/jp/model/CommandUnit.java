package com.atmaram.jp.model;

import com.atmaram.jp.Runtime;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.command.Command;
import com.atmaram.jp.exceptions.CommandConfigurationException;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import lombok.Data;
import org.json.simple.JSONArray;

import java.util.Arrays;

@Data
public class CommandUnit extends Unit {
    Command command;
    JSONArray stepLogObject=new JSONArray();
    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        try {
            command.eval(variableStore, Arrays.asList());
        } catch (CommandConfigurationException e) {
            throw new UnitConfigurationException("Command Unit not properly configured",this.name,e);
        }
    }

    @Override
    public ValueStore execute(ValueStore valueStore, int index) {
        this.printStartExecute(index);
        logObject.put("steps",stepLogObject);
        logObject.put("type","command");
        command.setLogObject(stepLogObject);
        command.execute(Arrays.asList(),valueStore,index+1);
        this.printDoneExecute(index);
        return valueStore;
    }

    @Override
    public Unit fill(ValueStore valueStore,boolean lazy) {
        return this;
    }
}
