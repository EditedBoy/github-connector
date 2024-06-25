package com.connector.github.service;

import com.connector.github.api.dto.RepositoryData;
import com.connector.github.client.GitHubWebClient;
import com.connector.github.client.dto.GitHubRepository;
import com.connector.github.mapper.RepositoryMapper;
import com.connector.github.utility.logging.annotation.Logging;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class GitHubService {

  private final GitHubWebClient gitHubWebClient;
  private final RepositoryMapper repositoryMapper;

  @Logging
  public Flux<RepositoryData> getRepositories(String username) {
    return gitHubWebClient.getRepositories(username)
        .flatMap(this::getBranches);
  }

  private Mono<RepositoryData> getBranches(GitHubRepository repository) {
    return gitHubWebClient.getBranches(repository.owner().login(), repository.name())
        .collectList()
        .map(branches -> repositoryMapper.toRepositoryData(repository, branches));
  }
}
