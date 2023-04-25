package com.example.kunuz.controller;

import com.example.kunuz.dto.jwt.JwtDTO;
import com.example.kunuz.dto.profile.ProfileDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.exps.MethodNotAllowedException;
import com.example.kunuz.service.ProfileService;
import com.example.kunuz.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping({"", "/"})
    public ResponseEntity<ProfileDTO> create(@RequestBody ProfileDTO dto,
                                             @RequestHeader("Authorization") String authorization) {

        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(profileService.create(dto, jwtDTO.getId()));
    }

    @PostMapping("/update")
    public ResponseEntity<ProfileDTO> update(@RequestBody ProfileDTO dto,
                                             @RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (!jwtDTO.getRole().equals(ProfileRole.ADMIN)) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        dto.setId(jwtDTO.getId());
        return ResponseEntity.ok(profileService.update(dto));
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
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        dto.setId(jwtDTO.getId());
        return ResponseEntity.ok(profileService.update(dto));
    }
}
