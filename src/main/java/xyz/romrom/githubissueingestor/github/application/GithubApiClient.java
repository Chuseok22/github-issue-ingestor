package xyz.romrom.githubissueingestor.github.application;

import static xyz.romrom.githubissueingestor.common.core.util.CommonUtil.nvl;

import java.net.URI;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import xyz.romrom.githubissueingestor.github.application.dto.GithubBlobResponse;
import xyz.romrom.githubissueingestor.github.application.dto.GithubCommitResponse;
import xyz.romrom.githubissueingestor.github.application.dto.GithubRepoResponse;
import xyz.romrom.githubissueingestor.github.application.dto.GithubTreeResponse;
import xyz.romrom.githubissueingestor.github.core.data.GithubCommitListItem;
import xyz.romrom.githubissueingestor.github.core.data.GithubIssueItem;
import xyz.romrom.githubissueingestor.github.core.data.GithubReleaseItem;

@Component
@Slf4j
@RequiredArgsConstructor
public class GithubApiClient {

  private final WebClient githubWebClient;

  public GithubRepoResponse getRepository(String repositoryFullName) {
    String[] parts = split(repositoryFullName);
    String path = "/repos/" + parts[0] + "/" + parts[1];
    log.info("깃허브 repository fetch 요청: {}", path);

    return githubWebClient.get()
      .uri(path)
      .retrieve()
      .onStatus(HttpStatusCode::isError, response ->
        response.bodyToMono(String.class)
          .map(body ->
            new IllegalArgumentException("깃허브 레포지토리 fetch 실패: " + body)
          )
      )
      .bodyToMono(GithubRepoResponse.class)
      .block(Duration.ofSeconds(30));
  }

  public GithubTreeResponse getTreeRecursive(String repositoryFullName, String treeSha) {
    String[] parts = split(repositoryFullName);
    String path = "/repos/" + parts[0] + "/" + parts[1] + "/git/trees/" + treeSha;
    log.info("깃허브 tree fetch 요청: {}", path);

    URI uri = UriComponentsBuilder.fromPath(path)
      .queryParam("recursive", "1")
      .build(true)
      .toUri();

    return githubWebClient.get()
      .uri(uri)
      .retrieve()
      .onStatus(HttpStatusCode::isError, response ->
        response.bodyToMono(String.class)
          .map(body -> new IllegalStateException("깃허브 Tree fetch 실패: " + body))
      )
      .bodyToMono(GithubTreeResponse.class)
      .block(Duration.ofSeconds(60));
  }

  public GithubBlobResponse getBlob(String repositoryFullName, String blobSha) {
    String[] parts = split(repositoryFullName);
    String path = "/repos/" + parts[0] + "/" + parts[1] + "/git/blobs/" + blobSha;
    log.info("깃허브 blob fetch 요청: {}", path);

    return githubWebClient.get()
      .uri(path)
      .retrieve()
      .onStatus(HttpStatusCode::isError, response ->
        response.bodyToMono(String.class)
          .map(body -> new IllegalStateException("깃허브 blob fetch 실패: " + body))
      )
      .bodyToMono(GithubBlobResponse.class)
      .block(Duration.ofSeconds(60));
  }

  public GithubCommitResponse getCommit(String repositoryFullName, String sha) {
    String[] parts = split(repositoryFullName);
    String path = "/repos/" + parts[0] + "/" + parts[1] + "/commits/" + sha;
    log.info("깃허브 commit fetch 요청: {}", path);

    return githubWebClient.get()
      .uri(path)
      .retrieve()
      .onStatus(HttpStatusCode::isError, response ->
        response.bodyToMono(String.class)
          .map(body -> new IllegalStateException("깃허브 커밋 fetch 실패: " + body))
      )
      .bodyToMono(GithubCommitResponse.class)
      .block(Duration.ofSeconds(60));
  }

  public List<GithubIssueItem> getIssues(String repositoryFullName, Instant since, int page, int perPage) {
    String[] parts = split(repositoryFullName);
    String path = "/repos/" + parts[0] + "/" + parts[1] + "/issues";
    log.info("깃허브 issue fetch 요청: {}", path);

    URI uri = UriComponentsBuilder.fromPath(path)
      .queryParam("state", "all")
      .queryParam("since", since.toString())
      .queryParam("per_page", String.valueOf(perPage))
      .queryParam("page", String.valueOf(page))
      .build(true)
      .toUri();

    GithubIssueItem[] items = githubWebClient.get()
      .uri(uri)
      .retrieve()
      .onStatus(HttpStatusCode::isError, response ->
        response.bodyToMono(String.class)
          .map(body -> new IllegalStateException("깃허브 이슈 fetch 실패: " + body))
      )
      .bodyToMono(GithubIssueItem[].class)
      .block(Duration.ofSeconds(60));

    if (items == null) {
      return List.of();
    }
    return List.of(items);
  }

  public List<GithubCommitListItem> getCommits(String repositoryFullName, Instant since, int page, int perPage) {
    String[] parts = split(repositoryFullName);
    String path = "/repos/" + parts[0] + "/" + parts[1] + "/commits";

    URI uri = UriComponentsBuilder.fromPath(path)
      .queryParam("since", since.toString())
      .queryParam("per_page", String.valueOf(perPage))
      .queryParam("page", String.valueOf(page))
      .build(true)
      .toUri();

    GithubCommitListItem[] items = githubWebClient.get()
      .uri(uri)
      .retrieve()
      .onStatus(HttpStatusCode::isError, response ->
        response.bodyToMono(String.class)
          .map(body -> new IllegalStateException("깃허브 커밋 목록 fetch 실패: " + body))
      )
      .bodyToMono(GithubCommitListItem[].class)
      .block(Duration.ofSeconds(60));

    if (items == null) {
      return List.of();
    }
    return List.of(items);
  }

  public List<GithubReleaseItem> getReleases(String repositoryFullName, int page, int perPage) {
    String[] parts = split(repositoryFullName);
    String path = "/repos" + parts[0] + "/" + parts[1] + "/releases";

    URI uri = UriComponentsBuilder.fromPath(path)
      .queryParam("per_page", String.valueOf(perPage))
      .queryParam("page", String.valueOf(page))
      .build(true)
      .toUri();

    GithubReleaseItem[] items = githubWebClient.get()
      .uri(uri)
      .retrieve()
      .onStatus(HttpStatusCode::isError, response ->
        response.bodyToMono(String.class)
          .map(body -> new IllegalStateException("깃허브 릴리즈 목록 fetch 실패: " + body))
      )
      .bodyToMono(GithubReleaseItem[].class)
      .block(Duration.ofSeconds(60));

    if (items == null) {
      return List.of();
    }
    return List.of(items);
  }

  private String[] split(String repositoryFullName) {
    if (nvl(repositoryFullName, "").isEmpty() || !repositoryFullName.contains("/")) {
      throw new IllegalArgumentException("레포지토리 명은 'owner/name' 형태여야합니다");
    }
    String[] parts = repositoryFullName.split("/");
    if (parts.length != 2) {
      throw new IllegalArgumentException("레포지토리 명은 'owner/name' 형태여야합니다");
    }
    return parts;
  }
}
