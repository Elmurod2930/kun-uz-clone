package com.example.kunuz.service;

import com.example.kunuz.dto.auth.AuthDTO;
import com.example.kunuz.dto.auth.AuthResponseDTO;
import com.example.kunuz.dto.auth.RegistrationDTO;
import com.example.kunuz.dto.auth.RegistrationResponseDTO;
import com.example.kunuz.entity.ProfileEntity;
import com.example.kunuz.enums.GeneralStatus;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.ItemNotFoundException;
import com.example.kunuz.repository.EmailHistoryRepository;
import com.example.kunuz.repository.ProfileRepository;
import com.example.kunuz.util.JwtUtil;
import com.example.kunuz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private MailSenderService mailSenderService;
    @Autowired
    private EmailHistoryRepository emailHistoryRepository;


    public AuthResponseDTO login(AuthDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findByEmailAndPasswordAndVisible(
                dto.getEmail(),
                MD5Util.getMd5Hash(dto.getPassword()),
                true);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Email or password incorrect");
        }
        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(GeneralStatus.ACTIVE)) {
            throw new AppBadRequestException("Wrong status");
        }
        AuthResponseDTO responseDTO = new AuthResponseDTO();
        responseDTO.setName(entity.getName());
        responseDTO.setSurname(entity.getSurname());
        responseDTO.setRole(entity.getRole());
        responseDTO.setJwt(JwtUtil.encode(entity.getId(), entity.getRole()));
        return responseDTO;
    }

    public RegistrationResponseDTO emailVerification(String jwt) {
        // asjkdhaksdh.daskhdkashkdja
        String email = JwtUtil.decodeEmailVerification(jwt);
        Optional<ProfileEntity> optional = profileRepository.findByEmail(email);
        if (optional.isEmpty()) {
            throw new ItemNotFoundException("Email not found.");
        }
        ProfileEntity entity = optional.get();
        if (!entity.getStatus().equals(GeneralStatus.REGISTER)) {
            throw new AppBadRequestException("Wrong status");
        }
        entity.setStatus(GeneralStatus.ACTIVE);
        profileRepository.save(entity);
        return new RegistrationResponseDTO("Registration Done");
    }

    public RegistrationResponseDTO registration(RegistrationDTO dto) {
        isValidRegistrationDTO(dto);
        Optional<ProfileEntity> optionProfileEmail = profileRepository.findByEmail(dto.getEmail());
//        Optional<ProfileEntity> optionProfilePhone = profileRepository.findByPhone(dto.getPhone());
        if (optionProfileEmail.isPresent()) {
            throw new ItemNotFoundException("Email/phone already exists");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setPassword(MD5Util.getMd5Hash(dto.getPassword()));
        entity.setSurname(dto.getSurname());
        entity.setName(dto.getName());
        entity.setStatus(GeneralStatus.REGISTER);
        entity.setRole(ProfileRole.USER);

        mailSenderService.sendRegistrationEmailMime(dto.getEmail());

        profileRepository.save(entity);
        String s = "Verification link was send to email: " + dto.getEmail();
        return new RegistrationResponseDTO(s);
    }

    public void isValidRegistrationDTO(RegistrationDTO dto) {
        if (dto.getName() == null) {
            throw new AppBadRequestException("invalid name");
        }
        if (dto.getSurname() == null) {
            throw new AppBadRequestException("invalid surname");
        }
        if (dto.getPhone() == null && dto.getEmail() == null) {
            throw new AppBadRequestException("invalid email and phone");
        }
        if (dto.getPassword() == null) {
            throw new AppBadRequestException("invalid password");
        }
    }
}
