package com.example.kunuz.dto.article;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SavedArticleRequestDTO {
    private Integer profileId;
    @NotBlank
    private String articleId;
}
