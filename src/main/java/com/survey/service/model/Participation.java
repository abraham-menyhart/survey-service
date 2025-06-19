package com.survey.service.model;

public record Participation(
        Long memberId,
        Long surveyId,
        Long statusId,
        Integer length
) {
}