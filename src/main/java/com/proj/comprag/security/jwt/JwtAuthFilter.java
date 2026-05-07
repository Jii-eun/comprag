package com.proj.comprag.security.jwt;

<<<<<<< Updated upstream:src/main/java/com/proj/compRAG/security/jwt/JwtAuthFilter.java

=======
import com.proj.comprag.domain.user.repository.UserRepository;
import com.proj.comprag.dto.auth.UserPrincipal;
>>>>>>> Stashed changes:src/main/java/com/proj/comprag/security/jwt/JwtAuthFilter.java
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor    // Lombok 있어야 사용가능한 컴포넌트 -> 없이 하려면 생성자 직접 주입하면 된다고 함
public class JwtAuthFilter extends OncePerRequestFilter {

    private final jwtProvider;
    private final UserRepository userRepository;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        // 아래 경로들로 시작하는 요청은 필터를 거치지 않음
        return path.startsWith("/swagger-ui")
                || path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui.html");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws IOException, ServletException {


        String header = request.getHeader("Authorization"); // 이 글씨는 뭐로정해지나?
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7); // 왜 7임

        if(!jwtProvider.validate(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        UUID userId = jwtProvider.getUserId(token);

        userRepository.findById(userId).ifPresent(user -> {
            // 최소 구현: 권한은 admin 여부만
            var authorities = user.getIsAdmin()
                    ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    :  List.of(new SimpleGrantedAuthority("ROLE_USER"));

            // 1) 그냥 찍기 (이것만으로도 보통 충분)
            logger.info("authorities = "+ authorities);

            // 2) 더 깔끔하게 권한 문자열만 찍기(추천)
            logger.info( "authoritiesStr = "+ authorities.stream()
                    .map(SimpleGrantedAuthority::getAuthority)
                    .toList());


            var principal = new UserPrincipal(
                    user.getId(),
                    user.getEmail(),
                    user.getIsAdmin()
            );

            var auth = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    authorities
            );

            logger.info("principal = "+ auth.getPrincipal());
            logger.info("isAuthenticated = "+ auth.isAuthenticated());

            SecurityContextHolder.getContext().setAuthentication(auth);

        });

        logger.info("JVM zone={}" + java.time.ZoneId.systemDefault());

        filterChain.doFilter(request, response);

    }

}
