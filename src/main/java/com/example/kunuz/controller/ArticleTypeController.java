package com.example.kunuz.controller;

import com.example.kunuz.dto.articleType.ArticleTypeDTO;
import com.example.kunuz.dto.articleType.ArticleTypeRequestDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.ArticleTypeService;
import com.example.kunuz.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/articleType")
@AllArgsConstructor
public class ArticleTypeController {

    private final ArticleTypeService articleTypeService;

    @PostMapping("/private")
    public ResponseEntity<ArticleTypeRequestDTO> create(@RequestBody @Valid ArticleTypeRequestDTO dto,
                                                        HttpServletRequest request) {
        JwtDTO jwtDTO = JwtUtil.checkForRequiredRole(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.create(dto, jwtDTO.getId()));
    }

    @PostMapping("/private/update/{id}")
    public ResponseEntity<ArticleTypeDTO> updateById(@PathVariable Integer id,
                                                     HttpServletRequest request,
                                                     @RequestBody ArticleTypeDTO dto) {
        JwtUtil.checkForRequiredRole(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.updateById(id, dto));
    }

    @DeleteMapping("/private/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.deleteById(id));
    }

    @GetMapping("/private")
    public ResponseEntity<List<ArticleTypeDTO>> getAll(HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request,ProfileRole.ADMIN);
        return ResponseEntity.ok(articleTypeService.getAll());
    }
    @GetMapping("/{lang}")
    public ResponseEntity<List<ArticleTypeDTO>> getByLang(@PathVariable String lang) {
        return ResponseEntity.ok(articleTypeService.getByLang(lang));
    }
}
