package com.connector.github.mapper;

import com.connector.github.dto.CommitData;
import com.connector.github.client.dto.GitHubCommit;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfiguration.class)
public interface CommitMapper {

  CommitData toCommitData(GitHubCommit commit);
}
