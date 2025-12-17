package xyz.romrom.githubissueingestor.common.infrastructure.properties;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "text-chunk")
public record TextChunkerProperties(
  @NotNull
  Integer maxChars,
  @NotNull
  Integer overlapChars,
  @NotNull
  Integer hardMaxChars
) {

}
