package com.example.kunuz.dto.region;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegionRequestDTO {
    @NotBlank(message = "name uz required")
    private String nameUz;
    @NotBlank(message = "name ru required")
    private String nameRu;
    @NotBlank(message = "name en required")
    private String nameEn;
}
