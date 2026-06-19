package com.proj.comprag.service.auth;

import com.proj.comprag.common.exception.ErrorCode;
import com.proj.comprag.common.exception.custom.UnauthorizedException;
import com.proj.comprag.common.util.AESEncryptionUtil;
import com.proj.comprag.domain.user.entity.User;
import com.proj.comprag.domain.user.repository.UserRepository;
import com.proj.comprag.dto.auth.AuthResponse;
import com.proj.comprag.dto.auth.LoginRequest;
import com.proj.comprag.dto.auth.SignUpRequest;
import com.proj.comprag.dto.auth.UserSummaryResponse;
import com.proj.comprag.security.jwt.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
public class AuthService {

    private final AESEncryptionUtil aes;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    // final 선언
    // 선언 시점에 값이 들어가건, 모든 생성자에서 초기화되어야 함

    public AuthService(AESEncryptionUtil aes, UserRepository userRepository,
                       PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.aes = aes;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
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

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email()).
                orElseThrow(() -> new IllegalArgumentException("이메일 혹은 패스워드가 틀렸습니다.1"));

        Boolean matches = passwordEncoder.matches(request.password(), user.getPasswordHash());

        if(!matches) {
//            throw new IllegalArgumentException("이메일 혹은 패스워드가 틀렸습니다.2");
            // IllegalArgumentException = 메서드에 전달된 인자(argument)가 잘못됐다

            // 커스텀 예외로 처리
            throw new UnauthorizedException(ErrorCode.LOGIN_FAILED);
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
