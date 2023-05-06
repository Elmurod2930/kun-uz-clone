package com.example.kunuz.dto.tagName;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class TagDTO {
    private Integer id;
    private String name;
    private LocalDateTime createdDate;
}
