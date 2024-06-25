package com.connector.github.mapper;

import com.connector.github.api.dto.BranchData;
import com.connector.github.client.dto.GitHubBranch;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class, uses = CommitMapper.class)
public interface BranchMapper {

  BranchData toBranchData(GitHubBranch branch);

  List<BranchData> toBranchData(List<GitHubBranch> branch);
}
