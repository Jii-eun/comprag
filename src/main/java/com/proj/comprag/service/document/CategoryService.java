package com.proj.comprag.service.document;

import com.proj.comprag.domain.category.entity.Category;
import com.proj.comprag.domain.category.repository.CategoryRepository;
import com.proj.comprag.dto.category.CategoryCreateRequest;
import com.proj.comprag.dto.category.CategoryListResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public UUID createCategory(UUID userId, CategoryCreateRequest request) {

        UUID cateId = UUID.randomUUID();
        OffsetDateTime now = OffsetDateTime.now();

        Category cate = new Category(
              cateId,
              request.name(),
              request.description(),
              now,
              now
        );

        categoryRepository.save(cate);

        return cateId;
    }

    @Transactional
    public List<CategoryListResponse> selectCategories() {

//        List<Category> categories = categoryRepository.findAllByOrderByUpdatedAtDesc();
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "updatedAt"));

        List<CategoryListResponse> results = categories.stream()
                .map(d -> new CategoryListResponse(
                        d.getId(),
                        d.getName(),
                        d.getDescription(),
                        d.getCreatedAt()
                )).toList();

        return results;

    }
}
