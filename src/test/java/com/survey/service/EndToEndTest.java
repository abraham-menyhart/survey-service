package com.survey.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.survey.service.dto.MemberPointsDto;
import com.survey.service.dto.SurveyStatisticsDto;
import com.survey.service.model.Member;
import com.survey.service.model.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
class EndToEndTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void getCompletedRespondents_shouldReturnMembers_whenSurveyHasCompletedParticipations() throws Exception {
        //given - Using test survey data from test-surveys.csv
        Long surveyId = 1L;

        //when
        String response = mockMvc.perform(get("/api/surveys/{surveyId}/completed-respondents", surveyId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then - Validates members who completed Survey 1 (from test-participation.csv: John Doe and Jane Smith)
        Member[] members = objectMapper.readValue(response, Member[].class);
        assertThat(members).hasSize(2);
        assertThat(members).extracting(Member::fullName).containsExactlyInAnyOrder("John Doe", "Jane Smith");
    }

    @Test
    void getInvitableMembers_shouldReturnActiveNonParticipatedMembers_whenSurveyExists() throws Exception {
        //given - Using test survey data from test-surveys.csv
        Long surveyId = 1L;

        //when
        String response = mockMvc.perform(get("/api/surveys/{surveyId}/invitable-members", surveyId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then - Validates active members who haven't participated in Survey 1 (Alice Johnson only)
        Member[] members = objectMapper.readValue(response, Member[].class);
        assertThat(members).hasSize(1);
        assertThat(members[0].fullName()).isEqualTo("Alice Johnson");
        assertThat(members[0].isActive()).isTrue();
    }

    @Test
    void getCompletedSurveys_shouldReturnSurveys_whenMemberHasCompletedSurveys() throws Exception {
        //given - Using test member data from test-members.csv
        Long memberId = 1L;

        //when
        String response = mockMvc.perform(get("/api/members/{memberId}/completed-surveys", memberId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then -
        Survey[] surveys = objectMapper.readValue(response, Survey[].class);
        assertThat(surveys).hasSize(1);
        assertThat(surveys[0].name()).isEqualTo("Test Survey 1");
    }

    @Test
    void getMemberPoints_shouldReturnPoints_whenMemberHasEarnedPoints() throws Exception {
        //given - Using test member data from test-members.csv
        Long memberId = 1L;

        //when
        String response = mockMvc.perform(get("/api/members/{memberId}/points", memberId))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then - Validates John Doe's points (completed Survey 1: 5 points, filtered Survey 2: 3 points)
        MemberPointsDto[] points = objectMapper.readValue(response, MemberPointsDto[].class);
        assertThat(points).hasSize(2);
        assertThat(points).extracting(MemberPointsDto::points).containsExactlyInAnyOrder(5, 3);
    }

    @Test
    void getSurveyStatistics_shouldReturnStatistics_whenSurveysHaveParticipations() throws Exception {
        //given - Using test survey data from test-surveys.csv

        //when
        String response = mockMvc.perform(get("/api/surveys/statistics"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        SurveyStatisticsDto[] statistics = objectMapper.readValue(response, SurveyStatisticsDto[].class);
        assertThat(statistics).hasSize(3);
        assertThat(statistics).extracting(SurveyStatisticsDto::surveyName)
                .containsExactlyInAnyOrder("Test Survey 1", "Test Survey 2", "Test Survey 3");
    }
}