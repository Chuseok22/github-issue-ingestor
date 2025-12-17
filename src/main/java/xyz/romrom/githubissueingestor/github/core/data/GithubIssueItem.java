package xyz.romrom.githubissueingestor.github.core.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record GithubIssueItem(
  @JsonProperty(value = "id")
  Long id,
  @JsonProperty(value = "number")
  Integer number,
  @JsonProperty(value = "title")
  String title,
  @JsonProperty(value = "body")
  String body,
  @JsonProperty(value = "html_url")
  String htmlUrl,
  @JsonProperty(value = "updated_at")
  Instant updatedAt,
  @JsonProperty(value = "pull_request")
  Object pullRequest // 존재하면 PR
) {

  public boolean isPullRequest() {
    return pullRequest != null;
  }
}
