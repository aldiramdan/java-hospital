package com.aldiramdan.hospital.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DoctorRequest {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 125, message="name must have \\{{min}\\} - \\{{max}\\} characters")
    private String name;

    @NotBlank(message = "specialization is required")
    @Size(min = 3, max = 30, message="Key must have \\{{min}\\} - \\{{max}\\} characters")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "specialization must be alphabet")
    private String specialization;

    @NotNull(message = "consultationFee is required")
    @PositiveOrZero(message = "consultationFee must be positive")
    private double consultationFee;
}
