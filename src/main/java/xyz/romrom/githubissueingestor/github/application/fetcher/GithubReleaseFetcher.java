package xyz.romrom.githubissueingestor.github.application.fetcher;

import static xyz.romrom.githubissueingestor.common.core.util.CommonUtil.nvl;

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
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;
import xyz.romrom.githubissueingestor.github.core.data.GithubReleaseItem;
import xyz.romrom.githubissueingestor.github.core.data.SourceDocument;
import xyz.romrom.githubissueingestor.github.core.fetcher.GithubFetcher;
import xyz.romrom.githubissueingestor.github.infrastructure.service.CursorService;

@Component
@Slf4j
@RequiredArgsConstructor
public class GithubReleaseFetcher implements GithubFetcher {

  private static final Instant DEFAULT_SINCE = Instant.parse("1970-01-01T00:00:00Z");

  private final GithubApiClient githubApiClient;
  private final CursorService cursorService;

  @Override
  public GithubFetchResult fetch(String repositoryFullName) {
    Instant since = cursorService.getCursor(repositoryFullName, SourceType.RELEASE)
      .map(TimeUtil::isoToInstant)
      .orElse(DEFAULT_SINCE);

    List<SourceDocument> documents = new ArrayList<>();
    Instant maxTime = since;

    int page = 1;
    int perPage = 100;

    while (true) {
      List<GithubReleaseItem> items = githubApiClient.getReleases(repositoryFullName, page, perPage);
      if (items.isEmpty()) {
        break;
      }

      for (GithubReleaseItem item : items) {
        if (item.id() == null) {
          continue;
        }

        Instant time = item.publishedAt() != null ? item.publishedAt() : item.createdAt();
        if (time == null) {
          time = Instant.now();
        }

        if (!time.isAfter(since)) {
          continue;
        }

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("tagName", item.tagName());

        String title = "[RELEASE]" + nvl(item.name(), "").trim() + " (" + nvl(item.tagName(), "").trim() + ")";
        String body = nvl(item.body(), "").trim();

        SourceDocument document = new SourceDocument(
          SourceType.RELEASE,
          String.valueOf(item.id()),
          title,
          body,
          item.htmlUrl(),
          time,
          metadata
        );
        documents.add(document);

        if (time.isAfter(maxTime)) {
          maxTime = time;
        }
      }

      page++;
      if (page > 20) {
        break;
      }
    }
    return new GithubFetchResult(SourceType.RELEASE, documents, maxTime.toString());
  }
}
