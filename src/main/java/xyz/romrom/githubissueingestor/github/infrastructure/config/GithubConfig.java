package xyz.romrom.githubissueingestor.github.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.romrom.githubissueingestor.github.infrastructure.properties.GithubProperties;

@Configuration
@EnableConfigurationProperties(GithubProperties.class)
public class GithubConfig {

}
