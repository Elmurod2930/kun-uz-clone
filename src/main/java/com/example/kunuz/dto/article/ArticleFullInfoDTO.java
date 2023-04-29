package com.example.kunuz.dto.article;

import com.example.kunuz.entity.RegionEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ArticleFullInfoDTO {
    private String id;
    private String title;
    private String description;
    private String content;
    private Integer sharedCount;
    private RegionEntity region;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer likeCount;
    //tagList(name)

}
