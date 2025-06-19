package com.survey.service.service;

import com.survey.service.exception.SurveyNotFoundException;
import com.survey.service.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InvitableMembersServiceTest extends BaseServiceTest {

    private InvitableMembersService invitableMembersService;

    @BeforeEach
    void setUp() {
        invitableMembersService = new InvitableMembersService(dataLoaderService);
    }

    @Test
    void fetchInvitableMembersForSurvey_shouldReturnActiveNonParticipatedMembers_whenSurveyExists() {
        //given
        Long surveyId = 1L;

        //when
        List<Member> result = invitableMembersService.fetchInvitableMembersForSurvey(surveyId);

        //then
        assertThat(result).hasSize(1)
                .extracting(Member::id)
                .containsExactly(4L);
    }

    @Test
    void fetchInvitableMembersForSurvey_shouldReturnActiveNonParticipatedMembers_whenSurveyHasLimitedParticipations() {
        //given
        Long surveyId = 3L; // Survey 3 has participation from member 2 only

        //when
        List<Member> result = invitableMembersService.fetchInvitableMembersForSurvey(surveyId);

        //then
        assertThat(result).hasSize(2)
                .extracting(Member::id)
                .containsExactlyInAnyOrder(1L, 4L);
    }

    @Test
    void fetchInvitableMembersForSurvey_shouldReturnAllActiveMembers_whenSurveyHasNoParticipations() {
        //given
        Long surveyId = 4L; // Survey 4 exists but has no participations

        //when
        List<Member> result = invitableMembersService.fetchInvitableMembersForSurvey(surveyId);

        //then
        assertThat(result).hasSize(3)
                .extracting(Member::id)
                .containsExactlyInAnyOrder(1L, 2L, 4L);
    }

    @Test
    void fetchInvitableMembersForSurvey_shouldThrow_whenSurveyNotFound() {
        //given
        Long nonExistentSurveyId = 999L;

        //when & then
        assertThatThrownBy(() -> invitableMembersService.fetchInvitableMembersForSurvey(nonExistentSurveyId))
                .isInstanceOf(SurveyNotFoundException.class)
                .hasMessage("Survey with ID 999 not found");
    }

    @Test
    void fetchInvitableMembersForSurvey_shouldExcludeInactiveMembers_whenCheckingInvitability() {
        //given
        Long surveyId = 2L;

        //when
        List<Member> result = invitableMembersService.fetchInvitableMembersForSurvey(surveyId);

        //then
        assertThat(result).hasSize(2)
                .extracting(Member::id)
                .containsExactlyInAnyOrder(2L, 4L);
        assertThat(result).extracting(Member::isActive)
                .containsOnly(true);
    }
}