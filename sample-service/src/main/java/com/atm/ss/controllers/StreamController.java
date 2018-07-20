package com.atm.ss.controllers;

import com.atm.ss.dto.DeveloperDTO;
import com.atm.ss.dto.StreamDTO;
import com.atmaram.datagen.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/stream")
public class StreamController {
    @Autowired
    BeanFactory beanFactory;

    @GetMapping(path = "/{id}")
    public StreamDTO getStream(@PathVariable(name = "id") Long id){
        StreamDTO streamDTO= null;
        try {
            streamDTO = beanFactory.create(StreamDTO.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return streamDTO;

    }
    @PostMapping(path = "/add")
    public StreamDTO saveStream(@RequestBody StreamDTO streamDTO){
        return streamDTO;

    }
}
