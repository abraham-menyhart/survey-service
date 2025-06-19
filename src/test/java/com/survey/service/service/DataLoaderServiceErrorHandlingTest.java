package com.survey.service.service;

import com.survey.service.model.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestPropertySource(properties = {
        "data.members.file=classpath:data/invalid-members.csv",
        "data.surveys.file=classpath:data/test-surveys.csv",
        "data.statuses.file=classpath:data/test-statuses.csv",
        "data.participation.file=classpath:data/test-participation.csv"
})
class DataLoaderServiceErrorHandlingTest {

    @Autowired
    private DataLoaderService dataLoaderService;

    @Test
    void loadData_shouldSkipInvalidRecords_whenCsvContainsInvalidData() {
        //given - invalid CSV files are loaded automatically via @PostConstruct

        //when
        Map<Long, Member> members = dataLoaderService.getMembersById();

        //then
        // Should load valid records (ID 1 and 3), skipping invalid ones (ID with invalid_id)
        assertThat(members).hasSize(2)
                .containsEntry(1L, new Member(1L, "John Doe", "john.doe@example.com", true))
                .containsEntry(3L, new Member(3L, "Bob Wilson", "bob.wilson@example.com", false));

        // Invalid record should be skipped (member with invalid ID)
        assertThat(members.values().stream().anyMatch(m -> m.fullName().equals("Jane Smith"))).isFalse();
    }
}