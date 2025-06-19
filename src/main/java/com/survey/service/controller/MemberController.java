package com.survey.service.controller;

import com.survey.service.dto.MemberPointsDto;
import com.survey.service.model.Survey;
import com.survey.service.service.CompletedSurveysService;
import com.survey.service.service.MemberPointsService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/members")
@Validated
public class MemberController {

    private final CompletedSurveysService completedSurveysService;
    private final MemberPointsService memberPointsService;

    public MemberController(CompletedSurveysService completedSurveysService,
                            MemberPointsService memberPointsService) {
        this.completedSurveysService = completedSurveysService;
        this.memberPointsService = memberPointsService;
    }

    @GetMapping("/{memberId}/completed-surveys")
    public ResponseEntity<List<Survey>> getCompletedSurveys(
            @PathVariable @Positive(message = "Member ID must be a positive number") Long memberId) {
        List<Survey> surveys = completedSurveysService.fetchCompletedSurveysByMemberId(memberId);
        return ResponseEntity.ok(surveys);
    }

    @GetMapping("/{memberId}/points")
    public ResponseEntity<List<MemberPointsDto>> getMemberPoints(
            @PathVariable @Positive(message = "Member ID must be a positive number") Long memberId) {
        List<MemberPointsDto> points = memberPointsService.fetchMemberPoints(memberId);
        return ResponseEntity.ok(points);
    }
}