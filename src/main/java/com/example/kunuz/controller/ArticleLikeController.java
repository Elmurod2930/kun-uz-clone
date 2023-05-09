package com.example.kunuz.controller;

import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.service.ArticleLikeService;
import com.example.kunuz.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article-like")
@AllArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

    @GetMapping("/like/{id}")
    public ResponseEntity<Boolean> like(HttpServletRequest request, @PathVariable String id) {
        JwtDTO jwtDTO = JwtUtil.checkForRequiredRole(request);
        return ResponseEntity.ok(articleLikeService.like(id, jwtDTO.getId()));
    }

    @GetMapping("/disLike/{id}")
    public ResponseEntity<Boolean> disLike(HttpServletRequest request, @PathVariable String id) {
        JwtDTO jwtDTO = JwtUtil.checkForRequiredRole(request);
        return ResponseEntity.ok(articleLikeService.disLike(id, jwtDTO.getId()));
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<Boolean> remove(HttpServletRequest request, @PathVariable String id) {
        JwtDTO jwtDTO = JwtUtil.checkForRequiredRole(request);
        return ResponseEntity.ok(articleLikeService.remove(id, jwtDTO.getId()));
    }
}
