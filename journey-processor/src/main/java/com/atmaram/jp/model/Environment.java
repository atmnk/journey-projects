package com.atmaram.jp.model;

import com.atmaram.tp.Variable;
import lombok.Data;

import java.util.List;

@Data
public class Environment {
    List<EnvironmentVariable> variables;
}
