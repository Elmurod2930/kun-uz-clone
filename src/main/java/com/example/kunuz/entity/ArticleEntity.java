package com.example.kunuz.entity;

import com.example.kunuz.enums.PublisherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "article")
@Setter
@Getter
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "content")
    private String content;
    @Column(name = "shared_count")
    private Integer sharedCount;
    @Column(name = "image_id")
    private String imageId;
    @Column(name = "region_id")
    private Integer regionId;
    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "moderator_id")
    private Integer moderatorId;
    @Column(name = "publisher_id")
    private Integer publisherId;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PublisherStatus status;
    @Column(name = "created_date")
    private LocalDateTime createdDate;
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "visible")
    private Boolean visible;
    @Column(name = "view_count")
    private Integer viewCount;
}