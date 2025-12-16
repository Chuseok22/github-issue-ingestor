package xyz.romrom.githubissueingestor.qdrant.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.romrom.githubissueingestor.qdrant.infrastructure.property.QdrantProperty;

@Configuration
@EnableConfigurationProperties(QdrantProperty.class)
public class QdrantConfig {

}
