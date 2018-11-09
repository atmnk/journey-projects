package com.atmaram.tp.common;

import com.atmaram.tp.Variable;
import org.junit.Test;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ExpressionProcessorTest {

    //Get Variables
    @Test
    public void should_get_variables_when_no_output_redirection() {
        List<Variable> vars=ExpressionProcessor.getVariables("_mid(Var,1,1)");
        assertThat(vars.size()).isEqualTo(1);
    }
    @Test
    public void should_get_variables_when_output_redirection() {
        List<Variable> vars=ExpressionProcessor.getVariables("_mid(Var1,1,1)>Var2");
        assertThat(vars.size()).isEqualTo(1);
    }

    //Process
    //When output redirection
    @Test
    public void should_give_string_results(){
        Object data=ExpressionProcessor.process("_mid('Hello',1,1)>Var",new HashMap<>());
        assertThat(data).isInstanceOf(String.class);
        assertThat(data).isEqualTo("e");
    }
    @Test
    public void should_give_integer_results(){
        Object data=ExpressionProcessor.process("1+1>Var",new HashMap<>());
        assertThat(data).isInstanceOf(Integer.class);
        assertThat(data).isEqualTo(2);
    }
    @Test
    public void should_give_variable_results(){
        Object data=ExpressionProcessor.process("Var1>Var2",new HashMap<>());
        assertThat(data).isInstanceOf(String.class);
        assertThat(data).isEqualTo("${Var1}");
    }
    @Test
    public void should_give_other_objects(){
        Object data=ExpressionProcessor.process("_now>Var",new HashMap<>());
        assertThat(data).isInstanceOf(Date.class);
    }

    //When no output redirection
    @Test
    public void should_give_string_results_when_no_or(){
        Object data=ExpressionProcessor.process("_mid('Hello',1,1)",new HashMap<>());
        assertThat(data).isInstanceOf(String.class);
        assertThat(data).isEqualTo("e");
    }
    @Test
    public void should_give_integer_results_when_no_or(){
        Object data=ExpressionProcessor.process("1+1",new HashMap<>());
        assertThat(data).isInstanceOf(Integer.class);
        assertThat(data).isEqualTo(2);
    }
    @Test
    public void should_give_variable_results_when_no_or(){
        Object data=ExpressionProcessor.process("Var1",new HashMap<>());
        assertThat(data).isInstanceOf(String.class);
        assertThat(data).isEqualTo("${Var1}");
    }
    @Test
    public void should_give_other_objects_when_no_or(){
        Object data=ExpressionProcessor.process("_now",new HashMap<>());
        assertThat(data).isInstanceOf(Date.class);
    }


    //toTree
    @Test
    public void should_get_string_constant_from_expression(){
        ExpressionTree tree=ExpressionProcessor.toTree("'AA'");
        assertThat(tree.getConstant()).isEqualTo("AA");
    }
    @Test
    public void should_get_integer_constant_from_expression(){
        ExpressionTree tree=ExpressionProcessor.toTree("1");
        assertThat(tree.getConstant()).isEqualTo(1);
    }
    @Test
    public void should_get_this_variable_from_expression(){
        ExpressionTree tree=ExpressionProcessor.toTree("_this");
        assertThat(tree.getVariable()).isEqualTo("_this");
    }
    @Test
    public void should_get_plain_variable_from_expression(){
        ExpressionTree tree=ExpressionProcessor.toTree("Var");
        assertThat(tree.getVariable()).isEqualTo("Var");
    }
    //Plus
    @Test
    public void should_construct_expression_tree_with_plus_with_string(){
        ExpressionTree tree=ExpressionProcessor.toTree("'AA'+1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.ADD);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo("AA");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_plus_with_int(){
        ExpressionTree tree=ExpressionProcessor.toTree("12+1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.ADD);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo(12);
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_plus_with_function(){
        ExpressionTree tree=ExpressionProcessor.toTree("_plus(1,1)+1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.ADD);
        assertThat(tree.getArgs().get(0).toExpression()).isEqualTo("_plus(1,1)");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }



    @Test
    public void should_construct_expression_tree_with_plus_with_this(){
        ExpressionTree tree=ExpressionProcessor.toTree("_this+1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.ADD);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("_this");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_plus_with_variable(){
        ExpressionTree tree=ExpressionProcessor.toTree("Var+1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.ADD);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("Var");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    //MINUS
    @Test
    public void should_construct_expression_tree_with_minus_with_string(){
        ExpressionTree tree=ExpressionProcessor.toTree("'AA'-1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.SUBSTRACT);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo("AA");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_minus_with_int(){
        ExpressionTree tree=ExpressionProcessor.toTree("12-1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.SUBSTRACT);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo(12);
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_minus_with_function(){
        ExpressionTree tree=ExpressionProcessor.toTree("_plus(1,1)-1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.SUBSTRACT);
        assertThat(tree.getArgs().get(0).toExpression()).isEqualTo("_plus(1,1)");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_minus_with_this(){
        ExpressionTree tree=ExpressionProcessor.toTree("_this-1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.SUBSTRACT);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("_this");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_minus_with_variable(){
        ExpressionTree tree=ExpressionProcessor.toTree("Var-1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.SUBSTRACT);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("Var");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    //DIVIDE
    @Test
    public void should_construct_expression_tree_with_devide_with_string(){
        ExpressionTree tree=ExpressionProcessor.toTree("'AA'/1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Devide);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo("AA");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_devide_with_int(){
        ExpressionTree tree=ExpressionProcessor.toTree("12/1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Devide);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo(12);
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_devide_with_function(){
        ExpressionTree tree=ExpressionProcessor.toTree("_plus(1,1)/1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Devide);
        assertThat(tree.getArgs().get(0).toExpression()).isEqualTo("_plus(1,1)");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_devide_with_this(){
        ExpressionTree tree=ExpressionProcessor.toTree("_this/1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Devide);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("_this");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_devide_with_variable(){
        ExpressionTree tree=ExpressionProcessor.toTree("Var/1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Devide);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("Var");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    //Multiply
    @Test
    public void should_construct_expression_tree_with_multiply_with_string(){
        ExpressionTree tree=ExpressionProcessor.toTree("'AA'*1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Multiply);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo("AA");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_multiply_with_int(){
        ExpressionTree tree=ExpressionProcessor.toTree("12*1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Multiply);
        assertThat(tree.getArgs().get(0).getConstant()).isEqualTo(12);
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_multiplu_with_function(){
        ExpressionTree tree=ExpressionProcessor.toTree("_plus(1,1)*1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Multiply);
        assertThat(tree.getArgs().get(0).toExpression()).isEqualTo("_plus(1,1)");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_multiply_with_this(){
        ExpressionTree tree=ExpressionProcessor.toTree("_this*1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Multiply);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("_this");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }
    @Test
    public void should_construct_expression_tree_with_multiply_with_variable(){
        ExpressionTree tree=ExpressionProcessor.toTree("Var*1");
        assertThat(tree.rootProcessor).isEqualTo(Operation.Multiply);
        assertThat(tree.getArgs().get(0).getVariable()).isEqualTo("Var");
        assertThat(tree.getArgs().get(1).getConstant()).isEqualTo(1);
    }

    @Test
    public void should_return_string(){
        String op=(String)ExpressionProcessor.process("'AAAA'",new HashMap<>());
        assertThat(op).isEqualTo("AAAA");
    }
    @Test
    public void should_return_integer(){
        Integer op=(Integer)ExpressionProcessor.process("32",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(32));
    }
    @Test
    public void should_add_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3 + 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(5));
    }
    @Test
    public void should_substract_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3 - 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(1));
    }
    @Test
    public void should_add_multiple_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3+2+3",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(8));
    }
    @Test
    public void should_multiply_integer(){
        Integer op=(Integer)ExpressionProcessor.process("3 * 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(6));
    }
    @Test
    public void should_divide_integer(){
        Integer op=(Integer)ExpressionProcessor.process("6 / 2",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(3));
    }
    @Test
    public void should_get_lenth_of_string(){
        Integer op=(Integer)ExpressionProcessor.process("_len('AAAA')",new HashMap<>());
        assertThat(op).isEqualTo(Integer.valueOf(4));
    }
    @Test
    public void should_get_timestamp(){
        Long op=(Long)ExpressionProcessor.process("_timestamp",new HashMap<>());
        assertThat(op).isInstanceOf(Long.class);
    }
    @Test
    public void should_eval_expression(){
        String op=(String)ExpressionProcessor.process("_eval('#')",new HashMap<>());
        assertThat(op).isInstanceOf(String.class);
        assertThat(op.length()).isEqualTo(1);
        assertThat(Character.isDigit(op.charAt(0))).isTrue();

    }
    @Test
    public void should_get_last_from_string(){
        String op=(String)ExpressionProcessor.process("_last('ABC',2)",new HashMap<>());
        assertThat(op).isEqualTo("BC");

    }
    @Test
    public void should_get_first_from_string(){
        String op=(String)ExpressionProcessor.process("_first('ABC',2)",new HashMap<>());
        assertThat(op).isEqualTo("AB");

    }
    @Test
    public void should_get_mid_from_string(){
        String op=(String)ExpressionProcessor.process("_mid('ABCDEF',2,2)",new HashMap<>());
        assertThat(op).isEqualTo("CD");

    }
    @Test
    public void should_process_stack(){
        String op=(String)ExpressionProcessor.process("_mid('ABCDEF',_len('ABCDEF')-4,2)",new HashMap<>());
        assertThat(op).isEqualTo("CD");

    }
    @Test
    public void should_process_concat() {
        String op = (String) ExpressionProcessor.process("_concat(3,_timestamp,_eval('1###'))", new HashMap<>());
        assertThat(op.length()).isEqualTo(Integer.valueOf(18));
    }
    @Test
    public void should_get_pattern_value(){
        Object value=ExpressionProcessor.process("_eval('####')",new HashMap<>());
//        assertThat(value).isEqualTo("1234");
    }
    @Test
    public void should_get_first_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_first(data,4)",data);
        assertThat(value).isEqualTo("ABCD");
    }
    @Test
    public void should_get_mid_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_mid(data,4,4)",data);
        assertThat(value).isEqualTo("1234");
    }
    @Test
    public void should_get_last_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_last(data,4)",data);
        assertThat(value).isEqualTo("!@#$");
    }
    @Test
    public void should_get_lenth_value(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_len(data)",data);
        assertThat(value).isEqualTo(Integer.valueOf(12));
    }
    @Test
    public void should_do_plus(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_plus(3,4)",data);
        assertThat(value).isEqualTo(Integer.valueOf(7));
    }
    @Test
    public void should_do_minus(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","ABCD1234!@#$");
        Object value=ExpressionProcessor.process("_minus(4,3)",data);
        assertThat(value).isEqualTo(Integer.valueOf(1));
    }
    @Test
    public void should_do_composite(){
        HashMap<String,Object> data=new HashMap<>();
        data.put("data","CDE-F67690200320021");
        Object value=ExpressionProcessor.process("_first(data,_len(data)-4))",data);
        assertThat(value).isEqualTo("CDE-F6769020032");
    }
    @Test
    public void should_do_now(){
        Object value=ExpressionProcessor.process("_now",new HashMap<>());
        assertThat(value).isInstanceOf(Date.class);
    }
    @Test
    public void should_format_date(){
        Date date=new Date();
        Object value=ExpressionProcessor.process("_format(_now,'dd-MM')",new HashMap<>());
        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo(String.format("%02d",date.getDate())+"-"+String.format("%02d",date.getMonth()+1));
    }
    @Test
    public void should_add_day_to_date(){
        Date date=new Date();
        Object value=ExpressionProcessor.process("_format(_add_days(_now,1),'dd-MM')",new HashMap<>());
        assertThat(value).isInstanceOf(String.class);
        assertThat(value).isEqualTo(String.format("%02d",date.getDate()+1)+"-"+String.format("%02d",date.getMonth()+1));
    }

    //Short Hands
    @Test
    public void should_process_shorthand(){
        ExpressionProcessor.addShortHand("sscc",ExpressionProcessor.toTree("_concat(3,_timestamp,_eval('####'))"));
        Object data=ExpressionProcessor.process("_sscc",new HashMap<>());
        assertThat(data).isInstanceOf(String.class);
        assertThat(((String)data).length()).isEqualTo(18);
    }

    //Get Val

    @Test
    public void should_return_empty_string_when_empty_string(){
        String result=ExpressionProcessor.getVal("");
        assertThat(result).isEqualTo("");
    }

    @Test
    public void should_return_between_numbers(){
        String result=ExpressionProcessor.getVal("(10,100)");
        assertThat(Integer.parseInt(result)).isBetween(10,100);
    }

    @Test
    public void should_return_amoung_set(){
        String result=ExpressionProcessor.getVal("[10,20,100]");
        assertThat(result).isIn(Arrays.asList("10","20","100"));
    }
}
