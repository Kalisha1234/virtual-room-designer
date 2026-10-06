package com.roomdesigner.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RoomRequest(
    @NotBlank @Size(max = 80) String name,
    @DecimalMin("2.0") @DecimalMax("30.0") double width,
    @DecimalMin("2.0") @DecimalMax("30.0") double length,
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$") String floorColor,
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$") String wallColor
) {}
