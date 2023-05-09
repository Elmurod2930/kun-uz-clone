package com.example.kunuz.controller;

import com.example.kunuz.dto.category.CategoryDTO;
import com.example.kunuz.dto.category.CategoryRequestDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.exps.MethodNotAllowedException;
import com.example.kunuz.service.CategoryService;
import com.example.kunuz.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/private")
    public ResponseEntity<CategoryRequestDTO> create(@RequestBody @Valid CategoryRequestDTO dto,
                                                     HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @PostMapping("/private/{id}")
    public ResponseEntity<CategoryDTO> updateById(@PathVariable Integer id,
                                                  HttpServletRequest request,
                                                  @RequestBody CategoryDTO dto) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.updateById(id, dto));
    }

    @DeleteMapping("/private/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.deleteById(id));
    }

    @GetMapping("/private")
    public ResponseEntity<List<CategoryDTO>> getAll(HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{lang}")
    public ResponseEntity<List<CategoryDTO>> getByLang(@PathVariable String lang) {
        return ResponseEntity.ok(categoryService.getByLang(lang));
    }
}
