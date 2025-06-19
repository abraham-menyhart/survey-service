package com.survey.service.model;

public record Member(
        Long id,
        String fullName,
        String email,
        Boolean isActive
) {
}