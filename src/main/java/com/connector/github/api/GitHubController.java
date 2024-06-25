package com.connector.github.api;

import com.connector.github.dto.RepositoryData;
import com.connector.github.service.GitHubService;
import java.time.Duration;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @GetMapping(
      value = "/api/v2/users/{username}/repositories",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<RepositoryData> getRepositoriesV2(
      @PathVariable("username") String username) {

    return gitHubService.getRepositories(username);
  }

  @GetMapping(
      value = "/api/v1/ping",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<String> ping() {
    return Flux.fromStream(IntStream.range(0, 10)
            .mapToObj(i -> "Health ping -> %s".formatted(i + 1)))
        .delayElements(Duration.ofMillis(1000));
  }
}
