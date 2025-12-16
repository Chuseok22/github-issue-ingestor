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
import xyz.romrom.githubissueingestor.common.core.util.CommonUtil;
import xyz.romrom.githubissueingestor.common.core.util.TimeUtil;
import xyz.romrom.githubissueingestor.github.application.GithubApiClient;
import xyz.romrom.githubissueingestor.github.application.dto.GithubFetchResult;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;
import xyz.romrom.githubissueingestor.github.core.data.GithubCommitListItem;
import xyz.romrom.githubissueingestor.github.core.data.SourceDocument;
import xyz.romrom.githubissueingestor.github.core.fetcher.GithubFetcher;
import xyz.romrom.githubissueingestor.github.infrastructure.service.CursorService;

@Component
@Slf4j
@RequiredArgsConstructor
public class GithubCommitFetcher implements GithubFetcher {

  private static final Instant DEFAULT_SINCE = Instant.parse("1970-01-01T00:00:00Z");

  private final GithubApiClient githubApiClient;
  private final CursorService cursorService;

  @Override
  public GithubFetchResult fetch(String repositoryFullName) {
    Instant since = cursorService.getCursor(repositoryFullName, SourceType.COMMIT)
      .map(TimeUtil::isoToInstant)
      .orElse(DEFAULT_SINCE);

    List<SourceDocument> documents = new ArrayList<>();
    Instant maxTime = since;

    int page = 1;
    int perPage = 100;

    while (true) {
      List<GithubCommitListItem> items = githubApiClient.getCommits(repositoryFullName, since, page, perPage);
      if (items.isEmpty()) {
        break;
      }

      for (GithubCommitListItem item : items) {
        if (nvl(item.sha(), "").isEmpty() || item.commit() == null || item.commit().author() == null) {
          continue;
        }

        Instant commitTime = TimeUtil.isoToInstant(item.commit().author().date());
        String message = nvl(item.commit().message(), "").trim();

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("sha", item.sha());
        metadata.put("htmlUrl", item.htmlUrl());

        SourceDocument sourceDocument = new SourceDocument(
          SourceType.COMMIT,
          item.sha(),
          "[COMMIT] " + item.sha(),
          message,
          item.htmlUrl(),
          commitTime,
          metadata
        );
        documents.add(sourceDocument);

        if (commitTime.isAfter(maxTime)) {
          maxTime = commitTime;
        }
      }

      page++;
      if (page > 50) {
        break;
      }
    }

    return new GithubFetchResult(SourceType.COMMIT, documents, maxTime.toString());
  }
}
