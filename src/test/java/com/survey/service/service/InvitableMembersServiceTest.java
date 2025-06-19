package com.survey.service.service;

import com.survey.service.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void fetchInvitableMembersForSurvey_shouldReturnAllActiveMembers_whenSurveyHasNoParticipations() {
        //given
        Long surveyId = 999L;

        //when
        List<Member> result = invitableMembersService.fetchInvitableMembersForSurvey(surveyId);

        //then
        assertThat(result).hasSize(3)
                .extracting(Member::id)
                .containsExactlyInAnyOrder(1L, 2L, 4L);
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