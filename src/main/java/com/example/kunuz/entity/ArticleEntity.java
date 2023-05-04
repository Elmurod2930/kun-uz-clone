package com.example.kunuz.entity;

import com.example.kunuz.enums.PublisherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@Table(name = "article")
@Entity
@NoArgsConstructor
public class ArticleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "title", columnDefinition = "text")
    private String title;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "content", columnDefinition = "text")
    private String content;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PublisherStatus status = PublisherStatus.NOT_PUBLISHED;
    @Column(name = "shared_count")
    private Integer sharedCount = 0;
    @Column(name = "attach_id")
    private String attachId;
    @ManyToOne
    @JoinColumn(name = "attach_id",insertable = false,updatable = false)
    private AttachEntity attach;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private RegionEntity region;
    @ManyToOne
    @JoinColumn(name = "category_id",insertable = false,updatable = false)
    private CategoryEntity category;
    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private ProfileEntity moderator;

    @Column(name = "publisher_id")
    private Integer publisherId;
    @ManyToOne
    @JoinColumn(name = "publisher_id",insertable = false,updatable = false)
    private ProfileEntity publisher;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Column(name = "published_date")
    private LocalDateTime publishedDate;
    @Column(name = "visible")
    private Boolean visible = Boolean.TRUE;
    @Column(name = "view_count")
    private Integer viewCount;
    @Column(name = "type_id")
    private Integer typeId;
    @Column(name = "like_count")
    private Integer likeCount;
    @Column(name = "disLike_count")
    private Integer disLikeCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id",insertable = false,updatable = false)
    private ArticleTypeEntity type;



    public ArticleEntity(String id, String title, String description, String attachId, LocalDateTime publishedDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.attachId = attachId;
        this.publishedDate = publishedDate;
    }
}