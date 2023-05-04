package com.example.kunuz.controller;

import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.service.ArticleLikeService;
import com.example.kunuz.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article-like")
@AllArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;
    @GetMapping("/like/{id}")
    public ResponseEntity<Boolean> like(@RequestHeader("Authorization") String authorization, @PathVariable String id) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(articleLikeService.like(id, jwtDTO.getId()));
    }

    @GetMapping("/disLike/{id}")
    public ResponseEntity<Boolean> disLike(@RequestHeader("Authorization") String authorization, @PathVariable String id) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(articleLikeService.disLike(id, jwtDTO.getId()));
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<Boolean> remove(@RequestHeader("Authorization") String authorization, @PathVariable String id) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(articleLikeService.remove(id, jwtDTO.getId()));
    }
}
