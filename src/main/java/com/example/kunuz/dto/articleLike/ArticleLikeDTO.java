package com.example.kunuz.dto.articleLike;

import com.example.kunuz.enums.PublisherStatus;

import java.time.LocalDateTime;

public class ArticleLikeDTO {
    private Integer id;
    private Integer profileId;
    private String articleId;
    private LocalDateTime createdDate;
    private PublisherStatus status;
}
