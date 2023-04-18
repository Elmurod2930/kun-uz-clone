package com.example.kunuz.service;

import com.example.kunuz.dto.ProfileDTO;
import com.example.kunuz.entity.ProfileEntity;
import com.example.kunuz.enums.GeneralStatus;
import com.example.kunuz.exps.AppBadRequestException;
import com.example.kunuz.repository.ProfileRepository;
import com.example.kunuz.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    public ProfileDTO create(ProfileDTO dto, Integer adminId) {
        // check - homework
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
        dto.setId(entity.getId());
        return dto;
    }

    public void isValidProfile(ProfileDTO dto) {
        // throw ...
        if (dto.getEmail().isEmpty() || dto.getEmail().isBlank()) {
            throw new AppBadRequestException("invalid email");
        }
        if (dto.getPhone().isEmpty()) {
            throw new AppBadRequestException("invalid phone");
        }
        if (dto.getSurname() != null) {
            throw new AppBadRequestException("invalid surname");
        }
        if (dto.getName() != null) {
            throw new AppBadRequestException("invalid name");
        }
        if (dto.getRole() != null) {
            throw new AppBadRequestException("invalid role");
        }
        if (dto.getPassword() != null) {
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
        dto.setId(entity.getId());
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
        return entityToDto(entity.orElse(null));
    }

    public ProfileDTO deleteById(Integer id) {
        Optional<ProfileEntity> optional = profileRepository.findById(id);
        if (optional.isPresent()) {
            ProfileEntity entity = optional.get();
            entity.setId(id);
            entity.setVisible(false);
            profileRepository.save(entity);
        }
        throw new AppBadRequestException("not found profile");
    }

    public Page<ProfileDTO> pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable paging = PageRequest.of(page - 1, size);
        Page<ProfileEntity> pageObj = profileRepository.findAll(paging);
        Long totalCount = pageObj.getTotalElements();
        List<ProfileEntity> entityList = pageObj.getContent();
        List<ProfileDTO> dtoList = new LinkedList<>();
        for (ProfileEntity entity : entityList) {
            dtoList.add(entityToDto(entity));
        }

        Page<ProfileDTO> response = new PageImpl<>(dtoList, paging, totalCount);
        return response;
    }

    public ProfileDTO update(ProfileDTO dto) {
        Optional<ProfileEntity> optional = profileRepository.findById(dto.getId());
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
        }
        throw new AppBadRequestException("not found profile");
    }


}
