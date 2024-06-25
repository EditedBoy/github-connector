package com.connector.github.integration.client;

import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;

import com.connector.github.client.GitHubWebClient;
import com.connector.github.client.dto.GitHubBranch;
import com.connector.github.client.dto.GitHubRepository;
import com.connector.github.exception.NotFoundException;
import com.connector.github.integration.BaseIntegrationTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class GitHubWebClientTest extends BaseIntegrationTest {

  @Autowired
  private GitHubWebClient gitHubWebClient;

  @Test
  public void getRepositories() {
    var repository1 = buildGitHubRepository(OWNER, REPOSITORY_1, true);
    var repository2 = buildGitHubRepository(OWNER, REPOSITORY_2, false);

    mockRepositories(OWNER, List.of(repository1, repository2));

    Flux<GitHubRepository> repositories = gitHubWebClient.getRepositories(OWNER);

    StepVerifier.create(repositories)
        .expectNextMatches(repo -> repo.name().equals(REPOSITORY_2))
        .verifyComplete();

    verify(getRequestedFor(urlPathEqualTo("/github/users/%s/repos".formatted(OWNER))));
  }

  @Test
  public void getRepositoriesWhenNotFound() {
    mockRepositoriesNotFound(OWNER);

    Flux<GitHubRepository> repositories = gitHubWebClient.getRepositories(OWNER);

    StepVerifier.create(repositories)
        .expectErrorMatches(throwable -> throwable instanceof NotFoundException
            && ((NotFoundException) throwable).getHttpStatus() == HttpStatus.NOT_FOUND
            && throwable.getMessage().equals("Not found repositories for username: %s".formatted(OWNER)))
        .verify();

    verify(getRequestedFor(urlPathEqualTo("/github/users/%s/repos".formatted(OWNER))));
  }

  @Test
  public void getRepositoriesWhenInternalServer() {
    mockRepositoriesInternalServer(OWNER);

    Flux<GitHubRepository> repositories = gitHubWebClient.getRepositories(OWNER);

    StepVerifier.create(repositories)
        .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException
            && ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR)
        .verify();

    verify(getRequestedFor(urlPathEqualTo("/github/users/%s/repos".formatted(OWNER))));
  }


  @Test
  public void getBranches() {
    var branchMain = buildGitHubBranch(BRANCH_MAIN, COMMIT_SHA_MAIN);
    var branchTest = buildGitHubBranch(BRANCH_TEST, COMMIT_SHA_TEST);

    mockBranches(OWNER, REPOSITORY_1, List.of(branchMain, branchTest));

    Flux<GitHubBranch> branches = gitHubWebClient.getBranches(OWNER, REPOSITORY_1);

    StepVerifier.create(branches)
        .expectNextMatches(branch -> branch.name().equals(BRANCH_MAIN))
        .expectNextMatches(branch -> branch.name().equals(BRANCH_TEST))
        .verifyComplete();

    verify(getRequestedFor(urlPathEqualTo("/github/repos/%s/%s/branches".formatted(OWNER, REPOSITORY_1))));
  }

  @Test
  public void testGetBranches_NotFound() {
    mockRepositoriesNotFound(OWNER, REPOSITORY_1);

    Flux<GitHubBranch> branches = gitHubWebClient.getBranches(OWNER, REPOSITORY_1);

    StepVerifier.create(branches)
        .expectErrorMatches(throwable -> throwable instanceof WebClientResponseException
            && ((WebClientResponseException) throwable).getStatusCode() == HttpStatus.NOT_FOUND)
        .verify();

    verify(getRequestedFor(urlPathEqualTo("/github/repos/%s/%s/branches".formatted(OWNER, REPOSITORY_1))));
  }

}
