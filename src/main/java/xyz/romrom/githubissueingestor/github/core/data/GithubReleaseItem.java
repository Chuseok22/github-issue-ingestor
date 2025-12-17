package xyz.romrom.githubissueingestor.github.core.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record GithubReleaseItem(
  @JsonProperty(value = "id")
  Long id,
  @JsonProperty(value = "tag_name")
  String tagName,
  @JsonProperty(value = "name")
  String name,
  @JsonProperty(value = "body")
  String body,
  @JsonProperty(value = "html_url")
  String htmlUrl,
  @JsonProperty(value = "published_at")
  Instant publishedAt,
  @JsonProperty(value = "created_at")
  Instant createdAt
) {

}
