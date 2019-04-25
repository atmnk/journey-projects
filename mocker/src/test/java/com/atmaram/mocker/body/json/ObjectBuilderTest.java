package com.atmaram.mocker.body.json;

import com.atmaram.mocker.data.DataPool;
import org.junit.Test;

import java.util.ArrayList;

import static com.atmaram.mocker.body.json.JsonBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ObjectBuilderTest {
//    @Test
//    public void should_build_regex_with_variable() throws Exception {
//        JsonBuilder objectBuilder=object(pair("id",e("$id").save("id"))).save("foo");
//        assertThat(objectBuilder.toRegex()).isEqualTo("[\\s]*\\{[\\s]*\"id\"[\\s]*:[\\s]*(?<id>.*)[\\s]*[\\s]*\\}[\\s]*");
//        DataPool dataPool=new DataPool();
//        objectBuilder.gerpBody(dataPool,new ArrayList<>(),"{\"id\":1}");
//        assertThat(dataPool.getData("id")).isEqualTo("1");
//    }
}
