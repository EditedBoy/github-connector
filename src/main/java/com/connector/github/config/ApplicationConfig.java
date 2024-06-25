package com.connector.github.config;

import com.connector.github.config.property.ClientProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@EnableConfigurationProperties({ClientProperties.class})
@Configuration
public class ApplicationConfig {

  @Bean
  public WebClient gitHubClient(ClientProperties properties) {
    return WebClient.builder()
        .baseUrl(properties.gitHub().url())
        .build();
  }
}
