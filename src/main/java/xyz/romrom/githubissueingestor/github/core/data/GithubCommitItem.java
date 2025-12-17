package xyz.romrom.githubissueingestor.github.core.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubCommitItem(
  @JsonProperty(value = "message")
  String message,
  @JsonProperty(value = "author")
  Author author
) {

  public record Author(
    @JsonProperty(value = "date")
    String date
  ) {

  }
}
