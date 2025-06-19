package com.survey.service.controller;

import com.survey.service.dto.SurveyStatisticsDto;
import com.survey.service.model.Member;
import com.survey.service.service.CompletedRespondentsService;
import com.survey.service.service.InvitableMembersService;
import com.survey.service.service.SurveyAnalyticsStatisticsService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@Validated
public class SurveyController {

    private final CompletedRespondentsService completedRespondentsService;
    private final InvitableMembersService invitableMembersService;
    private final SurveyAnalyticsStatisticsService surveyAnalyticsStatisticsService;

    public SurveyController(CompletedRespondentsService completedRespondentsService,
                            InvitableMembersService invitableMembersService,
                            SurveyAnalyticsStatisticsService surveyAnalyticsStatisticsService) {
        this.completedRespondentsService = completedRespondentsService;
        this.invitableMembersService = invitableMembersService;
        this.surveyAnalyticsStatisticsService = surveyAnalyticsStatisticsService;
    }

    @GetMapping("/{surveyId}/completed-respondents")
    public ResponseEntity<List<Member>> getCompletedRespondents(
            @PathVariable @Positive(message = "Survey ID must be a positive number") Long surveyId) {
        List<Member> respondents = completedRespondentsService.fetchCompletedRespondentsBySurveyId(surveyId);
        return ResponseEntity.ok(respondents);
    }

    @GetMapping("/{surveyId}/invitable-members")
    public ResponseEntity<List<Member>> getInvitableMembers(
            @PathVariable @Positive(message = "Survey ID must be a positive number") Long surveyId) {
        List<Member> members = invitableMembersService.fetchInvitableMembersForSurvey(surveyId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/statistics")
    public ResponseEntity<List<SurveyStatisticsDto>> getSurveyStatistics() {
        List<SurveyStatisticsDto> statistics = surveyAnalyticsStatisticsService.fetchSurveyStatistics();
        return ResponseEntity.ok(statistics);
    }

}