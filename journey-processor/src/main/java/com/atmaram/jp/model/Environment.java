package com.atmaram.jp.model;

import com.atmaram.tp.Variable;
import lombok.Data;

import java.util.List;

@Data
public class Environment {
    String name;
    List<EnvironmentVariable> variables;
}
