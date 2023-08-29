package com.aldiramdan.hospital.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRequest {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 125, message="name must have \\{{min}\\} - \\{{max}\\} characters")
    @Pattern(regexp = "^[a-zA-Z\s]+$", message = "name must be alphabet")
    private String name;

    @NotNull(message = "age is required")
    @PositiveOrZero(message = "age must be positive")
    private int age;

    @NotBlank(message = "address is required")
    private String address;
}
