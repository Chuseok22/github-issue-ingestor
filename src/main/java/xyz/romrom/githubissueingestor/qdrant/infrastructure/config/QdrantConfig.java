package xyz.romrom.githubissueingestor.qdrant.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.romrom.githubissueingestor.qdrant.infrastructure.properties.QdrantProperties;

@Configuration
@EnableConfigurationProperties(QdrantProperties.class)
public class QdrantConfig {

}
