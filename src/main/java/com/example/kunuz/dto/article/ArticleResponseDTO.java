package com.example.kunuz.dto.article;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleResponseDTO {
    @NotNull(message = "title required")
    @Size(max = 225, message = "Title must be between 10 and 225 characters")
    private String title;
    @NotBlank(message = "Field must have some value")
    private String description;
    @NotEmpty(message = "Content ")
    private String content;
    private Integer attachId;
    private Integer regionId;
    private Integer categoryId;
    private Integer typeId;
}
