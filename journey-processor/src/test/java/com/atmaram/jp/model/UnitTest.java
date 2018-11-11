package com.atmaram.jp.model;

import com.atmaram.jp.ValueStore;
import com.atmaram.jp.VariableStore;
import com.atmaram.jp.exceptions.UnitConfigurationException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

public class UnitTest extends Unit{
    private final ByteArrayOutputStream outContent=new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();


    @Override
    public void eval(VariableStore variableStore) throws UnitConfigurationException {
        return;
    }

    @Override
    public ValueStore execute(ValueStore valueStore, int index) {
        return null;
    }

    @Override
    public Unit fill(ValueStore valueStore) {
        return null;
    }
    @Before
    public void setOutput(){
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }
    @Test
    public void should_print_statments_with_index_number_of_tabs(){
        this.print(2,"Hello");
        assertThat(outContent.toString()).isEqualTo("\t\tHello\n");
    }
    @Test
    public void should_print_start_execute_index_number_of_tabs(){
        this.name="Unit 1";
        this.printStartExecute(2);
        assertThat(outContent.toString()).isEqualTo("\t\tStarting Execution of Unit: Unit 1\n");
    }
    @Test
    public void should_print_done_execute_index_number_of_tabs(){
        this.name="Unit 1";
        this.printDoneExecute(2);
        assertThat(outContent.toString()).isEqualTo("\t\tDone Execution of Unit: Unit 1\n");
    }

}
