package com.connector.github.config.property;

import jakarta.validation.constraints.NotBlank;

public record GitHubConfig(
    @NotBlank String url
) {

}
