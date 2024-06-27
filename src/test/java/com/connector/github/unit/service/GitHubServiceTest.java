package com.connector.github.unit.service;

import static org.mockito.Mockito.when;

import com.connector.github.dto.RepositoryData;
import com.connector.github.client.GitHubWebClient;
import com.connector.github.mapper.BranchMapperImpl;
import com.connector.github.mapper.CommitMapperImpl;
import com.connector.github.mapper.RepositoryMapper;
import com.connector.github.mapper.RepositoryMapperImpl;
import com.connector.github.service.GitHubService;
import com.connector.github.unit.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class GitHubServiceTest extends BaseUnitTest {

  @Mock
  private GitHubWebClient gitHubWebClient;

  @Spy
  private RepositoryMapper repositoryMapper = new RepositoryMapperImpl(new BranchMapperImpl(new CommitMapperImpl()));

  @InjectMocks
  private GitHubService gitHubService;

  @Test
  public void getRepositories() {
    var repository1 = buildGitHubRepository(OWNER, REPOSITORY_1, false);
    var repository2 = buildGitHubRepository(OWNER, REPOSITORY_2, false);

    var branchMain = buildGitHubBranch(BRANCH_MAIN, COMMIT_SHA_MAIN);
    var branchTest = buildGitHubBranch(BRANCH_TEST, COMMIT_SHA_TEST);

    when(gitHubWebClient.getRepositories(OWNER)).thenReturn(Flux.just(repository1, repository2));
    when(gitHubWebClient.getBranches(OWNER, REPOSITORY_1)).thenReturn(Flux.just(branchMain));
    when(gitHubWebClient.getBranches(OWNER, REPOSITORY_2)).thenReturn(Flux.just(branchMain, branchTest));
    when(repositoryMapper.toRepositoryData(repository1, List.of(branchMain))).thenCallRealMethod();
    when(repositoryMapper.toRepositoryData(repository2, List.of(branchMain, branchTest))).thenCallRealMethod();

    Flux<RepositoryData> repositories = gitHubService.getRepositories(OWNER);

    StepVerifier.create(repositories)
        .expectNextMatches(repo ->
            REPOSITORY_1.equals(repo.getName())
                && repo.getBranches().size() == 1
                && BRANCH_MAIN.equals(repo.getBranches().get(0).getName())
                && COMMIT_SHA_MAIN.equals(repo.getBranches().get(0).getCommit().getSha()))
        .expectNextMatches(repo ->
            REPOSITORY_2.equals(repo.getName())
                && repo.getBranches().size() == 2
                && BRANCH_MAIN.equals(repo.getBranches().get(0).getName())
                && COMMIT_SHA_MAIN.equals(repo.getBranches().get(0).getCommit().getSha())
                && BRANCH_TEST.equals(repo.getBranches().get(1).getName())
                && COMMIT_SHA_TEST.equals(repo.getBranches().get(1).getCommit().getSha()))
        .verifyComplete();
  }

  @Test
  void getBranchesWhenErrorOccurred() {
    var repository = buildGitHubRepository(OWNER, REPOSITORY_1, false);

    when(gitHubWebClient.getRepositories(OWNER)).thenReturn(Flux.just(repository));
    when(gitHubWebClient.getBranches(OWNER, REPOSITORY_1)).thenReturn(
        Flux.error(new WebClientResponseException(500, "Internal Server Error", null, null, null)));

    Flux<RepositoryData> repositories = gitHubService.getRepositories(OWNER);

    StepVerifier.create(repositories)
        .expectErrorMatches(throwable ->
            throwable instanceof WebClientResponseException && ((WebClientResponseException) throwable).getStatusCode().value() == 500)
        .verify();
  }

  @Test
  void getRepositoriesWhenNotFoundExceptionOccurred() {
    when(gitHubWebClient.getRepositories(OWNER)).thenReturn(Flux.error(new WebClientResponseException(404, "Not Found", null, null, null)));

    Flux<RepositoryData> repositories = gitHubService.getRepositories(OWNER);

    StepVerifier.create(repositories)
        .expectErrorMatches(throwable ->
            throwable instanceof WebClientResponseException && ((WebClientResponseException) throwable).getStatusCode().value() == 404)
        .verify();
  }

  @Test
  void getRepositoriesEmpty() {
    when(gitHubWebClient.getRepositories(OWNER)).thenReturn(Flux.empty());

    Flux<RepositoryData> repositories = gitHubService.getRepositories(OWNER);

    StepVerifier.create(repositories)
        .expectNextCount(0)
        .verifyComplete();
  }
}
