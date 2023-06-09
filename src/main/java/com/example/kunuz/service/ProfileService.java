package com.example.kunuz.service;

import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.dto.profile.ProfileDTO;
import com.example.kunuz.dto.profile.ProfileFilterRequestDTO;
import com.example.kunuz.dto.profile.ProfileRequestDTO;
import com.example.kunuz.entity.AttachEntity;
import com.example.kunuz.entity.ProfileEntity;
import com.example.kunuz.enums.GeneralStatus;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.exps.AttachNotFoundException;
import com.example.kunuz.exps.ProfileNotFoundException;
import com.example.kunuz.repository.ProfileCustomRepository;
import com.example.kunuz.repository.ProfileRepository;
import com.example.kunuz.util.MD5Util;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileService {
    private ProfileRepository profileRepository;
    private AttachService attachService;
    private ProfileCustomRepository profileCustomRepository;

    public ProfileRequestDTO create(ProfileRequestDTO dto, Integer adminId) {
        isValidProfile(dto);
        ProfileEntity entity = new ProfileEntity();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhone(dto.getPhone());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setPassword(MD5Util.getMd5Hash(dto.getPassword())); // MD5 ?
        entity.setCreatedDate(LocalDateTime.now());
        entity.setVisible(true);
        entity.setStatus(GeneralStatus.ACTIVE);
        entity.setPrtId(adminId);
        profileRepository.save(entity);  // save profile
        dto.setPassword(null);
        return dto;
    }

    public void isValidProfile(ProfileRequestDTO dto) {
        if (dto.getEmail().isEmpty() || dto.getEmail().isBlank()) {
            throw new AppBadRequestException("invalid email");
        }
        if (dto.getPhone().isEmpty()) {
            throw new AppBadRequestException("invalid phone");
        }
        if (dto.getSurname().isEmpty()) {
            throw new AppBadRequestException("invalid surname");
        }
        if (dto.getName().isEmpty()) {
            throw new AppBadRequestException("invalid name");
        }
        if (dto.getRole() == null) {
            throw new AppBadRequestException("invalid role");
        }
        if (dto.getPassword() == null) {
            throw new AppBadRequestException("invalid password");
        }
    }

    public List<ProfileDTO> getAll() {
        Iterable<ProfileEntity> entityList = profileRepository.findAll();
        List<ProfileDTO> dtoList = new LinkedList<>();
        for (ProfileEntity entity : entityList) {
            dtoList.add(entityToDto(entity));
        }
        return dtoList;
    }

    public ProfileDTO entityToDto(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        dto.setPhone(entity.getPhone());
        dto.setName(entity.getName());
        dto.setPassword(entity.getPassword());
        dto.setSurname(entity.getSurname());
        return dto;
    }

    public ProfileDTO getById(Integer id) {
        Optional<ProfileEntity> entity = profileRepository.findById(id);
        assert null != entity.orElse(null);
        return entityToDto(entity.orElse(null));
    }

    public Boolean deleteById(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isPresent()) {
            ProfileEntity entity = optional.get();
            entity.setId(id);
            entity.setVisible(false);
            profileRepository.save(entity);
            return true;
        }
        throw new AppBadRequestException("not found profile");
    }

    public Page<ProfileDTO> pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable paging = PageRequest.of(page - 1, size, sort);
        Page<ProfileEntity> pageObj = profileRepository.findAll(paging);
        long totalCount = pageObj.getTotalElements();
        List<ProfileEntity> entityList = pageObj.getContent();
        List<ProfileDTO> dtoList = new LinkedList<>();
        for (ProfileEntity entity : entityList) {
            dtoList.add(entityToDto(entity));
        }

        return new PageImpl<>(dtoList, paging, totalCount);
    }

    public ProfileDTO update(ProfileDTO dto, Integer adminId) {
        Optional<ProfileEntity> optional = profileRepository.findById(adminId);
        if (optional.isPresent()) {
            ProfileEntity entity = optional.get();
            if (dto.getPhone() != null) {
                entity.setPhone(dto.getPhone());
            }
            if (dto.getSurname() != null) {
                entity.setSurname(dto.getSurname());
            }
            if (dto.getName() != null) {
                entity.setName(dto.getName());
            }
            if (dto.getRole() != null) {
                entity.setRole(dto.getRole());
            }
            profileRepository.save(entity);
            return dto;
        }
        throw new AppBadRequestException("not found profile");
    }

    public ProfileDTO updatePhoto(JwtDTO jwtDTO, String photoId) {
        Optional<ProfileEntity> optionalProfile = profileRepository.findById(jwtDTO.getId());
        if (optionalProfile.isEmpty()) {
            throw new ProfileNotFoundException("not found profile");
        }
        AttachEntity attachEntity = attachService.get(photoId);
        if (attachEntity == null) {
            throw new AttachNotFoundException("not found attach");
        }
        ProfileEntity entity = optionalProfile.get();
        // todo  entity.getPhoto() photo delete qilish kk
        entity.setAttach(attachEntity);
        profileRepository.save(entity);
        return entityToDto(entity);
    }

    public List<ProfileDTO> filter(ProfileFilterRequestDTO filterRequestDTO) {
        List<ProfileEntity> entityList = profileCustomRepository.filter(filterRequestDTO);
        List<ProfileDTO> dtoList = new LinkedList<>();
        for (ProfileEntity entity : entityList) {
            dtoList.add(entityToDto(entity));
        }
        return dtoList;
    }

    public ProfileEntity get(Integer moderId) {
        Optional<ProfileEntity> optional = profileRepository.findById(moderId);
        if (optional.isEmpty()) {
            throw new ProfileNotFoundException("profile not found");
        }
        return optional.get();
    }
}
