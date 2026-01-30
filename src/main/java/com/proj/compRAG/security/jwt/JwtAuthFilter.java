package com.proj.compRAG.security.jwt;

import com.proj.compRAG.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpResponse;
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

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    // HttpServletRequest와 ServletHTTPRequest의 차이점?
    // 차이 지피티에있음 -> 후에 기록
    // https://chatgpt.com/g/g-p-6967439031188191a5de36dcb1c710a4-ijigyong-seupeuringbuteu-peurojegteu/c/6968715c-d6b8-8320-bbab-1fedfbdcda72
    // Http~가 맞음
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
            var authroties = user.getIsAdmin()
                    ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    :  List.of(new SimpleGrantedAuthority("ROLE_USER"));

            var auth = new UsernamePasswordAuthenticationToken(
                    user.getId().toString(),
                    null,
                    authroties
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
        });

        filterChain.doFilter(request, response);

    }

}
