package com.example.kunuz.controller;

import com.example.kunuz.dto.category.CategoryDTO;
import com.example.kunuz.dto.category.CategoryRequestDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.exps.MethodNotAllowedException;
import com.example.kunuz.service.CategoryService;
import com.example.kunuz.util.JwtUtil;
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

    @PostMapping("/")
    public ResponseEntity<CategoryRequestDTO> create(@RequestBody @Valid CategoryRequestDTO dto,
                                                     @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateById(@PathVariable Integer id,
                                                  @RequestHeader("Authorization") String authorization,
                                                  @RequestBody CategoryDTO dto) {
        JwtUtil.getJwtDTO(authorization,ProfileRole.ADMIN);
        return ResponseEntity.ok(categoryService.updateById(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              @RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRole().equals(ProfileRole.ADMIN)) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(categoryService.deleteById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<CategoryDTO>> getAll(@RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRole().equals(ProfileRole.ADMIN)) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(categoryService.getAll());
    }

    @GetMapping("/{lang}")
    public ResponseEntity<List<CategoryDTO>> getByLang(@PathVariable String lang) {
        return ResponseEntity.ok(categoryService.getByLang(lang));
    }
}
