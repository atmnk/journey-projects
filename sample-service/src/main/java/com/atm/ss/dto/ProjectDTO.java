package com.atm.ss.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProjectDTO {
    String name;
    List<StreamDTO> streams;
}
