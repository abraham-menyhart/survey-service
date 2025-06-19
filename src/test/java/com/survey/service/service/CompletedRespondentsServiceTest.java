package com.survey.service.service;

import com.survey.service.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CompletedRespondentsServiceTest extends BaseServiceTest {

    private CompletedRespondentsService completedRespondentsService;

    @BeforeEach
    void setUp() {
        completedRespondentsService = new CompletedRespondentsService(dataLoaderService);
    }

    @Test
    void fetchCompletedRespondentsBySurveyId_shouldReturnCompletedMembers_whenSurveyHasCompletedParticipations() {
        //given
        Long surveyId = 1L;

        //when
        List<Member> result = completedRespondentsService.fetchCompletedRespondentsBySurveyId(surveyId);

        //then
        assertThat(result).hasSize(2)
                .extracting(Member::id)
                .containsExactlyInAnyOrder(1L, 2L);
    }

    @Test
    void fetchCompletedRespondentsBySurveyId_shouldReturnEmptyList_whenSurveyHasNoCompletedParticipations() {
        //given
        Long surveyId = 2L;

        //when
        List<Member> result = completedRespondentsService.fetchCompletedRespondentsBySurveyId(surveyId);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void fetchCompletedRespondentsBySurveyId_shouldReturnEmptyList_whenSurveyDoesNotExist() {
        //given
        Long nonExistentSurveyId = 999L;

        //when
        List<Member> result = completedRespondentsService.fetchCompletedRespondentsBySurveyId(nonExistentSurveyId);

        //then
        assertThat(result).isEmpty();
    }
}