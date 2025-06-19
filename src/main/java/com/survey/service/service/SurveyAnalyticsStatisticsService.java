package com.survey.service.service;

import com.survey.service.dto.SurveyStatisticsDto;
import com.survey.service.enums.ParticipationStatus;
import com.survey.service.model.Participation;
import com.survey.service.model.Survey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SurveyAnalyticsStatisticsService {

    private final DataLoaderService dataLoaderService;

    public SurveyAnalyticsStatisticsService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public List<SurveyStatisticsDto> fetchSurveyStatistics() {
        Map<Long, Survey> surveysById = dataLoaderService.getSurveysById();
        Map<Long, List<Participation>> participationsBySurvey = dataLoaderService.getParticipationsBySurveyId();

        return surveysById.values().stream()
                .map(survey -> calculateStatisticsForSurvey(survey, getParticipationsForSurvey(survey.id(), participationsBySurvey)))
                .toList();
    }

    private SurveyStatisticsDto calculateStatisticsForSurvey(Survey survey, List<Participation> participations) {
        StatusCounts statusCounts = calculateStatusCounts(participations);
        double averageLength = calculateAverageLength(participations);

        return new SurveyStatisticsDto(
                survey.id(),
                survey.name(),
                statusCounts.completes(),
                statusCounts.filtered(),
                statusCounts.rejected(),
                averageLength
        );
    }

    private List<Participation> getParticipationsForSurvey(Long surveyId,
                                                           Map<Long, List<Participation>> participationsBySurvey) {
        return participationsBySurvey.getOrDefault(surveyId, List.of());
    }

    private StatusCounts calculateStatusCounts(List<Participation> participations) {
        int completes = 0, filtered = 0, rejected = 0;

        for (Participation participation : participations) {
            switch (ParticipationStatus.fromId(participation.statusId())) {
                case COMPLETED -> completes++;
                case FILTERED -> filtered++;
                case REJECTED -> rejected++;
                default -> { /* ignore NOT_ASKED and other statuses */ }
            }
        }

        return new StatusCounts(completes, filtered, rejected);
    }

    private double calculateAverageLength(List<Participation> participations) {
        int lengthSum = 0;
        int lengthCount = 0;

        for (Participation participation : participations) {
            if (participation.length() != null) {
                lengthSum += participation.length();
                lengthCount++;
            }
        }

        return lengthCount > 0 ? (double) lengthSum / lengthCount : 0.0;
    }

    private record StatusCounts(int completes, int filtered, int rejected) {
    }
}