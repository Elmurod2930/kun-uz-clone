package com.example.kunuz.dto.comment;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentRequestDTO {
    @NotBlank
    private String content;
    @NotBlank
    private String articleId;

    private Integer replyId;
}
