package com.steiner.vblog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.steiner.vblog.Constants;
import com.steiner.vblog.exception.ServerInternalException;
import com.steiner.vblog.model.User;
import com.steiner.vblog.service.UserService;
import com.steiner.vblog.util.result.Result;
import jakarta.annotation.Nonnull;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.steiner.vblog.Constants.USERID_KEY;

@lombok.Builder
public class JWTUtil {
    @Nonnull
    @lombok.NonNull
    UserService userService;

    @Nonnull
    @lombok.NonNull
    String domain;

    @Nonnull
    @lombok.NonNull
    String audience;

    @Nonnull
    @lombok.NonNull
    String realm;

    @Nonnull
    @lombok.NonNull
    String secret;

    @Nonnull
    public String generateToken(@Nonnull User user) {
        return JWT.create()
                .withClaim(Constants.USERNAME_KEY, user.name)
                .withClaim(Constants.USERID_KEY, user.id)
                .withExpiresAt(Instant.now().plus(2,  ChronoUnit.DAYS))
                .sign(Algorithm.HMAC256(secret));
    }

    public int parseTokenIntoId(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim(USERID_KEY).asInt();
    }

    public Result<User, JWTVerificationException> tryParseToken(String token) {
        try {
            int id = this.parseTokenIntoId(token);
            User user = userService.findOne(id)
                    .orElseThrow(() -> new ServerInternalException("cannot find this user"));
            return Result.Ok(user);
        } catch (JWTVerificationException e) {
            return Result.Err(e);
        }
    }
}
