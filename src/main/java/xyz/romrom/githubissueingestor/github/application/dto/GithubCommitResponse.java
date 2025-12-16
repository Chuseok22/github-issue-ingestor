package xyz.romrom.githubissueingestor.github.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import xyz.romrom.githubissueingestor.github.core.data.GithubCommitItem;

public record GithubCommitResponse(
  @JsonProperty(value = "sha")
  String sha,
  @JsonProperty(value = "commit")
  GithubCommitItem commit
) {

}
