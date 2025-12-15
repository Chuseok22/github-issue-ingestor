package xyz.romrom.githubissueingestor.github.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import xyz.romrom.githubissueingestor.github.property.GithubProperty;

@Configuration
@EnableConfigurationProperties(GithubProperty.class)
public class GithubConfig {

}
