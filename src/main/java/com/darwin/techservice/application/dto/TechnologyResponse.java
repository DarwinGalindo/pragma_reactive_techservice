package com.darwin.techservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TechnologyResponse {
    private Long id;
    private String name;
    private String description;
}
