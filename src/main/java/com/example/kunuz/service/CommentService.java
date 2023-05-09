package com.example.kunuz.service;

import com.example.kunuz.dto.comment.CommentDTO;
import com.example.kunuz.dto.comment.CommentFilterRequestDTO;
import com.example.kunuz.dto.comment.CommentRequestDTO;
import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.entity.CommentEntity;
import com.example.kunuz.exps.CommentNotFoundException;
import com.example.kunuz.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProfileService profileService;
    private final ArticleService articleService;

    public CommentRequestDTO create(CommentRequestDTO dto){//, Integer profileId) {
        CommentEntity entity = new CommentEntity();
        entity.setArticleId(dto.getArticleId());
        entity.setContent(dto.getContent());
//        entity.setProfileId(profileId);
        entity.setReplyId(dto.getReplyId());
        commentRepository.save(entity);
        return dto;
    }

    public CommentRequestDTO update(Integer id, CommentRequestDTO dto, JwtDTO jwtDTO) {
        CommentEntity entity = get(id);
        entity.setArticleId(dto.getArticleId());
        entity.setContent(dto.getContent());
        entity.setProfileId(jwtDTO.getId());
        entity.setReplyId(dto.getReplyId());
        commentRepository.save(entity);
        return dto;
    }

    public CommentEntity get(Integer id) {
        Optional<CommentEntity> optional = commentRepository.findById(id);
        if (optional.isEmpty()) {
            throw new CommentNotFoundException("not found exception");
        }
        return optional.get();
    }

    public List<CommentDTO> getCommentByArticleId(String id) {
        List<CommentEntity> entityList = commentRepository.getCommentByArticleId(id);
        List<CommentDTO> dtoList = new LinkedList<>();
        for (CommentEntity entity : entityList) {
            dtoList.add(toCommentDTO(entity));
        }
        return dtoList;
    }

    public CommentDTO toCommentDTO(CommentEntity entity) {
        CommentDTO dto = new CommentDTO();
        dto.setId(entity.getId());
        dto.setArticle(articleService.entityToDTO(entity.getArticle()));
        dto.setContent(entity.getContent());
        dto.setReplyId(entity.getReplyId());
        dto.setProfile(profileService.entityToDto(entity.getProfile()));
        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdateDate(entity.getUpdatedDate());
        return dto;
    }

    public Page<CommentDTO> paging(int size, int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CommentEntity> entityPage = commentRepository.findAll(pageable);
        long totalCount = entityPage.getTotalElements();
        List<CommentEntity> entityList = entityPage.getContent();
        List<CommentDTO> dtoList = new LinkedList<>();
        for (CommentEntity entity : entityList) {
            dtoList.add(toCommentDTO(entity));
        }
        Page<CommentDTO> dtoPage = new PageImpl<>(dtoList, pageable, totalCount);
        return dtoPage;
    }

    public Page<CommentDTO> paging(int size, int page, CommentFilterRequestDTO filterDTO) {
        // TODO: 5/1/2023
        return null;
    }

    public List<CommentDTO> getReplyListByCommentId(String commentId) {
        List<CommentEntity> entityList = commentRepository.getReplyListByCommentId(commentId);
        List<CommentDTO> dtoList = new LinkedList<>();
        for (CommentEntity entity : entityList) {
            dtoList.add(toCommentDTO(entity));
        }
        return dtoList;
    }

    public Boolean delete(Integer id) {
        CommentEntity entity = get(id);
        entity.setVisible(Boolean.FALSE);
        commentRepository.save(entity);
        return true;
    }


}
