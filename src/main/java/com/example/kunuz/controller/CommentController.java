package com.example.kunuz.controller;

import com.example.kunuz.dto.comment.CommentDTO;
import com.example.kunuz.dto.comment.CommentFilterRequestDTO;
import com.example.kunuz.dto.comment.CommentRequestDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.CommentService;
import com.example.kunuz.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 1
    @PostMapping("/")
    public ResponseEntity<CommentRequestDTO> create(@RequestBody @Valid CommentRequestDTO dto,
                                                    @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        CommentRequestDTO requestDTO = commentService.create(dto, jwtDTO.getId());
        return ResponseEntity.ok(requestDTO);
    }

    // 2
    @PostMapping("/update")
    public ResponseEntity<CommentRequestDTO> update(@RequestHeader("Authorization") String authorization,
                                                    @RequestBody @Valid CommentRequestDTO dto,
                                                    @PathVariable Integer id) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        CommentRequestDTO requestDTO = commentService.update(id, dto, jwtDTO);
        return ResponseEntity.ok(requestDTO);
    }

    // 3
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id, @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(commentService.delete(id));
    }

    // 4
    @GetMapping("/commentList/{id}")
    public ResponseEntity<List<CommentDTO>> get(@PathVariable String id) {
        List<CommentDTO> list = commentService.getCommentByArticleId(id);
        return ResponseEntity.ok(list);
    }

    // 5
    @GetMapping("/pagination")
    public ResponseEntity<Page<CommentDTO>> paging(@RequestHeader("Authorization") String authorization,
                                                   @RequestParam("size") int size, @RequestParam("page") int page) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        Page<CommentDTO> dtoPage = commentService.paging(size, page);
        return ResponseEntity.ok(dtoPage);
    }

    // 6
    @PostMapping("/filter")
    public ResponseEntity<Page<CommentDTO>> filter(@RequestHeader("Authorization") String authorization,
                                                   @RequestParam("size") int size, @RequestParam("page") int page,
                                                   @RequestBody CommentFilterRequestDTO filterDTO) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        Page<CommentDTO> dtoPage = commentService.paging(size, page, filterDTO);
        return ResponseEntity.ok(dtoPage);
    }

    // 7
    @GetMapping("/{commentId}")
    public ResponseEntity<List<CommentDTO>> getReplyListByCommentId(@PathVariable String commentId) {
        List<CommentDTO> dtoList = commentService.getReplyListByCommentId(commentId);
        return ResponseEntity.ok(dtoList);
    }

}
