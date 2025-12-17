package xyz.romrom.githubissueingestor.github.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubRepoResponse(
  @JsonProperty(value = "full_name")
  String fullName,
  @JsonProperty(value = "default_branch")
  String defaultBranch
) {
}
