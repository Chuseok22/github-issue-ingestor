package xyz.romrom.githubissueingestor.ollama.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "ollama")
public record OllamaProperty(
  String baseUrl,
  String apiKey,
  String embeddingModel,
  int dimension
) {

}
