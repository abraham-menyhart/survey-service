package com.survey.service.exception;

public class SurveyNotFoundException extends RuntimeException {
    public SurveyNotFoundException(String message) {
        super(message);
    }

    public SurveyNotFoundException(Long surveyId) {
        super("Survey with ID " + surveyId + " not found. Please check if the survey exists and try again.");
    }
}