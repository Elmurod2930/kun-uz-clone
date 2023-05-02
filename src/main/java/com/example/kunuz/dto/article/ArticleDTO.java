package com.example.kunuz.dto.article;

import com.example.kunuz.dto.attach.AttachDTO;
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
    private AttachDTO attach;
//    @NotEmpty(message = "Should provide value")
//    private List<Integer> typeList;
}
