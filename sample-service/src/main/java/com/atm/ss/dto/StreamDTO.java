package com.atm.ss.dto;

import lombok.Data;

import java.util.List;

@Data
public class StreamDTO {
    String name;
    List<DeveloperDTO> developers;
}
