package com.steiner.vblog.configure;

import com.steiner.vblog.exception.ApplicationYamlLoadException;
import com.steiner.vblog.service.UserService;
import com.steiner.vblog.table_metadata.*;
import com.steiner.vblog.util.JWTUtil;
import jakarta.annotation.Nonnull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.Arrays;

// 这个配置类是封装 `application.yaml` 中的配置
// 还封装了一些自定义的 Bean
@Configuration
public class ApplicationConfigure {
    @Bean
    public Binder binder(@Nonnull Environment environment) {
        return Binder.get(environment);
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

        String tagsTableName = binder.bind("%s.tags-table-name".formatted(prefix), String.class)
                .orElse("tags");

        String articleTagTableName = binder.bind("%s.article-tag-table-name".formatted(prefix), String.class)
                .orElse("article-tag");

        String usersTableName = binder.bind("%s.users-table-name".formatted(prefix), String.class)
                .orElse("users");

        String categoriesTableName = binder.bind("%s.categories-table-name".formatted(prefix), String.class)
                .orElse("categories");

        int titleLength = binder.bind("%s.title-length".formatted(prefix), Integer.class)
                .orElse(256);

        int summaryLength = binder.bind("%s.summary-length".formatted(prefix), Integer.class)
                .orElse(200);

        int defaultQueryPage = binder.bind("%s.default-query-page".formatted(prefix), Integer.class)
                .orElse(0);

        int defaultQuerySize = binder.bind("%s.default-query-size".formatted(prefix), Integer.class)
                .orElse(20);

        return ArticlesMetadata.builder()
                .tableName(tableName)
                .articleTagTableName(articleTagTableName)
                .tagsTableName(tagsTableName)
                .usersTableName(usersTableName)
                .categoriesTableName(categoriesTableName)
                .titleLength(titleLength)
                .summaryLength(summaryLength)
                .defaultQueryPage(defaultQueryPage)
                .defaultQuerySize(defaultQuerySize)
                .build();
    }

    @Bean
    public CategoriesMetadata categoriesMetadata(@Nonnull Binder binder) {
        String prefix = "custom.table-metadata.categories";

        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("categories");

        int nameLength = binder.bind("%s.name-length".formatted(prefix), Integer.class)
                .orElse(64);

        String usersTableName = binder.bind("%s.users-table-name".formatted(prefix), String.class)
                .orElse("users");

        return CategoriesMetadata.builder()
                .tableName(tableName)
                .nameLength(nameLength)
                .usersTableName(usersTableName)
                .build();
    }

    @Bean
    public TagsMetadata tagsMetadata(@Nonnull Binder binder) {
        String prefix = "custom.table-metadata.tags";
        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("tags");

        int nameLength = binder.bind("%s.name-length".formatted(prefix), Integer.class)
                .orElse(64);

        String usersTableName = binder.bind("%s.users-table-name".formatted(prefix), String.class)
                .orElse("users");

        return TagsMetadata.builder()
                .tableName(tableName)
                .usersTableName(usersTableName)
                .nameLength(nameLength)
                .build();
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

        return ImageItemsMetadata.builder()
                .tableName(tableName)
                .nameLength(nameLength)
                .pathLength(pathLength)
                .build();
    }

    @Bean
    public UsersMetadata usersMetadata(@Nonnull Binder binder) {
        String prefix = "custom.table-metadata.users";
        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class)
                .orElse("users");

        int nameLength = binder.bind("%s.name-length".formatted(prefix), Integer.class)
                .orElse(64);

        int nicknameLength = binder.bind("%s.nickname-length".formatted(prefix), Integer.class)
                .orElse(64);

        int passwordLength = binder.bind("%s.password-hash-length".formatted(prefix), Integer.class)
                .orElse(64);

        int emailLength = binder.bind("%s.email-length".formatted(prefix), Integer.class)
                .orElse(64);

        String imageItemsTableName = binder.bind("%s.image-items-table-name".formatted(prefix), String.class)
                .orElse("image-items");

        return UsersMetadata.builder()
                .tableName(tableName)
                .nameLength(nameLength)
                .nicknameLength(nicknameLength)
                .imageItemsTableName(imageItemsTableName)
                .passwordHashLength(passwordLength)
                .emailLength(emailLength)
                .build();
    }

    @Bean
    public ArticleTagMetadata articleTagMetadata(@Nonnull Binder binder) {
        String prefix = "custom.table-metadata.article-tag";
        String tableName = binder.bind("%s.table-name".formatted(prefix), String.class).orElse("article-tag");
        String articlesTableName = binder.bind("%s.articles-table-name".formatted(prefix), String.class).orElse("articles");
        String tagsTableName = binder.bind("%s.tags-table-name".formatted(prefix), String.class).orElse("tags");

        return ArticleTagMetadata.builder()
                .tableName(tableName)
                .articlesTableName(articlesTableName)
                .tagsTableName(tagsTableName)
                .build();
    }

    @Bean("initialize")
    public boolean initialize(@Nonnull Binder binder) {
        return binder.bind("custom.initialize", Boolean.class).orElse(false);
    }

    @Bean("storage")
    public String storagePath(@Nonnull Binder binder) {
        String osname = System.getProperty("os.name").toLowerCase();

        if (osname.contains("win")) {
            return binder.bind("custom.windows-storage", String.class).orElseThrow(() -> new ApplicationYamlLoadException("custom.window-storage not defined"));
        } else if (osname.contains("nix") || osname.contains("nux") || osname.contains("linux")) {
            return binder.bind("custom.linux-storage", String.class).orElseThrow(() -> new ApplicationYamlLoadException("custom.linux-storage not defined"));
        } else {
            throw new ApplicationYamlLoadException("unsupported os '%s'".formatted(osname));
        }
    }

    @Bean("token-prefix")
    public String tokenPrefix(@Nonnull Binder binder) {
        return binder.bind("custom.token-prefix", String.class).orElse("Bearer");
    }

    @Bean
    public JWTUtil jwtUtil(@Nonnull Binder binder, UserService service) {
        String domain = binder.bind("jwt.domain", String.class).orElse("domain");
        String audience = binder.bind("jwt.audience", String.class).orElse("audience");
        String realm = binder.bind("jwt.realm", String.class).orElse("realm");
        String secret = binder.bind("jwt.secret", String.class).orElse("secret");

        return JWTUtil.builder()
                .domain(domain)
                .audience(audience)
                .realm(realm)
                .secret(secret)
                .userService(service)
                .build();
    }
}
