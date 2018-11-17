package com.atmaram.tp.common;

import com.atmaram.tp.template.Variable;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionTreeTest {
    //Get Variables
    @Test
    public void should_give_empty_list_for_constant(){
        ExpressionTree tree=ExpressionProcessor.toTree("1");
        assertThat(tree.getVariables()).isEqualTo(Arrays.asList());
    }
    @Test
    public void should_give_single_variable_for_variable(){
        ExpressionTree tree=ExpressionProcessor.toTree("Hello World");
        List<Variable> vars=tree.getVariables();
        Variable variable=new Variable();
        variable.setType("String");
        variable.setName("Hello World");
        assertThat(vars).isEqualTo(Arrays.asList(variable));    }
    @Test
    public void should_get_single_variable(){
        ExpressionTree tree=ExpressionProcessor.toTree("_mid(Var,1,1)");
        List<Variable> vars=tree.getVariables();
        Variable variable=new Variable();
        variable.setType("String");
        variable.setName("Var");
        assertThat(vars).isEqualTo(Arrays.asList(variable));
    }
    @Test
    public void should_get_multiple_variable(){
        ExpressionTree tree=ExpressionProcessor.toTree("_concat(Var1,Var2)");
        List<Variable> vars=tree.getVariables();
        Variable variable1=new Variable();
        variable1.setType("String");
        variable1.setName("Var1");
        Variable variable2=new Variable();
        variable2.setType("String");
        variable2.setName("Var2");
        assertThat(vars).isEqualTo(Arrays.asList(variable1,variable2));
    }

    //solve

    @Test
    public void should_give_same_expresion_tree_for_constant(){
        ExpressionTree tree=ExpressionProcessor.toTree("1");
        assertThat(tree.solve(new HashMap<>())).isEqualTo(tree);
    }

    @Test
    public void should_give_same_expresion_tree_for_variable_if_no_value_in_context(){
        ExpressionTree tree=ExpressionProcessor.toTree("Var");
        assertThat(tree.solve(new HashMap<>())).isEqualTo(tree);
    }

    @Test
    public void should_give_constant_expresion_tree_for_variable_if_value_in_context(){
        ExpressionTree tree=ExpressionProcessor.toTree("Var");
        HashMap context=new HashMap();
        context.put("Var",1);
        ExpressionTree actual=tree.solve(context);
        assertThat(actual.getConstant()).isEqualTo(1);
    }
    @Test
    public void should_give_same_expresion_tree_if_unsolved(){
        ExpressionTree tree=ExpressionProcessor.toTree("_mid(Var,1,1)");
        ExpressionTree solved=tree.solve(new HashMap<>());
        assertThat(solved.rootProcessor).isEqualTo(tree.rootProcessor);
        assertThat(solved.getArgs()).isEqualTo(tree.getArgs());
    }

    @Test
    public void should_give_constant_expresion_tree_if_solved(){
        ExpressionTree tree=ExpressionProcessor.toTree("_mid(Var,1,1)");
        HashMap context=new HashMap();
        context.put("Var","Hello");
        ExpressionTree actual=tree.solve(context);
        assertThat(actual.getConstant()).isNotNull();
        assertThat(actual.getConstant()).isEqualTo("e");
    }

    //To Expression

    @Test
    public void should_give_quoted_string_for_string_constant(){
        ExpressionTree tree=ExpressionProcessor.toTree("'Hello'");
        assertThat(tree.solve(new HashMap<>()).toExpression()).isEqualTo("'Hello'");
    }
    @Test
    public void should_give_nonquoted_string_for_integer_constant(){
        ExpressionTree tree=ExpressionProcessor.toTree("1");
        assertThat(tree.solve(new HashMap<>()).toExpression()).isEqualTo("1");
    }
    @Test
    public void should_give_object_for_any_other_constant(){
        ExpressionTree expressionTree=new ExpressionTree();
        expressionTree.constant=new Date();
        assertThat(expressionTree.solve(new HashMap<>()).toExpression()).isInstanceOf(Date.class);
    }
    @Test
    public void should_give_string_variable_with_variable_name_for_v(){
        ExpressionTree expressionTree=new ExpressionTree();
        expressionTree.variable="Var";
        assertThat(expressionTree.solve(new HashMap<>()).toExpression()).isEqualTo("Var");
    }

    @Test
    public void should_give_same_expression(){
        ExpressionTree expressionTree=ExpressionProcessor.toTree("_mid(Var,1,1)");
        assertThat(expressionTree.solve(new HashMap<>()).toExpression()).isEqualTo("_mid(Var,1,1)");
    }
    @Test
    public void should_give_same_expression_even_if_no_args(){
        ExpressionTree expressionTree=ExpressionProcessor.toTree("_timestamp");
        assertThat(expressionTree.toExpression()).isEqualTo("_timestamp");
    }
    @Test
    public void should_give_empty_string_if_everything_is_null(){
        ExpressionTree expressionTree=new ExpressionTree();
        assertThat(expressionTree.toExpression()).isEqualTo("");
    }

}
