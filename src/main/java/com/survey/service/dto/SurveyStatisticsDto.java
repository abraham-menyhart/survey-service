package com.survey.service.dto;

public record SurveyStatisticsDto(
        Long surveyId,
        String surveyName,
        Integer numberOfCompletes,
        Integer numberOfFiltered,
        Integer numberOfRejected,
        Double averageLength
) {
}