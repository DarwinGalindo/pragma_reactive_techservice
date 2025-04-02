package com.darwin.techservice.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import static com.darwin.techservice.application.util.Constants.*;

@Getter
@Setter
@AllArgsConstructor
public class TechnologyRequest {
    @NotBlank(message = TECHNOLOGY_NAME_NOT_BLANK)
    @Size(max = 50, message = TECHNOLOGY_NAME_SIZE)
    private String name;

    @NotBlank(message = TECHNOLOGY_DESCRIPTION_NOT_BLANK)
    @Size(max = 90, message = TECHNOLOGY_DESCRIPTION_SIZE)
    private String description;
}
