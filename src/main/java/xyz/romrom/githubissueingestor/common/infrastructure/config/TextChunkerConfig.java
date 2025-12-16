package xyz.romrom.githubissueingestor.common.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.romrom.githubissueingestor.common.infrastructure.properties.TextChunkerProperties;

@Configuration
@EnableConfigurationProperties(TextChunkerProperties.class)
public class TextChunkerConfig {

}
