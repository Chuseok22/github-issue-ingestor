package xyz.romrom.githubissueingestor.ollama.infrastructure.properties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "ollama")
public record OllamaProperties(
  @NotBlank
  String baseUrl,
  @NotBlank
  String apiKey,
  @NotBlank
  String embeddingModel,
  @NotNull
  Integer dimension
) {

}
