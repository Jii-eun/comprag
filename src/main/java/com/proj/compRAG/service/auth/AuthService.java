package com.proj.compRAG.service.auth;

import com.proj.compRAG.domain.user.entity.User;
import com.proj.compRAG.domain.user.repository.UserRepository;
import com.proj.compRAG.dto.auth.AuthResponse;
import com.proj.compRAG.dto.auth.LoginRequest;
import com.proj.compRAG.dto.auth.SignUpRequest;
import com.proj.compRAG.dto.auth.UserSummaryResponse;
import com.proj.compRAG.security.jwt.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    // final 선언
    // 선언 시점에 값이 들어가건, 모든 생성자에서 초기화되어야 함

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public void signUp(SignUpRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(
                request.email(),
                passwordHash,
                request.name(),
                false
        );

        userRepository.save(user);
    }

    //    @Transactional(readOnly)
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email()).
                orElseThrow(() -> new IllegalArgumentException("이메일 혹은 패스워드가 틀렸습니다.1"));

        Boolean matches = passwordEncoder.matches(request.password(), user.getPasswordHash());

        if(!matches) {
            throw new IllegalArgumentException("이메일 혹은 패스워드가 틀렸습니다.2");
        }

        String accessToken = jwtProvider.createAccessToken(user);

        UserSummaryResponse userSummary = new UserSummaryResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getIsAdmin()
        );

        return new AuthResponse(
                accessToken,
                userSummary
        );
    }

}
