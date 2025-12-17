package xyz.romrom.githubissueingestor.github.core.data;

import java.time.Instant;
import java.util.Map;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;

public record SourceDocument(
  SourceType sourceType,
  String sourceId,
  String title,
  String body,
  String url,
  Instant updatedAt,
  Map<String, Object> metadata
) {

}
