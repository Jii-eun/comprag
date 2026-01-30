package com.proj.compRAG.security.jwt;

import com.proj.compRAG.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;

// @Component = 스프링이 Bean으로 등록해서 DI(주입)할 수 있게 해주는 표시
// 따라서 AuthService 생성자에 JwtProvider를 넣어도 스프링이 알아서 주입해줌 없으면 빈으로 몰라서 생성자에 넣을 수 없음
// OR @Configuration을 class에 @Bean을 메소드에 붙여서, 리턴 객체를 Bean으로 (일일히) 등록
//  => 라이브러리클래스 처럼 내가 만든 클래스가 아닐때, 조건부로 메소드를 나눠야할 때 @Bean 사용
@Component
public class JwtProvider {

    // 토큰 생성,검증,클레임 추출

    private final SecretKey secretKey;
    private final long expMinutes;

    public JwtProvider(
            @Value("${spring.security.jwt.secret}")  String secretKeyString,
            @Value("${spring.security.jwt.access-token-exp-minutes}")long expMinutes) {

        this.secretKey = Keys.hmacShaKeyFor(secretKeyString.getBytes(StandardCharsets.UTF_8));
        this.expMinutes = expMinutes;
    }

    public String createAccessToken(User user){
        Instant now = Instant.now();
        Instant exp = now.plus(expMinutes, ChronoUnit.MINUTES);
        //  ?? Instant객체는 뭐지?
        // ChronUnit은뭐야?시간 체크하는거라는건 대충 감이옴

        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("admin", user.getIsAdmin())
                .issuedAt(Date.from(now))
                .expiration(Date.from(exp))
                .signWith(secretKey)
                .compact();
    }

    // boolean과 Boolean의 차이는..?
    public boolean validate(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public UUID getUserId(String token) {
        Claims claims = Jwts.parser().verifyWith(secretKey).build()
                .parseClaimsJws(token).getPayload();
        // ?? Claim 객체
        // ?? getpayload?
        return UUID.fromString(claims.getSubject());
    }

}
