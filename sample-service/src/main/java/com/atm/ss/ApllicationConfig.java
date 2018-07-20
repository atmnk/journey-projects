package com.atm.ss;

import com.atm.ss.dto.DeveloperDTO;
import com.atm.ss.dto.ProjectDTO;
import com.atm.ss.dto.StreamDTO;
import com.atmaram.datagen.BeanFactory;
import com.atmaram.datagen.generators.DateExpressionGenerator;
import com.atmaram.datagen.generators.Generator;
import com.atmaram.datagen.generators.ListGenerator;
import com.atmaram.datagen.generators.StringFormattedDateGenerator;
import com.atmaram.datagen.generators.custom.NameGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
public class ApllicationConfig {
    @Bean
    public BeanFactory beanFactory(){
        List<Character> allowedChars=new ArrayList<>();
        for(char i=97;i<=122;i++){
            allowedChars.add(new Character(i));
        }

        BeanFactory beanFactory=new BeanFactory();
        NameGenerator nameGenerator=new NameGenerator(3,10);
        beanFactory.apply(new StringFormattedDateGenerator("yyyy-MM-dd",new DateExpressionGenerator("yyyy-MM-dd","{1975-01-01,1990-12-31}"))).on(DeveloperDTO.class).getDateOfBirth();
        beanFactory.apply(new ListGenerator(2,10)).on(StreamDTO.class).getDevelopers();
        beanFactory.apply(new ListGenerator(1,10)).on(ProjectDTO.class).getStreams();
        beanFactory.apply(nameGenerator).on(DeveloperDTO.class).getName();
        beanFactory.apply(nameGenerator).on(ProjectDTO.class).getName();
        beanFactory.apply(nameGenerator).on(StreamDTO.class).getName();
        return beanFactory;
    }
}
