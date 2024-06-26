package com.connector.github.api;

import com.connector.github.dto.RepositoryData;
import com.connector.github.service.GitHubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class GitHubController implements GitHubControllerApi {

  private final GitHubService gitHubService;

  @Override
  public Mono<ResponseEntity<Flux<RepositoryData>>> getRepositories(
      @PathVariable("username") String username, ServerWebExchange exchange) {

    return Mono.just(ResponseEntity.ok(gitHubService.getRepositories(username)));
  }
}
