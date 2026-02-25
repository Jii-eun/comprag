package com.proj.comprag.security.jwt;

import com.proj.comprag.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
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

//        System.out.println("-------HIT " + request.getRequestURI());
//        System.out.println("-------AUTH " + request.getHeader("Authorization"));

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
//        System.out.println("-------userId = " + userId);

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


            var auth = new UsernamePasswordAuthenticationToken(
                    user.getId().toString(),
                    null,
                    authorities
            );

            logger.info("principal = "+ auth.getPrincipal());
            logger.info("isAuthenticated = "+ auth.isAuthenticated());

            SecurityContextHolder.getContext().setAuthentication(auth);

//            var auth = new UserPrincipal(
//                    user.getId(),
//                    user.getEmail(),
//                    user.getIsAdmin()
//            );
        });

        var userOpt = userRepository.findById(userId);
//        System.out.println("-------user found? " + userOpt.isPresent());

        logger.info("JVM zone={}" + java.time.ZoneId.systemDefault());

        filterChain.doFilter(request, response);

    }

}
