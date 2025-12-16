package xyz.romrom.githubissueingestor.ollama.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.romrom.githubissueingestor.ollama.infrastructure.property.OllamaProperty;

@Configuration
@EnableConfigurationProperties(OllamaProperty.class)
public class OllamaConfig {

}
