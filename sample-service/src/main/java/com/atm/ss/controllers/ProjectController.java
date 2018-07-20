package com.atm.ss.controllers;
import com.atm.ss.dto.ProjectDTO;
import com.atmaram.datagen.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/project")
public class ProjectController {
    @Autowired
    BeanFactory beanFactory;

    @GetMapping(path = "/{id}")
    public ProjectDTO getProject(@PathVariable(name = "id") Long id){
        ProjectDTO projectDTO= null;
        try {
            projectDTO = beanFactory.create(ProjectDTO.class);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return projectDTO;

    }
    @PostMapping(path = "/add")
    public ProjectDTO saveProject(@RequestBody ProjectDTO projectDTO){
        return projectDTO;

    }
}
