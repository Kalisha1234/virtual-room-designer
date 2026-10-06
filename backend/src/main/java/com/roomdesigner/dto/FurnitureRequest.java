package com.roomdesigner.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record FurnitureRequest(
    @NotBlank String catalogId,
    @NotNull @Min(0) Double x,
    @NotNull @Min(0) Double y,
    @Min(0) @Max(359) Integer rotation,
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$") String color
) {}
