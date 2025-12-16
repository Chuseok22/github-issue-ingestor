package xyz.romrom.githubissueingestor.github.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import xyz.romrom.githubissueingestor.github.core.data.GithubTreeItem;

public record GithubTreeResponse(
  @JsonProperty(value = "sha")
  String sha,
  @JsonProperty(value = "tree")
  List<GithubTreeItem> tree
  ) {

}
