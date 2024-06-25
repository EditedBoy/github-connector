package com.connector.github.unit.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.connector.github.api.dto.RepositoryData;
import com.connector.github.mapper.BranchMapperImpl;
import com.connector.github.mapper.CommitMapperImpl;
import com.connector.github.mapper.RepositoryMapper;
import com.connector.github.mapper.RepositoryMapperImpl;
import com.connector.github.unit.BaseUnitTest;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RepositoryMapperTest extends BaseUnitTest {

  private final RepositoryMapper repositoryMapper = new RepositoryMapperImpl(new BranchMapperImpl(new CommitMapperImpl()));

  @Test
  void toRepositoryData() {
    // Given
    var repository = buildGitHubRepository(OWNER, REPOSITORY_1, false);
    var branchMain = buildGitHubBranch(BRANCH_MAIN, COMMIT_SHA_MAIN);
    var branchTest = buildGitHubBranch(BRANCH_TEST, COMMIT_SHA_TEST);

    // When
    RepositoryData repositoryData = repositoryMapper.toRepositoryData(repository, List.of(branchMain, branchTest));

    // Then
    assertEquals(repository.name(), repositoryData.getName());
    assertEquals(repository.owner().login(), repositoryData.getOwner());
    assertEquals(2, repositoryData.getBranches().size());
    assertEquals(branchMain.name(), repositoryData.getBranches().get(0).getName());
    assertEquals(branchMain.commit().sha(), repositoryData.getBranches().get(0).getCommit().getSha());
    assertEquals(branchTest.name(), repositoryData.getBranches().get(1).getName());
    assertEquals(branchTest.commit().sha(), repositoryData.getBranches().get(1).getCommit().getSha());
  }
}
