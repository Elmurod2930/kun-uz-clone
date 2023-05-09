package com.example.kunuz.controller;

import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.service.CommentLikeService;
import com.example.kunuz.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/commentLike")
@AllArgsConstructor
public class CommentLikeController {
    private final CommentLikeService commentLikeService;

    @GetMapping("/like/{id}")
    public ResponseEntity<Boolean> like(HttpServletRequest request, @PathVariable Integer id) {
        JwtDTO jwtDTO = JwtUtil.checkForRequiredRole(request);
        return ResponseEntity.ok(commentLikeService.like(id, jwtDTO.getId()));
    }

    @GetMapping("/disLike/{id}")
    public ResponseEntity<Boolean> disLike(HttpServletRequest request, @PathVariable Integer id) {
        JwtDTO jwtDTO = JwtUtil.checkForRequiredRole(request);
        return ResponseEntity.ok(commentLikeService.disLike(id, jwtDTO.getId()));
    }

    @GetMapping("/remove/{id}")
    public ResponseEntity<Boolean> remove(HttpServletRequest request, @PathVariable Integer id) {
        JwtDTO jwtDTO = JwtUtil.checkForRequiredRole(request);
        return ResponseEntity.ok(commentLikeService.remove(id, jwtDTO.getId()));
    }
}
