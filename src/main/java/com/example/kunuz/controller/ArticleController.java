package com.example.kunuz.controller;

import com.example.kunuz.dto.ArticleDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.ArticleService;
import com.example.kunuz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/")
    public ResponseEntity<ArticleDTO> create(@RequestBody ArticleDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(jwtDTO, dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ArticleDTO> update(@RequestHeader("Authorization") String authorization,
                                             @RequestBody ArticleDTO dto, @PathVariable String id) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Boolean> delete(@PathVariable String id,
                                           @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PutMapping("/{id}")
    private ResponseEntity<ArticleDTO> changeStatus(@PathVariable String id) {
        return ResponseEntity.ok(articleService.changeStatus(id));
    }

    @GetMapping("/{typeId}")
    private ResponseEntity<List<ArticleDTO>> articleShortInfo(@PathVariable("typeId") Integer typeId) {
        List<ArticleDTO> dtoList = articleService.articleShortInfo(typeId);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{param}")
    private ResponseEntity<List<ArticleDTO>> articleShortInfo(@PathVariable List<Integer> idList) {
        List<ArticleDTO> dtoList = articleService.articleShortInfo(idList);
        return ResponseEntity.ok(dtoList);
    }
}
