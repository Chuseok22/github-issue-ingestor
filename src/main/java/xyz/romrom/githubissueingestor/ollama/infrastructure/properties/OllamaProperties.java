package xyz.romrom.githubissueingestor.ollama.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ollama")
public record OllamaProperties(
  String baseUrl,
  String apiKey,
  String embeddingModel,
  int dimension
) {

}
