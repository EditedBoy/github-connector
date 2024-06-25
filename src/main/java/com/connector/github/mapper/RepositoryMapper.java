package com.connector.github.mapper;

import com.connector.github.dto.RepositoryData;
import com.connector.github.client.dto.GitHubBranch;
import com.connector.github.client.dto.GitHubRepository;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfiguration.class, uses = BranchMapper.class)
public interface RepositoryMapper {

  @Mapping(target = "name", source = "repository.name")
  @Mapping(target = "owner", source = "repository.owner.login")
  @Mapping(target = "branches", source = "branches")
  RepositoryData toRepositoryData(GitHubRepository repository, List<GitHubBranch> branches);
}
