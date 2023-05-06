package com.example.kunuz.entity;

import com.example.kunuz.enums.LikeStatus;
import com.example.kunuz.enums.PublisherStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_like")
@Getter
@Setter
public class CommentLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "profile_id")
    private Integer profileId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id",updatable = false,insertable = false)
    private ProfileEntity profile;
    @Column(name = "comment_Id")
    private Integer commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_Id",updatable = false,insertable = false)
    private CommentEntity comment;
    @Column(name = "created_date")
    private LocalDateTime createdDate = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private LikeStatus status;

}
