package com.example.kunuz.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class EmailHistoryDTO {
    private Integer id;
    private String massage;
    private String email;
    private LocalDateTime createdDate;
}
