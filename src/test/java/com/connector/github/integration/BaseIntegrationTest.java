package com.connector.github.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.okJson;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import com.connector.github.BaseTest;
import com.connector.github.client.dto.GitHubBranch;
import com.connector.github.client.dto.GitHubRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

@IntegrationTest
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "40000")
@AutoConfigureWireMock(port = 0)
@DirtiesContext
@TestPropertySource(properties = {
    "integration.github.url=http://localhost:${wiremock.server.port}/github"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class BaseIntegrationTest extends BaseTest {

  protected static final ObjectMapper MAPPER = new ObjectMapper();

  protected void mockRepositories(String username, List<GitHubRepository> repositories) {
    try {
      stubFor(get(urlEqualTo("/github/users/%s/repos".formatted(username)))
          .willReturn(okJson(MAPPER.writeValueAsString(repositories))));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  protected void mockRepositoriesNotFound(String username) {
    mockFailedResponse("/github/users/%s/repos".formatted(username), "Repository not found", 404);
  }

  protected void mockRepositoriesInternalServer(String username) {
    mockFailedResponse("/github/users/%s/repos".formatted(username), "Internal server error", 500);
  }

  protected void mockBranches(String username, String repository, List<GitHubBranch> branches) {
    try {
      stubFor(get(urlEqualTo("/github/repos/%s/%s/branches".formatted(username, repository)))
          .willReturn(okJson(MAPPER.writeValueAsString(branches))));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  protected void mockRepositoriesNotFound(String username, String repository) {
    mockFailedResponse("/github/repos/%s/%s/branches".formatted(username, repository), "Branches not found", 404);
  }

  private void mockFailedResponse(String testUrl, String message, Integer status) {
    var errorResponse = """
        "message":"%s",
        "documentation_url":"https://docs.com/test.url"
        """
        .formatted(message);

    try {
      stubFor(get(urlEqualTo(testUrl))
          .willReturn(aResponse().withStatus(status)
              .withHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
              .withBody(errorResponse)));
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }


}
