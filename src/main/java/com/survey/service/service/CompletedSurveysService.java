package com.survey.service.service;

import com.survey.service.enums.ParticipationStatus;
import com.survey.service.model.Participation;
import com.survey.service.model.Survey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompletedSurveysService {

    private final DataLoaderService dataLoaderService;

    public CompletedSurveysService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public List<Survey> fetchCompletedSurveysByMemberId(Long memberId) {
        List<Participation> memberParticipations = getMemberParticipations(memberId);

        if (memberParticipations.isEmpty()) {
            return List.of();
        }

        return getCompletedSurveys(memberParticipations);
    }

    private List<Participation> getMemberParticipations(Long memberId) {
        List<Participation> participations = dataLoaderService.getParticipationsByMemberId().get(memberId);
        return participations != null ? participations : List.of();
    }

    private List<Survey> getCompletedSurveys(List<Participation> participations) {
        Map<Long, Survey> surveysById = dataLoaderService.getSurveysById();

        return participations.stream()
                .filter(this::isCompletedParticipation)
                .map(p -> surveysById.get(p.surveyId()))
                .toList();
    }

    private boolean isCompletedParticipation(Participation participation) {
        return ParticipationStatus.fromId(participation.statusId()).isCompleted();
    }
}