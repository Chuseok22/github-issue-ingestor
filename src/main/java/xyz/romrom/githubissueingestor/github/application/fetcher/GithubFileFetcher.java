package xyz.romrom.githubissueingestor.github.application.fetcher;

import static xyz.romrom.githubissueingestor.common.core.util.CommonUtil.nvl;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.romrom.githubissueingestor.common.core.util.TimeUtil;
import xyz.romrom.githubissueingestor.github.application.GithubApiClient;
import xyz.romrom.githubissueingestor.github.application.dto.GithubBlobResponse;
import xyz.romrom.githubissueingestor.github.application.dto.GithubCommitResponse;
import xyz.romrom.githubissueingestor.github.application.dto.GithubFetchResult;
import xyz.romrom.githubissueingestor.github.application.dto.GithubRepoResponse;
import xyz.romrom.githubissueingestor.github.application.dto.GithubTreeResponse;
import xyz.romrom.githubissueingestor.github.core.constant.SourceType;
import xyz.romrom.githubissueingestor.github.core.data.GithubTreeItem;
import xyz.romrom.githubissueingestor.github.core.data.SourceDocument;
import xyz.romrom.githubissueingestor.github.core.fetcher.GithubFetcher;
import xyz.romrom.githubissueingestor.github.infrastructure.service.CursorService;

@Component
@Slf4j
@RequiredArgsConstructor
public class GithubFileFetcher implements GithubFetcher {

  private static final String DEFAULT_CURSOR_VALUE = "NONE";

  private final GithubApiClient githubApiClient;
  private final CursorService cursorService;

  @Override
  public GithubFetchResult fetch(String repositoryFullName) {
    GithubRepoResponse repoResponse = githubApiClient.getRepository(repositoryFullName);

    // default branch 최신 커밋 sha
    GithubCommitResponse headCommit = githubApiClient.getCommit(repositoryFullName, repoResponse.defaultBranch());
    String headSha = headCommit.sha();

    String lastCursor = cursorService.getCursor(repositoryFullName, SourceType.REPO_FILE).orElse(DEFAULT_CURSOR_VALUE);
    if (headSha.equals(lastCursor)) {
      return new GithubFetchResult(SourceType.REPO_FILE, List.of(), lastCursor);
    }

    GithubTreeResponse tree = githubApiClient.getTreeRecursive(repositoryFullName, headSha);

    Instant commitTime = Instant.now();
    if (headCommit.commit() != null && headCommit.commit().author() != null) {
      commitTime = TimeUtil.isoToInstant(headCommit.commit().author().date());
    }

    List<SourceDocument> documents = new ArrayList<>();
    if (tree.tree() == null) {
      return new GithubFetchResult(SourceType.REPO_FILE, List.of(), headSha);
    }

    for (GithubTreeItem item : tree.tree()) {
      if (!"blob".equals(item.type())) {
        continue;
      }
      if (nvl(item.path(), "").isEmpty() || nvl(item.sha(), "").isEmpty()) {
        continue;
      }

      // 큰 파일 제외
      Long size = item.size();
      if (size != null && size > 1_000_000L) {
        continue;
      }

      GithubBlobResponse blob = githubApiClient.getBlob(repositoryFullName, item.sha());
      if (nvl(blob.content(), "").isEmpty()) {
        continue;
      }
      if (blob.encoding() != null && !"base64".equalsIgnoreCase(blob.encoding())) {
        continue;
      }

      String decoded = decodeBase64Content(blob.content());
      if (nvl(decoded, "").isEmpty()) {
        continue;
      }

      Map<String, Object> metadata = new HashMap<>();
      metadata.put("path", item.path());
      metadata.put("blobSha", item.sha());
      metadata.put("size", item.size());

      SourceDocument sourceDocument = new SourceDocument(
        SourceType.REPO_FILE,
        item.path(),
        item.path(),
        decoded,
        null,
        commitTime,
        metadata
      );
      documents.add(sourceDocument);
    }

    return new GithubFetchResult(SourceType.REPO_FILE, documents, headSha);
  }

  private String decodeBase64Content(String base64WithNewlines) {
    try {
      String normalized = base64WithNewlines.replace("\n", "").replace("\r", "");
      byte[] decoded = Base64.getDecoder().decode(normalized);
      return new String(decoded, StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
      log.warn("Base64 디코딩 실패: {}", e.getMessage());
      return "";
    }
  }
}
