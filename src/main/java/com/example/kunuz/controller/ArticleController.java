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

    // 5
    @GetMapping("/get5TypeArticle/{typeId}")
    public ResponseEntity<List<ArticleDTO>> get5TypeArticle(@PathVariable("typeId") Integer typeId) {
        List<ArticleDTO> dtoList = articleService.get5TypeArticle(typeId);
        return ResponseEntity.ok(dtoList);
    }

    // 6
    @GetMapping("/get3TypeArticle/{typeId}")
    public ResponseEntity<List<ArticleDTO>> get3TypeArticle(@PathVariable("typeId") Integer typeId) {
        List<ArticleDTO> dtoList = articleService.get3TypeArticle(typeId);
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

    // 9
    @GetMapping("/4-article-by-types")
    public ResponseEntity<List<ArticleShortInfoDTO>> get4ArticleByTypes(@RequestParam("typeId") Integer typeId,
                                                                        @RequestParam("id") String id) {
        List<ArticleShortInfoDTO> list = articleService.get4ArticleByTypes(typeId, id);
        return ResponseEntity.ok(list);
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

    // 13
    @GetMapping("/region-article")
    public ResponseEntity<List<ArticleShortInfoDTO>> getRegionArticle(@RequestParam("id") Integer id, @RequestParam("size") int size,
                                                                      @RequestParam("page") int page) {
        List<ArticleShortInfoDTO> list = articleService.getRegionArticle(id, size, page);
        return ResponseEntity.ok(list);
    }

    // 14
    @GetMapping("/5-category-article/{id}")
    public ResponseEntity<List<ArticleShortInfoDTO>> get5CategoryArticle(@PathVariable Integer id) {
        List<ArticleShortInfoDTO> list = articleService.get5CategoryArticle(id);
        return ResponseEntity.ok(list);
    }

    // 15
    @GetMapping("/category-article")
    public ResponseEntity<List<ArticleShortInfoDTO>> getCategoryArticle(@RequestParam("id") Integer id, @RequestParam("size") int size,
                                                                        @RequestParam("page") int page) {
        List<ArticleShortInfoDTO> list = articleService.getCategoryArticle(id, size, page);
        return ResponseEntity.ok(list);
    }

    // 16
    @PutMapping("/articleViewCount/{id}")
    public ResponseEntity<ArticleDTO> articleViewCount(@PathVariable String id){
        return ResponseEntity.ok(articleService.articleViewCount(id));
    }
    // 17
    @PutMapping("/articleShareCount/{id}")
    public ResponseEntity<ArticleDTO> articleShareCount(@PathVariable String id){
        return ResponseEntity.ok(articleService.articleShareCount(id));
    }
}
