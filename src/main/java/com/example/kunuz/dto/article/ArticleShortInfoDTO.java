package com.example.kunuz.dto.article;

import com.example.kunuz.dto.attach.AttachDTO;
import com.example.kunuz.entity.AttachEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleShortInfoDTO {
    private String id;
    private String title;
    private String description;
    private AttachDTO attach;
    private LocalDateTime publishedDate;
}
