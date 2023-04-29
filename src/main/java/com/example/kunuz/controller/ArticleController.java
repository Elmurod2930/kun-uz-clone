package com.example.kunuz.controller;

import com.example.kunuz.dto.article.ArticleDTO;
import com.example.kunuz.dto.article.ArticleFullInfoDTO;
import com.example.kunuz.dto.article.ArticleResponseDTO;
import com.example.kunuz.dto.article.ArticleShortInfoDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.ArticleService;
import com.example.kunuz.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/article")
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping("/")
    public ResponseEntity<ArticleResponseDTO> create(@RequestBody @Valid ArticleResponseDTO dto,
                                                     @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.create(dto, jwtDTO.getId()));
    }

    @PostMapping("/{id}")
    public ResponseEntity<ArticleDTO> update(@RequestHeader("Authorization") String authorization,
                                             @RequestBody ArticleDTO dto, @PathVariable String id) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable String id,
                                          @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.MODERATOR);
        return ResponseEntity.ok(articleService.delete(id));
    }

    @PutMapping("/{id}")
    private ResponseEntity<ArticleDTO> changeStatus(@PathVariable String id) {
        return ResponseEntity.ok(articleService.changeStatus(id));
    }

    @GetMapping("/{typeId}")
    public ResponseEntity<List<ArticleDTO>> articleShortInfo(@PathVariable("typeId") Integer typeId) {
        List<ArticleDTO> dtoList = articleService.articleShortInfo(typeId);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{list}")
    public ResponseEntity<List<ArticleDTO>> articleShortInfo(@PathVariable List<Integer> list) {
        List<ArticleDTO> dtoList = articleService.articleShortInfo(list);
        return ResponseEntity.ok(dtoList);
    }

    // ===========================================================================================================

    // 8
    @GetMapping("/get_by_id_and_lang")
    public ResponseEntity<ArticleFullInfoDTO> articleFullInfo(@RequestParam("id") String id,
                                                              @RequestParam("lang") String lang) {
        ArticleFullInfoDTO dto = articleService.articleFullInfo(id, lang);
        return ResponseEntity.ok(dto);
    }

    // 10
    @GetMapping("/4most_read")
    public ResponseEntity<List<ArticleShortInfoDTO>> articleShortInfo() {
        List<ArticleShortInfoDTO> dtoList = articleService.articleShortInfo();
        return ResponseEntity.ok(dtoList);
    }

    // 12
    @GetMapping("/get-by-type-and-region")
    public ResponseEntity<List<ArticleShortInfoDTO>> articleShortInfo(@RequestParam("type") String type,
                                                                      @RequestParam("region") String regionName) {
        List<ArticleShortInfoDTO> dtoList = articleService.articleShortInfo(type, regionName);
        return ResponseEntity.ok(dtoList);
    }


}
