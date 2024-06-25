package com.connector.github;

import com.connector.github.client.dto.GitHubBranch;
import com.connector.github.client.dto.GitHubCommit;
import com.connector.github.client.dto.GitHubOwner;
import com.connector.github.client.dto.GitHubRepository;

public abstract class BaseTest {

  protected static final String OWNER = "Test_Owner";
  protected static final String REPOSITORY_1 = "GitHub_repository_1";
  protected static final String REPOSITORY_2 = "GitHub_repository_2";
  protected static final String REPOSITORY_3 = "GitHub_repository_3";
  protected static final String BRANCH_MAIN = "main";
  protected static final String BRANCH_TEST = "test";
  protected static final String COMMIT_SHA_MAIN = "sha-main";
  protected static final String COMMIT_SHA_TEST = "sha-test";


  protected GitHubRepository buildGitHubRepository(String owner, String name, Boolean isFork) {
    return GitHubRepository.builder()
        .owner(new GitHubOwner(owner))
        .name(name)
        .fork(isFork)
        .build();
  }

  protected GitHubBranch buildGitHubBranch(String name, String sha) {
    return GitHubBranch.builder()
        .name(name)
        .commit(new GitHubCommit(sha))
        .build();
  }
}
