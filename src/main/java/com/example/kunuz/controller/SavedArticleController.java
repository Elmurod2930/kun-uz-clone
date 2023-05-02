package com.example.kunuz.controller;

import com.example.kunuz.dto.article.SavedArticleDTO;
import com.example.kunuz.dto.article.SavedArticleRequestDTO;
import com.example.kunuz.dto.article.SavedArticleResponseDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.service.SavedArticleService;
import com.example.kunuz.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/saved-article")
@AllArgsConstructor
public class SavedArticleController {
    private final SavedArticleService savedArticleService;

    @PostMapping("/{id}")
    public ResponseEntity<SavedArticleRequestDTO> create(@RequestHeader("Authorization") String authorization,
                                                         @RequestBody @Valid SavedArticleRequestDTO dto,
                                                         @PathVariable("id") String articleId) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        dto.setProfileId(jwtDTO.getId());
        SavedArticleRequestDTO responseDTO = savedArticleService.create(dto, articleId);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id, @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(savedArticleService.delete(id, jwtDTO.getId()));
    }

    @GetMapping("/")
    public ResponseEntity<List<SavedArticleResponseDTO>> getList(@RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(savedArticleService.getList(jwtDTO.getId()));
    }
}
