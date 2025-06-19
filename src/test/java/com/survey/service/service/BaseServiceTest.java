package com.survey.service.service;

import com.survey.service.enums.ParticipationStatus;
import com.survey.service.model.Member;
import com.survey.service.model.Participation;
import com.survey.service.model.Survey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public abstract class BaseServiceTest {

    @Mock
    protected DataLoaderService dataLoaderService;

    protected Map<Long, Member> testMembers;
    protected Map<Long, Survey> testSurveys;
    protected Map<Long, List<Participation>> testParticipationsByMember;
    protected Map<Long, List<Participation>> testParticipationsBySurvey;

    @BeforeEach
    void baseSetUp() {
        setupTestData();
        setupMocks();
    }

    protected void setupTestData() {
        testMembers = Map.of(
                1L, new Member(1L, "John Doe", "john@example.com", true),
                2L, new Member(2L, "Jane Smith", "jane@example.com", true),
                3L, new Member(3L, "Bob Wilson", "bob@example.com", false),
                4L, new Member(4L, "Alice Johnson", "alice@example.com", true)
        );

        testSurveys = Map.of(
                1L, new Survey(1L, "Survey 1", 10, 5, 2),
                2L, new Survey(2L, "Survey 2", 20, 10, 3),
                3L, new Survey(3L, "Survey 3", 15, 8, 1),
                4L, new Survey(4L, "Survey 4", 25, 12, 4)
        );

        testParticipationsByMember = Map.of(
                1L, List.of(
                        new Participation(1L, 1L, ParticipationStatus.COMPLETED.getId(), 15),
                        new Participation(1L, 2L, ParticipationStatus.FILTERED.getId(), null)
                ),
                2L, List.of(
                        new Participation(2L, 1L, ParticipationStatus.COMPLETED.getId(), 20),
                        new Participation(2L, 3L, ParticipationStatus.REJECTED.getId(), null)
                ),
                3L, List.of(
                        new Participation(3L, 1L, ParticipationStatus.NOT_ASKED.getId(), null)
                )
        );

        testParticipationsBySurvey = Map.of(
                1L, List.of(
                        new Participation(1L, 1L, ParticipationStatus.COMPLETED.getId(), 15),
                        new Participation(2L, 1L, ParticipationStatus.COMPLETED.getId(), 20),
                        new Participation(3L, 1L, ParticipationStatus.NOT_ASKED.getId(), null)
                ),
                2L, List.of(
                        new Participation(1L, 2L, ParticipationStatus.FILTERED.getId(), null)
                ),
                3L, List.of(
                        new Participation(2L, 3L, ParticipationStatus.REJECTED.getId(), null)
                )
        );
    }

    protected void setupMocks() {
        lenient().when(dataLoaderService.getMembersById()).thenReturn(testMembers);
        lenient().when(dataLoaderService.getSurveysById()).thenReturn(testSurveys);
        lenient().when(dataLoaderService.getParticipationsByMemberId()).thenReturn(testParticipationsByMember);
        lenient().when(dataLoaderService.getParticipationsBySurveyId()).thenReturn(testParticipationsBySurvey);
    }
}