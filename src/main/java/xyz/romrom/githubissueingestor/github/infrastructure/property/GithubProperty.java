package xyz.romrom.githubissueingestor.github.infrastructure.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "github")
public record GithubProperty(
  String token,
  String baseUrl
) {

}
