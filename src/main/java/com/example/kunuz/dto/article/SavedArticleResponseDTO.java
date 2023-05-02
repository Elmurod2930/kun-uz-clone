package com.example.kunuz.dto.article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SavedArticleResponseDTO {
    private Integer id;
    private ArticleDTO articleDTO;
}
