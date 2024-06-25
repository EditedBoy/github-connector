package com.connector.github.config.property;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "integration")
public record ClientProperties(
    @NotNull GitHubConfig gitHub
) {

}
