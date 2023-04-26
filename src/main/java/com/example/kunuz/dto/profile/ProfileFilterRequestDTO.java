package com.example.kunuz.dto.profile;

import com.example.kunuz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileFilterRequestDTO {
    private String name;
    private String surname;
    private ProfileRole role;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
}
