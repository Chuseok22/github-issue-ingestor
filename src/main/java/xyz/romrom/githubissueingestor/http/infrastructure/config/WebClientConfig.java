package xyz.romrom.githubissueingestor.http.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import xyz.romrom.githubissueingestor.github.infrastructure.properties.GithubProperties;
import xyz.romrom.githubissueingestor.ollama.infrastructure.properties.OllamaProperties;
import xyz.romrom.githubissueingestor.qdrant.infrastructure.properties.QdrantProperties;

@Configuration
public class WebClientConfig {

  @Bean
  public WebClient githubWebClient(GithubProperties property) {
    WebClient.Builder builder = WebClient.builder()
      .baseUrl(property.baseUrl())
      .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github+json")
      .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + property.token());

    return builder.build();
  }

  @Bean
  public WebClient ollamaWebClient(OllamaProperties property) {
    ExchangeStrategies strategies = ExchangeStrategies.builder()
      .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(8 * 1024 * 1024))
      .build();

    return WebClient.builder()
      .baseUrl(property.baseUrl())
      .exchangeStrategies(strategies)
      .defaultHeader("X-API-Key", property.apiKey())
      .build();
  }

  @Bean
  public WebClient qdrantWebClient(QdrantProperties property) {
    return WebClient.builder()
      .baseUrl(property.baseUrl())
      .defaultHeader("api-key", property.apiKey())
      .build();
  }
}
