package com.atmaram.tp.common;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class OperationTest {
    //ADD
    @Test
    public void should_add_two_integers(){
        List data=new ArrayList();
        data.add(2);
        data.add(3);
        assertThat(Operation.ADD.toValue(data)).isEqualTo(5);
    }
    @Test
    public void should_add_two_integers_represented_as_string(){
        List data=new ArrayList();
        data.add("2");
        data.add("3");
        assertThat(Operation.ADD.toValue(data)).isEqualTo(5);
    }

    //DAY
    @Test
    public void should_get_todays_day(){

        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        assertThat(Operation.DAY.toValue(data)).isEqualTo(1);
    }
    //MONTH
    @Test
    public void should_get_todays_month(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        assertThat(Operation.MONTH.toValue(data)).isEqualTo(2);
    }
    //YEAR
    @Test
    public void should_get_todays_year(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        assertThat(Operation.YEAR.toValue(data)).isEqualTo(2011);
    }

    //FORMAT_DATE
    @Test
    public void should_format_date(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        data.add("yyyy-MM-dd");
        assertThat(Operation.FORMAT_DATE.toValue(data)).isEqualTo("2011-02-01");
    }

    //NOW
    @Test
    public void should_return_current_date(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        data.add("yyyy-MM-dd");
        assertThat(Operation.NOW.toValue(data)).isEqualTo(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
    }
    //ADD_MONTHS
    @Test
    public void should_add_months(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        data.add(1);
        calendar.add(Calendar.MONTH,1);
        assertThat(Operation.ADD_MONTHS.toValue(data)).isEqualTo(calendar.getTime());
    }
    @Test
    public void should_add_months_if_arg_string(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        data.add("1");
        calendar.add(Calendar.MONTH,1);
        assertThat(Operation.ADD_MONTHS.toValue(data)).isEqualTo(calendar.getTime());
    }

    //ADD_YEARS
    @Test
    public void should_add_years(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        data.add(1);
        calendar.add(Calendar.YEAR,1);
        assertThat(Operation.ADD_YEAR.toValue(data)).isEqualTo(calendar.getTime());
    }
    @Test
    public void should_add_years_if_arg_string(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        data.add("1");
        calendar.add(Calendar.YEAR,1);
        assertThat(Operation.ADD_YEAR.toValue(data)).isEqualTo(calendar.getTime());
    }
    //ADD_DAYS
    @Test
    public void should_add_days(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        data.add(367);
        calendar.add(Calendar.DATE,367);
        assertThat(Operation.ADD_DAYS.toValue(data)).isEqualTo(calendar.getTime());
    }
    @Test
    public void should_add_days_if_arg_string(){
        List data=new ArrayList();
        Calendar calendar= Calendar.getInstance();
        calendar.set(2011,1,1);
        data.add(calendar.getTime());
        data.add("367");
        calendar.add(Calendar.DATE,367);
        assertThat(Operation.ADD_DAYS.toValue(data)).isEqualTo(calendar.getTime());
    }

    //CONCAT
    @Test
    public void should_concat_two_string(){
        List data=new ArrayList();
        data.add("Hello");
        data.add("World");
        assertThat(Operation.CONCAT.toValue(data)).isEqualTo("HelloWorld");
    }

    //SUBSTRACT
    @Test
    public void should_substract_two_integers(){
        List data=new ArrayList();
        data.add(3);
        data.add(2);
        assertThat(Operation.SUBSTRACT.toValue(data)).isEqualTo(1);
    }

    //UPPER
    @Test
    public void should_convert_string_to_upper_case(){
        List data=new ArrayList();
        data.add("hello");
        assertThat(Operation.UPPER.toValue(data)).isEqualTo("HELLO");
    }

    //LOWER
    @Test
    public void should_convert_string_to_lower_case(){
        List data=new ArrayList();
        data.add("HELLO");
        assertThat(Operation.LOWER.toValue(data)).isEqualTo("hello");
    }

    //RANDOM
    @Test
    public void should_return_random_number_between_range_if_both_same(){
        List data=new ArrayList();
        data.add(1);
        data.add(1);
        assertThat(Operation.RANDOM.toValue(data)).isEqualTo(1);
    }
    @Test
    public void should_return_random_number_between_range(){
        List data=new ArrayList();
        data.add(1);
        data.add(2);
        assertThat((Integer) Operation.RANDOM.toValue(data)).isBetween(1,2);
    }
}
