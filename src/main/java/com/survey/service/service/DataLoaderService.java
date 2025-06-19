package com.survey.service.service;

import com.survey.service.model.Member;
import com.survey.service.model.Participation;
import com.survey.service.model.Status;
import com.survey.service.model.Survey;
import jakarta.annotation.PostConstruct;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class DataLoaderService {

    private static final Logger logger = LoggerFactory.getLogger(DataLoaderService.class);

    private final ResourceLoader resourceLoader;

    @Value("${data.members.file}")
    private String membersFilePath;

    @Value("${data.surveys.file}")
    private String surveysFilePath;


    @Value("${data.participation.file}")
    private String participationFilePath;

    public DataLoaderService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    // Data storage maps
    private final Map<Long, Member> membersById = new HashMap<>();
    private final Map<Long, Survey> surveysById = new HashMap<>();
    private final Map<Long, List<Participation>> participationsByMemberId = new HashMap<>();
    private final Map<Long, List<Participation>> participationsBySurveyId = new HashMap<>();

    @PostConstruct
    public void loadData() {
        logger.info("Starting data loading from CSV files...");

        loadMembers();
        loadSurveys();
        loadParticipations();

        logger.info("Data loading completed. Loaded {} members, {} surveys, {} participations",
                membersById.size(), surveysById.size(),
                participationsByMemberId.values().stream().mapToInt(List::size).sum());
    }

    private void loadMembers() {
        loadCsvData(membersFilePath, "members", this::parseMemberRecord, membersById::put);
    }

    private void loadSurveys() {
        loadCsvData(surveysFilePath, "surveys", this::parseSurveyRecord, surveysById::put);
    }


    private void loadParticipations() {
        loadCsvData(participationFilePath, "participations", this::parseParticipationRecord, this::storeParticipation);
    }

    private <T> void loadCsvData(String filePath, String entityType, Function<CSVRecord, T> parser, BiConsumer<Long, T> storer) {
        try {
            Resource resource = resourceLoader.getResource(filePath);
            try (InputStreamReader reader = new InputStreamReader(resource.getInputStream());
                 CSVParser csvParser = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader)) {

                int loadedCount = 0;
                int skippedCount = 0;

                for (CSVRecord record : csvParser) {
                    try {
                        T entity = parser.apply(record);
                        Long id = extractId(entity);
                        storer.accept(id, entity);
                        loadedCount++;
                    } catch (Exception e) {
                        logger.warn("Skipping invalid {} record at line {}: {}",
                                entityType, record.getRecordNumber(), e.getMessage());
                        skippedCount++;
                    }
                }

                logger.info("Loaded {} {}, skipped {} invalid records", loadedCount, entityType, skippedCount);
            }
        } catch (IOException e) {
            logger.error("Error loading {} from file: {}", entityType, filePath, e);
            throw new RuntimeException("Failed to load " + entityType + " data", e);
        }
    }

    private Member parseMemberRecord(CSVRecord record) {
        Long id = Long.parseLong(record.get("Member Id"));
        String fullName = record.get("Full name");
        String email = record.get("E-mail address");
        Boolean isActive = "1".equals(record.get("Is Active"));
        return new Member(id, fullName, email, isActive);
    }

    private Survey parseSurveyRecord(CSVRecord record) {
        Long id = Long.parseLong(record.get("Survey Id"));
        String name = record.get("Name");
        Integer expectedCompletes = Integer.parseInt(record.get("Expected completes"));
        Integer completionPoints = Integer.parseInt(record.get("Completion points"));
        Integer filteredPoints = Integer.parseInt(record.get("Filtered points"));
        return new Survey(id, name, expectedCompletes, completionPoints, filteredPoints);
    }


    private Participation parseParticipationRecord(CSVRecord record) {
        Long memberId = Long.parseLong(record.get("Member Id"));
        Long surveyId = Long.parseLong(record.get("Survey Id"));
        Long statusId = Long.parseLong(record.get("Status"));

        Integer length = parseLength(record.get("Length"));

        return new Participation(memberId, surveyId, statusId, length);
    }

    private Integer parseLength(String lengthStr) {
        if (lengthStr != null && !lengthStr.trim().isEmpty()) {
            return Integer.parseInt(lengthStr);
        }
        return null;
    }

    private Long extractId(Object entity) {
        return switch (entity) {
            case Member member -> member.id();
            case Survey survey -> survey.id();
            case Status status -> status.id();
            case Participation participation -> participation.memberId();
            default -> throw new IllegalArgumentException("Unknown entity type: " + entity.getClass());
        };
    }

    private void storeParticipation(Long id, Participation participation) {
        participationsByMemberId.computeIfAbsent(participation.memberId(), k -> new ArrayList<>()).add(participation);
        participationsBySurveyId.computeIfAbsent(participation.surveyId(), k -> new ArrayList<>()).add(participation);
    }

    // Getter methods for accessing data
    public Map<Long, Member> getMembersById() {
        return Map.copyOf(membersById);
    }

    public Map<Long, Survey> getSurveysById() {
        return Map.copyOf(surveysById);
    }


    public Map<Long, List<Participation>> getParticipationsByMemberId() {
        return Map.copyOf(participationsByMemberId);
    }

    public Map<Long, List<Participation>> getParticipationsBySurveyId() {
        return Map.copyOf(participationsBySurveyId);
    }
}