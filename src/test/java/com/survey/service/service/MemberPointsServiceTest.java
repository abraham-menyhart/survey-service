package com.survey.service.service;

import com.survey.service.dto.MemberPointsDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class MemberPointsServiceTest extends BaseServiceTest {

    private MemberPointsService memberPointsService;

    @BeforeEach
    void setUp() {
        memberPointsService = new MemberPointsService(dataLoaderService);
    }

    @Test
    void fetchMemberPoints_shouldReturnPointsForCompletedAndFilteredSurveys_whenMemberHasEligibleParticipations() {
        //given
        Long memberId = 1L;

        //when
        List<MemberPointsDto> result = memberPointsService.fetchMemberPoints(memberId);

        //then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(MemberPointsDto::surveyId, MemberPointsDto::points)
                .containsExactlyInAnyOrder(
                        tuple(1L, 5),
                        tuple(2L, 3)
                );
    }

    @Test
    void fetchMemberPoints_shouldReturnEmptyList_whenMemberHasNoEligibleParticipations() {
        //given
        Long memberId = 3L;

        //when
        List<MemberPointsDto> result = memberPointsService.fetchMemberPoints(memberId);

        //then
        assertThat(result).isEmpty();
    }

    @Test
    void fetchMemberPoints_shouldReturnEmptyList_whenMemberDoesNotExist() {
        //given
        Long nonExistentMemberId = 999L;

        //when
        List<MemberPointsDto> result = memberPointsService.fetchMemberPoints(nonExistentMemberId);

        //then
        assertThat(result).isEmpty();
    }
}