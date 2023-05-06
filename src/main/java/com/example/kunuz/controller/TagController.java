package com.example.kunuz.controller;

import com.example.kunuz.dto.tagName.TagDTO;
import com.example.kunuz.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ap1/v1/tag")
@AllArgsConstructor
public class TagController {
    private final TagService tagNameService;

    @PostMapping("/{name}")
    public RequestEntity<TagDTO> create(@PathVariable String name, @RequestHeader("Authorization") String authorization) {
        return null;
    }
}
