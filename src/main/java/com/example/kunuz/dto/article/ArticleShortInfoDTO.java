package com.example.kunuz.dto.article;

import com.example.kunuz.entity.AttachEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private AttachEntity attach;
    private LocalDateTime publishedDate;
}
