package com.aldiramdan.hospital.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseRequest {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 50, message="name must have \\{{min}\\} - \\{{max}\\} characters")
    private String name;

    @NotBlank(message = "medicine is required")
    @Size(min = 3, max = 50, message="name must have \\{{min}\\} - \\{{max}\\} characters")
    private String medicine;
}
