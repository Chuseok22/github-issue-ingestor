package xyz.romrom.githubissueingestor.github.core.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubCommitListItem(
  @JsonProperty(value = "sha")
  String sha,
  @JsonProperty(value = "html_url")
  String htmlUrl,
  @JsonProperty(value = "commit")
  GithubCommitItem commit
) {

}
