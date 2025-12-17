package xyz.romrom.githubissueingestor.github.application.dto;

import java.util.List;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;
import xyz.romrom.githubissueingestor.github.core.data.SourceDocument;

public record GithubFetchResult(
  SourceType sourceType,
  List<SourceDocument> documents,
  String nextCursorValue
) {

}
