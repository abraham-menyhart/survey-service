package com.survey.service.controller;

import com.survey.service.dto.MemberPointsDto;
import com.survey.service.model.Survey;
import com.survey.service.service.CompletedSurveysService;
import com.survey.service.service.MemberPointsService;
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

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompletedSurveysService completedSurveysService;

    @MockBean
    private MemberPointsService memberPointsService;


    private Survey testSurvey;
    private MemberPointsDto testPoints;

    @BeforeEach
    void setUp() {
        testSurvey = new Survey(1L, "Test Survey", 100, 50, 25);
        testPoints = new MemberPointsDto(1L, 50);
    }

    @Test
    void getCompletedSurveys_shouldReturnOk_whenValidMemberId() throws Exception {
        //given
        Long memberId = 1L;
        when(completedSurveysService.fetchCompletedSurveysByMemberId(eq(memberId)))
                .thenReturn(List.of(testSurvey));

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/completed-surveys", memberId))
                .andExpect(status().isOk());
    }

    @Test
    void getCompletedSurveys_shouldReturnOk_whenNoCompletedSurveys() throws Exception {
        //given
        Long memberId = 999L;
        when(completedSurveysService.fetchCompletedSurveysByMemberId(eq(memberId)))
                .thenReturn(List.of());

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/completed-surveys", memberId))
                .andExpect(status().isOk());
    }

    @Test
    void getCompletedSurveys_shouldReturnBadRequest_whenNegativeMemberId() throws Exception {
        //given
        Long memberId = -1L;

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/completed-surveys", memberId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Member ID must be a positive number"));
    }

    @Test
    void getCompletedSurveys_shouldReturnBadRequest_whenZeroMemberId() throws Exception {
        //given
        Long memberId = 0L;

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/completed-surveys", memberId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Member ID must be a positive number"));
    }

    @Test
    void getMemberPoints_shouldReturnOk_whenValidMemberId() throws Exception {
        //given
        Long memberId = 1L;
        when(memberPointsService.fetchMemberPoints(eq(memberId)))
                .thenReturn(List.of(testPoints));

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/points", memberId))
                .andExpect(status().isOk());
    }

    @Test
    void getMemberPoints_shouldReturnOk_whenNoPoints() throws Exception {
        //given
        Long memberId = 999L;
        when(memberPointsService.fetchMemberPoints(eq(memberId)))
                .thenReturn(List.of());

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/points", memberId))
                .andExpect(status().isOk());
    }

    @Test
    void getMemberPoints_shouldReturnBadRequest_whenNegativeMemberId() throws Exception {
        //given
        Long memberId = -1L;

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/points", memberId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Member ID must be a positive number"));
    }

    @Test
    void getMemberPoints_shouldReturnBadRequest_whenZeroMemberId() throws Exception {
        //given
        Long memberId = 0L;

        //when & then
        mockMvc.perform(get("/api/members/{memberId}/points", memberId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Member ID must be a positive number"));
    }
}