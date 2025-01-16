package org.example.gym.ui.dto;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateGymRequest {

    @NotBlank(message = "Gym name cannot be empty")
    private String name;

    @NotBlank(message = "Street cannot be empty")
    private String street;

    @NotBlank(message = "City cannot be empty")
    private String city;

    @NotBlank(message = "Country cannot be empty")
    private String country;

    @Min(value = 1, message = "Gym number must be greater than or equal to 1")
    private int number;

    @NotNull
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private double latitude;

    @NotNull
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private double longitude;
}
