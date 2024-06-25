package com.connector.github.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubBranch(
    String name,
    GitHubCommit commit
) {

}
