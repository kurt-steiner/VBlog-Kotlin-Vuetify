package com.steiner.vblog.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.steiner.vblog.util.result.Result;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
public class JWTUtil {
    String secret;

    public JWTUtil(String secret) {
        this.secret = secret;
    }

    public String generateToken(String username) {
        return JWT.create()
                .withClaim("username", username)
                .withExpiresAt(Instant.now().plus(2,  ChronoUnit.DAYS))
                .sign(Algorithm.HMAC256(secret));
    }

    public Result<String, JWTVerificationException> parseToken(String token)  {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return Result.Ok(decodedJWT.getClaim("username").asString());
        } catch (JWTVerificationException exception) {
            return Result.Err(exception);
        }
    }
}
