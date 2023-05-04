package com.example.kunuz.dto.article;

import com.example.kunuz.dto.region.RegionDTO;
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
    private RegionDTO region;
    private LocalDateTime publishedDate;
    private Integer viewCount;
    private Integer likeCount;
    //tagList(name)

}
