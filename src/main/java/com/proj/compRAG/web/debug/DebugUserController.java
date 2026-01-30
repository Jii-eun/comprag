package com.proj.compRAG.web.debug;

import com.proj.compRAG.domain.user.entity.User;
import com.proj.compRAG.domain.user.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/debug/users")
public class DebugUserController {

    private final UserRepository userRepository;

    public DebugUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<com.proj.compRAG.domain.user.entity.User> list() {
        return userRepository.findAll();
    }

    @PostMapping("/write")
    public String write() {
        User u = new User(
                "test1@example.com",
                "test-user",
                "1234",
                false
        );

        userRepository.save(u);
        return "writed" + u.getId();

    }

}
