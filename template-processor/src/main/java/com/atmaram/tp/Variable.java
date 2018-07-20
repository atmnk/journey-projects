package com.atmaram.tp;
import lombok.Data;

import java.util.List;
@Data
public class Variable {
    String name;
    String type;
    List<Variable> inner_variables;
}
