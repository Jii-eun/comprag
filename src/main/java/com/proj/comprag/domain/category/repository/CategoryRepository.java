package com.proj.comprag.domain.category.repository;

import com.proj.comprag.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    List<Category> findAllByOrderByUpdatedAtDesc();

//    List<Category> findAll(Sort sort);    // 이게 추가되어있지않아도 자동으로 처리됨
}