package com.steiner.vblog.mapper;

import com.steiner.vblog.dto.query.UserQuery;
import com.steiner.vblog.dto.request.PostUserRequest;
import com.steiner.vblog.dto.request.PutUserRequest;
import com.steiner.vblog.model.User;
import com.steiner.vblog.table_metadata.UsersMetadata;
import jakarta.annotation.Nonnull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface UserMapper {
    void createTable(@Nonnull UsersMetadata metadata);
    void dropTable(@Nonnull UsersMetadata metadata);

    int insertOne(@Param("metadata") @Nonnull UsersMetadata metadata,
                  @Param("request") @Nonnull PostUserRequest request);

    void deleteOne(@Param("metadata") @Nonnull UsersMetadata metadata,
                   @Param("id") int id);

    void updateOne(@Param("metadata") @Nonnull UsersMetadata metadata,
                   @Param("request") @Nonnull PutUserRequest request);

    Optional<User> findOne(@Param("metadata") @Nonnull UsersMetadata metadata,
                           @Param("id") int id);

    List<User> findAll(@Param("metadata") @Nonnull UsersMetadata metadata,
                       @Param("query") @Nonnull UserQuery query);
}
