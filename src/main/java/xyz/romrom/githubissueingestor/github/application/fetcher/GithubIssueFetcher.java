package xyz.romrom.githubissueingestor.github.application.fetcher;

import static xyz.romrom.githubissueingestor.common.core.util.CommonUtil.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.romrom.githubissueingestor.common.core.util.TimeUtil;
import xyz.romrom.githubissueingestor.github.application.GithubApiClient;
import xyz.romrom.githubissueingestor.github.application.dto.GithubFetchResult;
import xyz.romrom.githubissueingestor.github.core.data.GithubIssueItem;
import xyz.romrom.githubissueingestor.github.core.data.SourceDocument;
import xyz.romrom.githubissueingestor.github.infrastructure.service.CursorService;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;
import xyz.romrom.githubissueingestor.github.core.fetcher.GithubFetcher;

@Component
@Slf4j
@RequiredArgsConstructor
public class GithubIssueFetcher implements GithubFetcher {

  private static final Instant DEFAULT_SINCE = Instant.parse("1970-01-01-T00:00:00Z");

  private final GithubApiClient githubApiClient;
  private final CursorService cursorService;


  @Override
  public GithubFetchResult fetch(String repositoryFullName) {
    Instant since = cursorService.getCursor(repositoryFullName, SourceType.ISSUE)
      .map(TimeUtil::isoToInstant)
      .orElse(DEFAULT_SINCE);

    // GitHub Issue 엔드포인트는 PR도 같이 내려옴 -> 타입 분리
    List<SourceDocument> documents = new ArrayList<>();
    Instant maxUpdated = since;

    int page = 1;
    int perPage = 100;

    while (true) {
      List<GithubIssueItem> items = githubApiClient.getIssues(repositoryFullName, since, page, perPage);
      if (items.isEmpty()) {
        break;
      }

      for (GithubIssueItem item : items) {
        if (item.id() == null || item.updatedAt() == null) {
          continue;
        }

        SourceType sourceType = item.isPullRequest() ? SourceType.PULL_REQUEST : SourceType.ISSUE;

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("number", item.number());
        metadata.put("htmlUrl", item.htmlUrl());

        String title = prefix(sourceType) + " #" + item.number() + " " + item.title();
        String body = nvl(item.body(), "").trim();

        SourceDocument sourceDocument = new SourceDocument(
          sourceType,
          String.valueOf(item.id()),
          title,
          body,
          item.htmlUrl(),
          item.updatedAt(),
          metadata
        );

        documents.add(sourceDocument);

        if (item.updatedAt().isAfter(maxUpdated)) {
          maxUpdated = item.updatedAt();
        }
      }

      page++;
      if (page > 50) {
        break; // 비정상 루프 방지용 안전장치
      }
    }

    // ISSUE 커서는 maxUpdated로 저장 (PR도 같이 생성)
    return new GithubFetchResult(SourceType.ISSUE, documents, maxUpdated.toString());
  }

  private String prefix(SourceType sourceType) {
    switch (sourceType) {
      case PULL_REQUEST -> {
        return "[PR]";
      }
      case ISSUE -> {
        return "[ISSUE]";
      }
      default -> throw new IllegalArgumentException("허용되지 않은 SourceType");
    }
  }
}
