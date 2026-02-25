package com.proj.comprag.web.document;

import com.proj.comprag.dto.category.CategoryCreateRequest;
import com.proj.comprag.dto.category.CategoryCreateResponse;
import com.proj.comprag.dto.category.CategoryListResponse;
import com.proj.comprag.service.document.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryCreateResponse> createCategory (@RequestBody CategoryCreateRequest request,
                                                Authentication authentication) {
        String principal = (String) authentication.getPrincipal();
        UUID userId = UUID.fromString(principal);

        UUID cateId = categoryService.createCategory(userId, request);

//        return  ResponseEntity.created(URI.create("/api/category/" + cateId)).body(cateId);
        return ResponseEntity
                .created(URI.create("/api/categories/" + cateId))
                .body(new CategoryCreateResponse(cateId, request.name()));

    }

    @GetMapping
    public ResponseEntity<List<CategoryListResponse>> selectCategories(Authentication authentication) {

        List<CategoryListResponse> categories = categoryService.selectCategories();

        return ResponseEntity.ok(categories);
    }
}
