package com.survey.service.service;

import com.survey.service.dto.MemberPointsDto;
import com.survey.service.enums.ParticipationStatus;
import com.survey.service.model.Participation;
import com.survey.service.model.Survey;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberPointsService {

    private final DataLoaderService dataLoaderService;

    public MemberPointsService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public List<MemberPointsDto> fetchMemberPoints(Long memberId) {
        List<Participation> memberParticipations = getMemberParticipations(memberId);

        if (memberParticipations.isEmpty()) {
            return List.of();
        }

        return calculateMemberPoints(memberParticipations);
    }

    private List<Participation> getMemberParticipations(Long memberId) {
        List<Participation> participations = dataLoaderService.getParticipationsByMemberId().get(memberId);
        return participations != null ? participations : List.of();
    }

    private List<MemberPointsDto> calculateMemberPoints(List<Participation> participations) {
        return participations.stream()
                .filter(this::isEligibleForPoints)
                .map(this::createMemberPointsDto)
                .toList();
    }

    private boolean isEligibleForPoints(Participation participation) {
        return ParticipationStatus.fromId(participation.statusId()).isEligibleForPoints();
    }

    private MemberPointsDto createMemberPointsDto(Participation participation) {
        Survey survey = getSurvey(participation.surveyId());
        Integer points = calculatePoints(participation, survey);

        return new MemberPointsDto(participation.surveyId(), points);
    }

    private Survey getSurvey(Long surveyId) {
        return dataLoaderService.getSurveysById().get(surveyId);
    }

    private Integer calculatePoints(Participation participation, Survey survey) {
        ParticipationStatus status = ParticipationStatus.fromId(participation.statusId());

        return status.isCompleted()
                ? survey.completionPoints()
                : survey.filteredPoints();
    }
}