package com.steiner.vblog;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.steiner.vblog.dto.request.*;
import com.steiner.vblog.model.User;
import com.steiner.vblog.model.article.ArticleStatus;
import com.steiner.vblog.util.Response;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

public class ApiTest {
    RestClient client = RestClient.builder().build();

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testRegister() {
        String stringResponse = client.post()
                .uri("http://localhost:8080/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PostUserRequest("steiner", "123456", null, null, null))
                .retrieve()
                .body(String.class);

        System.out.println(stringResponse);
    }

    @Test
    public void testFakeDataInject() throws JsonProcessingException {
        String stringLoginResponse = client.post()
                .uri("http://localhost:8080/login")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new LoginRequest("steiner", "123456"))
                .retrieve()
                .body(String.class);

        System.out.println(stringLoginResponse);

        Response.Ok<String> loginResponse = objectMapper.readValue(
                stringLoginResponse,
                objectMapper.getTypeFactory().constructParametricType(
                        Response.Ok.class,
                        String.class
                )
        );

        String authentication = "Bearer %s".formatted(loginResponse.data);

        String stringUserResponse = client.get()
                .uri("http://localhost:8080/user")
                .header(Constants.AuthorizationHeader, authentication)
                .retrieve()
                .body(String.class);

        Response.Ok<User> userResponse = objectMapper.readValue(
                stringUserResponse,
                objectMapper.getTypeFactory().constructParametricType(
                        Response.Ok.class,
                        User.class
                )
        );

        User currentUser = Objects.requireNonNull(userResponse.data);

        List.of(
                new PostCategoryRequest("category 1", currentUser.id),
                new PostCategoryRequest("category 2", currentUser.id)
        ).forEach(request -> {
            client.post()
                    .uri("http://localhost:8080/category")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.AuthorizationHeader, authentication)
                    .body(request)
                    .retrieve()
                    .body(String.class);
        });


        List.of(
                new PostTagRequest("tag 1", currentUser.id),
                new PostTagRequest("tag 2", currentUser.id)
        ).forEach(request -> {
            client.post()
                    .uri("http://localhost:8080/tag")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(Constants.AuthorizationHeader, authentication)
                    .body(request)
                    .retrieve()
                    .body(String.class);
        });

        PostArticleRequest postArticleRequest = PostArticleRequest.builder()
                .title("hello")
                .htmlContent("<h1> Hello World </h1>")
                .markdownContent("# Hello World")
                .status(ArticleStatus.Published)
                .authorId(currentUser.id)
                .build();

        client.post()
                .uri("http://localhost:8080/article")
                .contentType(MediaType.APPLICATION_JSON)
                .header(Constants.AuthorizationHeader, authentication)
                .body(postArticleRequest)
                .retrieve()
                .body(String.class);
    }
}
