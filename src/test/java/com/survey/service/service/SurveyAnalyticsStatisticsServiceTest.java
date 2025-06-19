package com.survey.service.service;

import com.survey.service.dto.SurveyStatisticsDto;
import com.survey.service.model.Participation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

class SurveyAnalyticsStatisticsServiceTest extends BaseServiceTest {

    private SurveyAnalyticsStatisticsService surveyAnalyticsStatisticsService;

    @BeforeEach
    void setUp() {
        surveyAnalyticsStatisticsService = new SurveyAnalyticsStatisticsService(dataLoaderService);
    }

    @Test
    void fetchSurveyStatistics_shouldReturnCorrectStatistics_whenSurveysHaveParticipations() {
        //given

        //when
        List<SurveyStatisticsDto> result = surveyAnalyticsStatisticsService.fetchSurveyStatistics();

        //then
        assertThat(result).hasSize(4);

        SurveyStatisticsDto survey1Stats = findStatsBySurveyId(result, 1L);
        assertThat(survey1Stats.surveyId()).isEqualTo(1L);
        assertThat(survey1Stats.surveyName()).isEqualTo("Survey 1");
        assertThat(survey1Stats.numberOfCompletes()).isEqualTo(2);
        assertThat(survey1Stats.numberOfFiltered()).isEqualTo(0);
        assertThat(survey1Stats.numberOfRejected()).isEqualTo(0);
        assertThat(survey1Stats.averageLength()).isEqualTo(17.5);
    }

    @Test
    void fetchSurveyStatistics_shouldReturnZeroStatistics_whenSurveyHasNoParticipations() {
        //given
        Map<Long, List<Participation>> emptyParticipations = Map.of();
        lenient().when(dataLoaderService.getParticipationsBySurveyId()).thenReturn(emptyParticipations);

        //when
        List<SurveyStatisticsDto> result = surveyAnalyticsStatisticsService.fetchSurveyStatistics();

        //then
        assertThat(result).hasSize(4);
        assertThat(result).allMatch(stats ->
                stats.numberOfCompletes() == 0 &&
                        stats.numberOfFiltered() == 0 &&
                        stats.numberOfRejected() == 0 &&
                        stats.averageLength() == 0.0
        );
    }

    @Test
    void fetchSurveyStatistics_shouldHandleNullLengthValues_whenCalculatingAverageLength() {
        //given
        Map<Long, List<Participation>> participationsWithNulls = Map.of(
                2L, List.of(new Participation(1L, 2L, 3L, null))
        );
        lenient().when(dataLoaderService.getParticipationsBySurveyId()).thenReturn(participationsWithNulls);

        //when
        List<SurveyStatisticsDto> result = surveyAnalyticsStatisticsService.fetchSurveyStatistics();

        //then
        SurveyStatisticsDto survey2Stats = findStatsBySurveyId(result, 2L);
        assertThat(survey2Stats.averageLength()).isEqualTo(0.0);
    }

    private SurveyStatisticsDto findStatsBySurveyId(List<SurveyStatisticsDto> stats, Long surveyId) {
        return stats.stream()
                .filter(s -> s.surveyId().equals(surveyId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Survey stats not found for ID: " + surveyId));
    }
}