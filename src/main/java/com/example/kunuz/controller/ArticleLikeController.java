package com.example.kunuz.controller;

import com.example.kunuz.service.ArticleLikeService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article-like")
@AllArgsConstructor
public class ArticleLikeController {
    private final ArticleLikeService articleLikeService;

}
