package xyz.romrom.githubissueingestor.github.core.data;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GithubTreeItem(
  @JsonProperty(value = "path")
  String path,
  @JsonProperty(value = "mode")
  String mode,
  @JsonProperty(value = "type")
  String type,
  @JsonProperty(value = "sha")
  String sha,
  @JsonProperty(value = "size")
  Long size
) {

}
