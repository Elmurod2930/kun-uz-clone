package com.example.kunuz.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequestDTO {
    @NotBlank(message = "name uz required")
    private String nameUz;
    @NotBlank(message = "name ru required")
    private String nameRu;
    @NotBlank(message = "name en required")
    private String nameEn;
}
