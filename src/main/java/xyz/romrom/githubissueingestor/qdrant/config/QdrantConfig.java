package xyz.romrom.githubissueingestor.qdrant.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.romrom.githubissueingestor.qdrant.property.QdrantProperty;

@Configuration
@EnableConfigurationProperties(QdrantProperty.class)
public class QdrantConfig {

}
