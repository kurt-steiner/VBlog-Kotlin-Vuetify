package com.steiner.vblog.configure;

import com.steiner.vblog.table_metadata.*;
import jakarta.annotation.Nonnull;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

// 这个配置类是封装 `application.yaml` 中的配置
// 还封装了一些自定义的 Bean
@Configuration
public final class ApplicationConfigure {
    @Bean
    public Binder binder(@Nonnull Environment environment) {
        return Binder.get(environment);
    }

    @Bean("authentication-header")
    public String authenticationHeader(@Nonnull Binder binder) {
        return binder.bind("custom.authentication-header", String.class).orElse("Authentication");
    }

    @Bean("authorization-header")
    public String authorizationHeader(@Nonnull Binder binder) {
        return binder.bind("custom.authorization-header", String.class).orElse("Authorization");
    }

    @Bean("custom.permit")
    public String [] permit(@Nonnull Binder binder) {
        return binder.bind("custom.permit", String[].class).orElse(new String[0]);
    }

    @Bean
    public ArticlesMetadata articlesMetadata(@Nonnull Binder binder) {
        String prefix = "custom.table-metadata.articles";

        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("articles");

        String associationTableName = binder.bind("%s.association-table-name".formatted(prefix), String.class)
                .orElse("articles-tag");

        int titleLength = binder.bind("%s.title-length".formatted(prefix), Integer.class)
                .orElse(256);

        int defaultQueryPage = binder.bind("%s.default-query-page".formatted(prefix), Integer.class)
                .orElse(0);

        int defaultQuerySize = binder.bind("%s.default-query-size".formatted(prefix), Integer.class)
                .orElse(20);

        return new ArticlesMetadata(
                tableName,
                associationTableName,
                titleLength,
                defaultQueryPage,
                defaultQuerySize
        );
    }

    @Bean
    public CategoriesMetadata categoriesMetadata(@Nonnull Binder binder, @Nonnull UsersMetadata usersMetadata) {
        String prefix = "custom.table-metadata.categories";

        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("categories");

        int nameLength = binder.bind("%s.name-length".formatted(prefix), Integer.class)
                .orElse(64);

        return new CategoriesMetadata(tableName, usersMetadata.tableName, nameLength);
    }

    @Bean
    public TagsMetadata tagsMetadata(@Nonnull Binder binder, UsersMetadata usersMetadata, ArticlesMetadata articlesMetadata) {
        String prefix = "custom.table-metadata.tags";
        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("tags");

        int nameLength = binder.bind("%s.name-length".formatted(prefix), Integer.class)
                .orElse(64);

        return new TagsMetadata(tableName, usersMetadata.tableName, articlesMetadata.tableName, nameLength);
    }

    @Bean
    public ImageItemsMetadata imageItemsMetadata(@Nonnull Binder binder) {
        String prefix = "custom.table-metadata.image-items";
        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("image-items");

        int nameLength = binder.bind("%s.name-length".formatted(prefix), Integer.class)
                .orElse(4096);

        int pathLength = binder.bind("%s.path-length".formatted(prefix), Integer.class)
                .orElse(4096);

        return new ImageItemsMetadata(tableName, nameLength, pathLength);
    }

    @Bean
    public UsersMetadata usersMetadata(@Nonnull Binder binder, ImageItemsMetadata imageItemsMetadata) {
        String prefix = "custom.table-metadata.users";
        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("users");

        int nameLength = binder.bind("%s.name-length".formatted(prefix), Integer.class)
                .orElse(64);

        int nicknameLength = binder.bind("%s.nickname-length".formatted(prefix), Integer.class)
                .orElse(64);

        int passwordLength = binder.bind("%s.password-length".formatted(prefix), Integer.class)
                .orElse(64);

        int emailLength = binder.bind("%s.email-length".formatted(prefix), Integer.class)
                .orElse(64);

        return new UsersMetadata(
                tableName,
                imageItemsMetadata.tableName,
                nameLength,
                nicknameLength,
                passwordLength,
                emailLength
        );
    }
}
