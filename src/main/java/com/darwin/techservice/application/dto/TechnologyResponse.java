package com.darwin.techservice.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TechnologyResponse {
    private Long id;
    private String name;
    private String description;
}
