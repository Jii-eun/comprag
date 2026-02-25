package com.proj.comprag.domain.user.repository;

import com.proj.comprag.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    // 쿼리 메소드
    // Optional<T>는 “값이 있을 수도/없을 수도 있음”을 표현하는 컨테이너
    Optional<User> findByEmail(String email);

//    List<User> findAllOrderByCreatedAt();

//    List<User> findAllById(UUID id);
//
//    User findOneById(UUID userId);
}
