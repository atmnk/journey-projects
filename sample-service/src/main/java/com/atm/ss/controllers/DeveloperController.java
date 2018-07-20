package com.atm.ss.controllers;

import com.atm.ss.dto.DeveloperDTO;
import com.atm.ss.dto.ProjectDTO;
import com.atmaram.datagen.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/developer")
public class DeveloperController {
    @Autowired
    BeanFactory beanFactory;

    @GetMapping(path = "/{id}")
    public DeveloperDTO getDeveloper(@PathVariable(name = "id") Long id){
        DeveloperDTO developerDTO= null;
        try {
            developerDTO = beanFactory.create(DeveloperDTO.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return developerDTO;

    }
    @PostMapping(path = "/add")
    public DeveloperDTO saveDev(@RequestBody DeveloperDTO developerDTO){
        return developerDTO;

    }
}
