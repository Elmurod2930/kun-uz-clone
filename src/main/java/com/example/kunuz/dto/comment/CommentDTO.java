package com.example.kunuz.dto.comment;

import com.example.kunuz.dto.article.ArticleDTO;
import com.example.kunuz.dto.profile.ProfileDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CommentDTO {
    private Integer id;
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime updateDate;
    private ProfileDTO profile;
    private String content;
    private ArticleDTO article;
    private Integer replyId;
    private boolean visible = Boolean.TRUE;
}
