package com.example.kunuz.dto.profile;

import com.example.kunuz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ProfileDTO {
    private Integer id;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private ProfileRole role;
}
