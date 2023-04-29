package com.example.kunuz.dto.profile;

import com.example.kunuz.enums.ProfileRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileRequestDTO {
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String password;
    private ProfileRole role;
}
