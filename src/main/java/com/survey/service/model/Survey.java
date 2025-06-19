package com.survey.service.model;

public record Survey(
        Long id,
        String name,
        Integer expectedCompletes,
        Integer completionPoints,
        Integer filteredPoints
) {
}