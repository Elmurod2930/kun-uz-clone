package com.example.kunuz.dto.article;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ArticleDTO {
    private String title;
    private String description;
    private String content;
    private String attachId;
    private Integer regionId;
    private Integer categoryId;
    private Integer typeId;
//    @NotEmpty(message = "Should provide value")
//    private List<Integer> typeList;
}
