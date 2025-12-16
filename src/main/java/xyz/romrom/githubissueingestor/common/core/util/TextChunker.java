package xyz.romrom.githubissueingestor.common.core.util;

import static xyz.romrom.githubissueingestor.common.core.util.CommonUtil.nvl;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TextChunker {

  private final int maxChars;
  private final int overlapChars;
  private final int hardMaxChars;

  public List<String> chunk(String text) {
    if (nvl(text, "").isEmpty()) {
      return List.of();
    }

    String normalized = text.trim();
    if (normalized.length() > hardMaxChars) {
      normalized = normalized.substring(0, hardMaxChars);
    }

    if (normalized.length() <= maxChars) {
      return List.of(normalized);
    }

    List<String> chunks = new ArrayList<>();
    int start = 0;

    while (start < normalized.length()) {
      int end = Math.min(start + maxChars, normalized.length());
      String part = normalized.substring(start, end).trim();
      if (!nvl(part, "").isEmpty()) {
        chunks.add(part);
      }

      if (end >= normalized.length()) {
        break;
      }

      int nextStart = end - overlapChars;
      start = Math.max(nextStart, 0);
    }

    return chunks;
  }
}
