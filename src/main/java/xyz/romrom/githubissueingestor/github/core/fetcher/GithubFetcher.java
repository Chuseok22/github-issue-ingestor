package xyz.romrom.githubissueingestor.github.core.fetcher;

import xyz.romrom.githubissueingestor.github.application.dto.GithubFetchResult;

public interface GithubFetcher {

  GithubFetchResult fetch(String repositoryFullName);

}
