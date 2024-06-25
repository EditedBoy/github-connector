package com.connector.github.client;

import com.connector.github.client.dto.GitHubBranch;
import com.connector.github.client.dto.GitHubRepository;
import com.connector.github.exception.NotFoundException;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
@Service
public class GitHubWebClient {

  private final WebClient webClient;

  public Flux<GitHubRepository> getRepositories(String username) {
    return webClient.get()
        .uri("/users/{username}/repos", username)
        .retrieve()
        .onStatus(status -> status.value() == 404, notFoundFunction("Not found repositories for username: %s".formatted(username)))
        .bodyToFlux(GitHubRepository.class)
        .filter(repository -> !repository.fork())
        .log()
        .doOnError(error -> log.error("Error fetching data: {}", error.getMessage()));
  }

  public Flux<GitHubBranch> getBranches(String owner, String repository) {
    return webClient.get()
        .uri("/repos/{owner}/{repository}/branches", owner, repository)
        .retrieve()
        .bodyToFlux(GitHubBranch.class)
        .log()
        .doOnError(error -> log.error("Error fetching data: {}", error.getMessage()));
  }

  private Function<ClientResponse, Mono<? extends Throwable>> notFoundFunction(String message) {
    return clientResponse -> Mono.just(new NotFoundException(message));
  }
}
