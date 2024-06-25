# GitHub Repository and Branch Fetcher
This project is a Spring Boot application that uses WebFlux to fetch repository and branch information from the GitHub API. It demonstrates unit testing, integration testing with WireMock, and continuous integration using Jenkins.

## Table of Contents
- [Overview](#overview) 
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Setup and Installation](#setup-and-installation)
- [Running the Application](#running-the-application)
- [Swgger API](#swagger)
- [Testing](#testing)
- [License](#license)

## Overview
This application provides endpoints to fetch repository information and their respective branches for a given GitHub username. It uses WebFlux for reactive programming and integrates with the GitHub API.

## Features
- Fetch repositories for a given GitHub username.
- Fetch branches for a specific repository.
- Log response bodies and errors.
- Handle WebClient exceptions.
- Unit and integration tests.
- Continuous integration with Jenkins.
- Dockerization for container deployment.

## Technologies Used
- Java 17
- Spring Boot 3
- Spring WebFlux
- WireMock for integration tests
- JUnit 5 for testing
- Mockito for mocking
- Jenkins for CI/CD
- Docker for containerization
- Gradle for build automation

## Setup and Installation

### Prerequisites
- Java 17
- Gradle
- Docker (for containerization)
- Jenkins (for CI/CD)

### Clone the Repository
```bash
git clone https://github.com/EditedBoy/github-connector.git
cd github-connector
```

### Build the Project
```bash
./gradlew clean build
```

## Running the Application

### Local Environment
To run the application locally, you can use the Spring Boot Gradle plugin:
```bash
./gradlew bootRun
```

### Docker
To build and run the Docker image:
```bash
docker build -t github-connector .
docker run -p 8080:8080 github-connector
```

## Swagger
Swagger UI is available to visualize and interact with the API's resources.

After running the application, access Swagger UI at:
```bash
http://localhost:8080/swagger-ui.html
```
or
```bash
http://localhost:8080/webjars/swagger-ui/index.html
```

API docs JSON payload can be fetched from:
```bash
http://localhost:8080/v3/api-docs
```

## Testing

### Unit Tests
Unit tests are written using JUnit 5 and Mockito. You can run the tests with:
```bash
./gradlew test
```

### Integration Tests
Integration tests use WireMock to mock the GitHub API. These can be run with:
```bash
./gradlew integrationTest
```

## License
This project is licensed under the MIT License. See the LICENSE file for details.
