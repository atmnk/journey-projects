package com.atmaram.jp.model;

import com.atmaram.jp.RestClient;
import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import com.atmaram.tp.common.exceptions.TemplateParseException;
import lombok.Data;
import org.json.simple.parser.ParseException;

@Data
public abstract class Unit {
    String name;
    int wait=0;
    public abstract void eval(VariableStore variableStore) throws UnitConfigurationException;
    public abstract ValueStore execute(RestClient restClient, ValueStore valueStore);
    public abstract Unit fill(ValueStore valueStore);
}
