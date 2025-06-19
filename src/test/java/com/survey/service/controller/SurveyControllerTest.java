package com.survey.service.controller;

import com.survey.service.dto.SurveyStatisticsDto;
import com.survey.service.model.Member;
import com.survey.service.service.CompletedRespondentsService;
import com.survey.service.service.InvitableMembersService;
import com.survey.service.service.SurveyAnalyticsStatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SurveyController.class)
class SurveyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompletedRespondentsService completedRespondentsService;

    @MockBean
    private InvitableMembersService invitableMembersService;

    @MockBean
    private SurveyAnalyticsStatisticsService surveyAnalyticsStatisticsService;

    private Member testMember;
    private SurveyStatisticsDto testStatistics;

    @BeforeEach
    void setUp() {
        testMember = new Member(1L, "John Doe", "john@example.com", true);
        testStatistics = new SurveyStatisticsDto(1L, "Test Survey", 10, 5, 2, 15.5);
    }

    @Test
    void getCompletedRespondents_shouldReturnOk_whenValidSurveyId() throws Exception {
        //given
        Long surveyId = 1L;
        when(completedRespondentsService.fetchCompletedRespondentsBySurveyId(eq(surveyId)))
                .thenReturn(List.of(testMember));

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/completed-respondents", surveyId))
                .andExpect(status().isOk());
    }

    @Test
    void getCompletedRespondents_shouldReturnOk_whenNoRespondents() throws Exception {
        //given
        Long surveyId = 999L;
        when(completedRespondentsService.fetchCompletedRespondentsBySurveyId(eq(surveyId)))
                .thenReturn(List.of());

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/completed-respondents", surveyId))
                .andExpect(status().isOk());
    }

    @Test
    void getCompletedRespondents_shouldReturnBadRequest_whenNegativeSurveyId() throws Exception {
        //given
        Long surveyId = -1L;

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/completed-respondents", surveyId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Survey ID must be a positive number"));
    }

    @Test
    void getCompletedRespondents_shouldReturnBadRequest_whenZeroSurveyId() throws Exception {
        //given
        Long surveyId = 0L;

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/completed-respondents", surveyId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Survey ID must be a positive number"));
    }

    @Test
    void getInvitableMembers_shouldReturnOk_whenValidSurveyId() throws Exception {
        //given
        Long surveyId = 1L;
        when(invitableMembersService.fetchInvitableMembersForSurvey(eq(surveyId)))
                .thenReturn(List.of(testMember));

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/invitable-members", surveyId))
                .andExpect(status().isOk());
    }

    @Test
    void getInvitableMembers_shouldReturnOk_whenNoInvitableMembers() throws Exception {
        //given
        Long surveyId = 999L;
        when(invitableMembersService.fetchInvitableMembersForSurvey(eq(surveyId)))
                .thenReturn(List.of());

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/invitable-members", surveyId))
                .andExpect(status().isOk());
    }

    @Test
    void getInvitableMembers_shouldReturnBadRequest_whenNegativeSurveyId() throws Exception {
        //given
        Long surveyId = -1L;

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/invitable-members", surveyId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Survey ID must be a positive number"));
    }

    @Test
    void getInvitableMembers_shouldReturnBadRequest_whenZeroSurveyId() throws Exception {
        //given
        Long surveyId = 0L;

        //when & then
        mockMvc.perform(get("/api/surveys/{surveyId}/invitable-members", surveyId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Survey ID must be a positive number"));
    }

    @Test
    void getSurveyStatistics_shouldReturnOk_whenStatisticsExist() throws Exception {
        //given
        when(surveyAnalyticsStatisticsService.fetchSurveyStatistics())
                .thenReturn(List.of(testStatistics));

        //when & then
        mockMvc.perform(get("/api/surveys/statistics"))
                .andExpect(status().isOk());
    }

    @Test
    void getSurveyStatistics_shouldReturnOk_whenNoStatistics() throws Exception {
        //given
        when(surveyAnalyticsStatisticsService.fetchSurveyStatistics())
                .thenReturn(List.of());

        //when & then
        mockMvc.perform(get("/api/surveys/statistics"))
                .andExpect(status().isOk());
    }
}