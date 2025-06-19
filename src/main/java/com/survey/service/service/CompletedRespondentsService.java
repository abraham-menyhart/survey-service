package com.survey.service.service;

import com.survey.service.enums.ParticipationStatus;
import com.survey.service.model.Member;
import com.survey.service.model.Participation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CompletedRespondentsService {

    private final DataLoaderService dataLoaderService;

    public CompletedRespondentsService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public List<Member> fetchCompletedRespondentsBySurveyId(Long surveyId) {
        List<Participation> surveyParticipations = getSurveyParticipations(surveyId);
        if (surveyParticipations.isEmpty()) {
            return List.of();
        }

        return getCompletedRespondents(surveyParticipations);
    }

    private List<Participation> getSurveyParticipations(Long surveyId) {
        List<Participation> participations = dataLoaderService.getParticipationsBySurveyId().get(surveyId);
        return participations != null ? participations : List.of();
    }

    private List<Member> getCompletedRespondents(List<Participation> participations) {
        Map<Long, Member> membersById = dataLoaderService.getMembersById();

        return participations.stream()
                .filter(this::isCompletedParticipation)
                .map(p -> membersById.get(p.memberId()))
                .toList();
    }

    private boolean isCompletedParticipation(Participation participation) {
        return ParticipationStatus.fromId(participation.statusId()).isCompleted();
    }
}