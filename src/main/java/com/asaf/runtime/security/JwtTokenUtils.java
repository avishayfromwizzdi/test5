package com.asaf.runtime.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {
  private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtils.class);
  private static final String ID = "ID";

  @Value("${security.jwt.issuer:test-5-runtime }")
  private String jwtIssuer;

  @Value("${security.jwt.expiration:604800000 }")
  private long jwtDefaultExpiration;

  @Autowired
  @Qualifier("cachedJWTSecret")
  private SecretKey cachedJWTSecret;

  @Autowired private JwtParser jwtParser;

  public String generateAccessToken(UserSecurityContext user) {

    String id = user.getId();
    return Jwts.builder()
        .setSubject(String.format("%s,%s", id, user.getUsername()))
        .setIssuer(jwtIssuer)
        .setIssuedAt(new Date())
        .setExpiration(new Date(System.currentTimeMillis() + this.jwtDefaultExpiration)) // 1 week
        .claim(ID, id)
        .signWith(cachedJWTSecret)
        .compact();
  }

  public String getId(Jws<Claims> claimsJws) {
    return (String) claimsJws.getBody().get(ID);
  }

  public Jws<Claims> getClaims(String token) {
    try {
      return jwtParser.parseClaimsJws(token);
    } catch (MalformedJwtException ex) {
      logger.error("Invalid JWT token - {}", ex.getMessage());
    } catch (ExpiredJwtException ex) {
      logger.error("Expired JWT token - {}", ex.getMessage());
    } catch (UnsupportedJwtException ex) {
      logger.error("Unsupported JWT token - {}", ex.getMessage());
    } catch (IllegalArgumentException ex) {
      logger.error("JWT claims string is empty - {}", ex.getMessage());
    }
    return null;
  }

  @Configuration
  public static class JwtConfiguration {
    @Value("${security.jwt.secretLocation:/home/project/secret.jwt }")
    private String jwtTokenSecretLocation;

    @Value("${security.jwt.secret:#{null}}")
    private String jwtTokenSecret;

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Qualifier("cachedJWTSecret")
    public SecretKey cachedJWTSecret() {
      return getJWTSecret();
    }

    @Bean
    public JwtParser jwtParser(@Qualifier("cachedJWTSecret") SecretKey cachedJWTSecret) {
      return Jwts.parserBuilder().setSigningKey(cachedJWTSecret).build();
    }

    private SecretKey getJWTSecret() {
      if (jwtTokenSecret != null) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtTokenSecret));
      }
      File file = new File(jwtTokenSecretLocation);
      if (file.exists()) {
        try {
          String cachedJWTSecretStr = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
          return Keys.hmacShaKeyFor(Decoders.BASE64.decode(cachedJWTSecretStr));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      SecretKey cachedJWTSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

      try {
        String secret = Encoders.BASE64.encode(cachedJWTSecret.getEncoded());
        FileUtils.write(file, secret, StandardCharsets.UTF_8);
      } catch (IOException e) {
        e.printStackTrace();
      }

      return cachedJWTSecret;
    }
  }
}
