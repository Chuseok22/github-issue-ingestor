package xyz.romrom.githubissueingestor.github.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubBlobResponse(
  @JsonProperty(value = "content")
  String content,
  @JsonProperty(value = "encoding")
  String encoding,
  @JsonProperty(value = "size")
  Long size
  ) {

}
