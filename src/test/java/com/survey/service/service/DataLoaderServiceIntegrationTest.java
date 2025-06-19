package com.survey.service.service;

import com.survey.service.model.Member;
import com.survey.service.model.Participation;
import com.survey.service.model.Survey;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "data.members.file=classpath:data/test-members.csv",
        "data.surveys.file=classpath:data/test-surveys.csv",
        "data.statuses.file=classpath:data/test-statuses.csv",
        "data.participation.file=classpath:data/test-participation.csv"
})
class DataLoaderServiceIntegrationTest {

    @Autowired
    private DataLoaderService dataLoaderService;

    @Test
    void loadData_shouldLoadAllEntitiesSuccessfully_whenValidCsvFiles() {
        //given - test CSV files are loaded automatically via @PostConstruct

        //when
        Map<Long, Member> members = dataLoaderService.getMembersById();
        Map<Long, Survey> surveys = dataLoaderService.getSurveysById();
        Map<Long, List<Participation>> participationsByMember = dataLoaderService.getParticipationsByMemberId();
        Map<Long, List<Participation>> participationsBySurvey = dataLoaderService.getParticipationsBySurveyId();

        //then
        assertThat(members).hasSize(4);
        assertThat(surveys).hasSize(3);
        assertThat(participationsByMember).hasSize(4);
        assertThat(participationsBySurvey).hasSize(3);
    }

    @Test
    void loadData_shouldLoadMembersCorrectly_whenValidCsvFile() {
        //given - test CSV files are loaded automatically via @PostConstruct

        //when
        Map<Long, Member> members = dataLoaderService.getMembersById();

        //then
        assertThat(members)
                .containsEntry(1L, new Member(1L, "John Doe", "john.doe@example.com", true))
                .containsEntry(2L, new Member(2L, "Jane Smith", "jane.smith@example.com", true))
                .containsEntry(3L, new Member(3L, "Bob Wilson", "bob.wilson@example.com", false))
                .containsEntry(4L, new Member(4L, "Alice Johnson", "alice.johnson@example.com", true));
    }

    @Test
    void loadData_shouldLoadSurveysCorrectly_whenValidCsvFile() {
        //given - test CSV files are loaded automatically via @PostConstruct

        //when
        Map<Long, Survey> surveys = dataLoaderService.getSurveysById();

        //then
        assertThat(surveys)
                .containsEntry(1L, new Survey(1L, "Test Survey 1", 10, 5, 2))
                .containsEntry(2L, new Survey(2L, "Test Survey 2", 20, 10, 3))
                .containsEntry(3L, new Survey(3L, "Test Survey 3", 15, 8, 1));
    }


    @Test
    void loadData_shouldLoadParticipationsCorrectly_whenValidCsvFile() {
        //given - test CSV files are loaded automatically via @PostConstruct

        //when
        Map<Long, List<Participation>> participationsByMember = dataLoaderService.getParticipationsByMemberId();
        Map<Long, List<Participation>> participationsBySurvey = dataLoaderService.getParticipationsBySurveyId();

        //then
        // Test participations by member
        List<Participation> member1Participations = participationsByMember.get(1L);
        assertThat(member1Participations).hasSize(2)
                .contains(
                        new Participation(1L, 1L, 4L, 15),
                        new Participation(1L, 2L, 3L, null)
                );

        List<Participation> member2Participations = participationsByMember.get(2L);
        assertThat(member2Participations).hasSize(2)
                .contains(
                        new Participation(2L, 1L, 4L, 20),
                        new Participation(2L, 3L, 2L, null)
                );

        // Test participations by survey
        List<Participation> survey1Participations = participationsBySurvey.get(1L);
        assertThat(survey1Participations).hasSize(3)
                .contains(
                        new Participation(1L, 1L, 4L, 15),
                        new Participation(2L, 1L, 4L, 20),
                        new Participation(3L, 1L, 1L, null)
                );
    }

    @Test
    void loadData_shouldHandleNullLengthValues_whenParticipationHasEmptyLength() {
        //given - test CSV files are loaded automatically via @PostConstruct

        //when
        Map<Long, List<Participation>> participationsByMember = dataLoaderService.getParticipationsByMemberId();

        //then
        List<Participation> member1Participations = participationsByMember.get(1L);
        Participation participationWithNullLength = member1Participations.stream()
                .filter(p -> p.surveyId().equals(2L))
                .findFirst()
                .orElseThrow();

        assertThat(participationWithNullLength.length()).isNull();
    }

    @Test
    void loadData_shouldHandleValidLengthValues_whenParticipationHasLength() {
        //given - test CSV files are loaded automatically via @PostConstruct

        //when
        Map<Long, List<Participation>> participationsByMember = dataLoaderService.getParticipationsByMemberId();

        //then
        List<Participation> member1Participations = participationsByMember.get(1L);
        Participation participationWithLength = member1Participations.stream()
                .filter(p -> p.surveyId().equals(1L))
                .findFirst()
                .orElseThrow();

        assertThat(participationWithLength.length()).isEqualTo(15);
    }

}