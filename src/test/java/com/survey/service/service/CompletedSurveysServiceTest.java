package com.survey.service.service;

import com.survey.service.model.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompletedSurveysServiceTest extends BaseServiceTest {

    private CompletedSurveysService completedSurveysService;

    @BeforeEach
    void setUp() {
        completedSurveysService = new CompletedSurveysService(dataLoaderService);
    }

    @Test
    void fetchCompletedSurveysByMemberId_shouldReturnCompletedSurveys_whenMemberHasCompletedSurveys() {
        //given
        Long memberId = 1L;

        //when
        List<Survey> result = completedSurveysService.fetchCompletedSurveysByMemberId(memberId);

        //then
        assertThat(result).hasSize(1)
                .extracting(Survey::id)
                .containsExactly(1L);
    }

    @Test
    void fetchCompletedSurveysByMemberId_shouldReturnEmptyList_whenMemberHasNoCompletedSurveys() {
        //given
        Long memberId = 3L;

        //when
        List<Survey> result = completedSurveysService.fetchCompletedSurveysByMemberId(memberId);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void fetchCompletedSurveysByMemberId_shouldReturnEmptyList_whenMemberDoesNotExist() {
        //given
        Long nonExistentMemberId = 999L;

        //when
        List<Survey> result = completedSurveysService.fetchCompletedSurveysByMemberId(nonExistentMemberId);

        //then
        assertThat(result).isEmpty();
    }
}