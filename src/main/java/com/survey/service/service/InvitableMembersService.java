package com.survey.service.service;

import com.survey.service.model.Member;
import com.survey.service.model.Participation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InvitableMembersService {

    private final DataLoaderService dataLoaderService;

    public InvitableMembersService(DataLoaderService dataLoaderService) {
        this.dataLoaderService = dataLoaderService;
    }

    public List<Member> fetchInvitableMembersForSurvey(Long surveyId) {
        Map<Long, Member> membersById = dataLoaderService.getMembersById();
        Set<Long> participatedMemberIds = getParticipatedMemberIds(surveyId);

        return getInvitableMembers(membersById, participatedMemberIds);
    }

    private Set<Long> getParticipatedMemberIds(Long surveyId) {
        List<Participation> surveyParticipations = dataLoaderService.getParticipationsBySurveyId().get(surveyId);

        return surveyParticipations != null
                ? surveyParticipations.stream().map(Participation::memberId).collect(Collectors.toSet())
                : Set.of();
    }

    private List<Member> getInvitableMembers(Map<Long, Member> membersById, Set<Long> participatedMemberIds) {
        return membersById.values().stream()
                .filter(Member::isActive)
                .filter(member -> !participatedMemberIds.contains(member.id()))
                .toList();
    }
}