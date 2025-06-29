# Survey Service

## Description

A Spring Boot application that provides REST API endpoints for survey analytics and member management. The service loads
member, survey, and participation data from CSV files and offers functionalities to:

- Fetch members who completed specific surveys
- Get surveys completed by specific members
- Calculate member points based on survey participation
- Find members eligible for survey invitations
- Generate survey statistics and analytics

## Tech Stack

- **Java 21** - Programming language
- **Spring Boot 3.2.2** - Application framework
- **Maven** - Dependency management and build tool
- **Apache Commons CSV** - CSV file parsing
- **Jakarta Bean Validation** - Request parameter validation
- **AssertJ** - Testing assertions
- **MockMvc** - Web layer testing

## How to Start the Application

### Prerequisites

- Java 21 or later
- Maven 3.6+

### Running the Application

```bash
# Using Maven
mvn spring-boot:run

# Or build and run JAR
mvn clean package
java -jar target/survey-service-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`


### Available Endpoints

- `GET /api/surveys/{surveyId}/completed-respondents` - Get members who completed a survey
- `GET /api/surveys/{surveyId}/invitable-members` - Get members eligible for survey invitation
- `GET /api/surveys/statistics` - Get survey analytics and statistics
- `GET /api/members/{memberId}/completed-surveys` - Get surveys completed by a member
- `GET /api/members/{memberId}/points` - Get points earned by a member

See `src/test/resources/http-examples/` folder for sample HTTP requests.

## Data Model

The service loads data from CSV files on startup:

- `members.csv` - Member information
- `surveys.csv` - Survey definitions
- `participation.csv` - Member participation records

### Design Decision: Status as Enum

The original requirements mentioned a `statuses.csv` file, but I implemented participation statuses as an enum (
`ParticipationStatus`) instead. This decision was made because:

- The statuses (Not Asked, Rejected, Filtered, Completed) represent a fixed set of values
- They have specific business logic associated with them (e.g., which statuses are eligible for points)
- An enum provides type safety and prevents invalid status values
- In a real-world scenario, I would recommend to business stakeholders that such fixed values should be implemented as
  enums rather than loaded from CSV files

## API Documentation

OpenAPI/Swagger documentation is available at `openapi.yaml` in the project root.
