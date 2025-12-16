package xyz.romrom.githubissueingestor.qdrant.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "qdrant")
public record QdrantProperties(
  String baseUrl,
  String apiKey,
  String collection
) {

}
