package xyz.romrom.githubissueingestor.qdrant.infrastructure.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "qdrant")
public record QdrantProperty(
  String baseUrl,
  String apiKey,
  String collection
) {

}
