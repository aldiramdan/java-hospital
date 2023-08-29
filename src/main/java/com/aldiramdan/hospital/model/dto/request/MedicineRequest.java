package com.aldiramdan.hospital.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicineRequest {
    @NotBlank(message = "name is required")
    @Size(min = 3, max = 50, message="name must have \\{{min}\\} - \\{{max}\\} characters")
    @Pattern(regexp = "^[a-zA-Z\s]+$", message = "name must be alphabet")
    private String name;

    @NotNull(message = "price is required")
    @PositiveOrZero(message = "price must be positive")
    private double price;
}
