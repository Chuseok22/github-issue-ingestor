package xyz.romrom.githubissueingestor.common.infrastructure.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "text-chunk")
public record TextChunkerProperties(
  int maxChars,
  int overlapChars,
  int hardMaxChars
) {

}
