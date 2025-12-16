package xyz.romrom.githubissueingestor.github.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github")
public record GithubProperties(
  String token,
  String baseUrl
) {

}
