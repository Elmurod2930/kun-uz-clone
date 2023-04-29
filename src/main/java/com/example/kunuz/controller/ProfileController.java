package com.example.kunuz.controller;

import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.dto.profile.ProfileDTO;
import com.example.kunuz.dto.profile.ProfileRequestDTO;
import com.example.kunuz.dto.profile.ProfileFilterRequestDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.ProfileService;
import com.example.kunuz.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
@AllArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping({"", "/"})
    public ResponseEntity<ProfileRequestDTO> create(@RequestBody @Valid ProfileRequestDTO dto,
                                                    @RequestHeader("Authorization") String authorization) {

        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(dto, jwtDTO.getId()));
    }

    @PostMapping("/update")
    public ResponseEntity<ProfileDTO> update(@RequestBody ProfileDTO dto,
                                                    @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.update(dto, jwtDTO.getId()));
    }


    @GetMapping("/")
    public ResponseEntity<List<ProfileDTO>> getAll() {
        List<ProfileDTO> dtoList = profileService.getAll();
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable("id") Integer id) {
        ProfileDTO dto = profileService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(profileService.deleteById(id));
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<ProfileDTO>> pagination(@RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        Page<ProfileDTO> response = profileService.pagination(page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update-detail")
    public ResponseEntity<ProfileDTO> updateDetail(@RequestBody ProfileDTO dto,
                                                   @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(profileService.update(dto, jwtDTO.getId()));
    }

    @PostMapping("/update-photo/{photoId}")
    public ResponseEntity<ProfileDTO> updatePhoto(@PathVariable String photoId,
                                                         @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization);
        return ResponseEntity.ok(profileService.updatePhoto(jwtDTO, photoId));
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filter(@RequestBody ProfileFilterRequestDTO filterRequestDTO) {
        List<ProfileDTO> dtoList = profileService.filter(filterRequestDTO);
        return ResponseEntity.ok(dtoList);
    }
}
