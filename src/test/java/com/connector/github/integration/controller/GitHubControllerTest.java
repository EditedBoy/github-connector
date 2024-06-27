package com.connector.github.integration.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.connector.github.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

public class GitHubControllerTest extends BaseIntegrationTest {

  @Autowired
  private WebTestClient webTestClient;

  @Test
  void getRepositories() {
    var repository1 = buildGitHubRepository(OWNER, REPOSITORY_1, false);
    var repository2 = buildGitHubRepository(OWNER, REPOSITORY_2, true);
    var repository3 = buildGitHubRepository(OWNER, REPOSITORY_3, false);

    var branchMain = buildGitHubBranch(BRANCH_MAIN, COMMIT_SHA_MAIN);
    var branchTest = buildGitHubBranch(BRANCH_TEST, COMMIT_SHA_TEST);

    mockRepositories(OWNER, List.of(repository1, repository2, repository3));
    mockBranches(OWNER, REPOSITORY_1, List.of(branchMain));
    mockBranches(OWNER, REPOSITORY_2, List.of(branchMain));
    mockBranches(OWNER, REPOSITORY_3, List.of(branchMain, branchTest));

    webTestClient.get()
        .uri("/api/v1/users/{username}/repositories", OWNER)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.size()").isEqualTo(2)

        .jsonPath("$[?(@.owner == '%s')]", OWNER).exists()
        .jsonPath("$[?(@.name == '%s')]", REPOSITORY_1).exists()
        .jsonPath("$[?(@.name == '%s')].branches[?(@.name == '%s')]", REPOSITORY_1, BRANCH_MAIN).exists()
        .jsonPath("$[?(@.name == '%s')].branches[?(@.name == '%s')].commit.sha", REPOSITORY_1, BRANCH_MAIN).isEqualTo(COMMIT_SHA_MAIN)

        .jsonPath("$[?(@.owner == '%s')]", OWNER).exists()
        .jsonPath("$[?(@.name == '%s')]", REPOSITORY_3).exists()
        .jsonPath("$[?(@.name == '%s')].branches[?(@.name == '%s')]", REPOSITORY_3, BRANCH_MAIN).exists()
        .jsonPath("$[?(@.name == '%s')].branches[?(@.name == '%s')].commit.sha", REPOSITORY_3, BRANCH_MAIN).isEqualTo(COMMIT_SHA_MAIN)
        .jsonPath("$[?(@.name == '%s')].branches[?(@.name == '%s')]", REPOSITORY_3, BRANCH_TEST).exists()
        .jsonPath("$[?(@.name == '%s')].branches[?(@.name == '%s')].commit.sha", REPOSITORY_3, BRANCH_TEST).isEqualTo(COMMIT_SHA_TEST);
  }

  @Test
  void getRepositoriesWhenNotSupportedAcceptHeader() {
    webTestClient.get()
        .uri("/api/v1/users/{username}/repositories", OWNER)
        .accept(MediaType.APPLICATION_XML)
        .exchange()
        .expectBody()
        .jsonPath("$.status").isEqualTo(406)
        .jsonPath("$.message").isEqualTo("Could not find acceptable representation");
  }

  @Test
  void getRepositoriesWhenGitHubUserNotExist() {
    mockRepositoriesNotFound(OWNER);
    webTestClient.get()
        .uri("/api/v1/users/{username}/repositories", OWNER)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectBody()
        .jsonPath("$.status").isEqualTo(404)
        .jsonPath("$.message").isEqualTo("Not found repositories for username: " + OWNER);
  }

  @Test
  void getRepositoriesWhenInternalServerOccurred() {
    mockRepositoriesInternalServer(OWNER);
    webTestClient.get()
        .uri("/api/v1/users/{username}/repositories", OWNER)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectBody()
        .jsonPath("$.status").isEqualTo(500)
        .jsonPath("$.message").value(message -> assertTrue(message.toString().contains("Internal server message detail")));
  }
}
