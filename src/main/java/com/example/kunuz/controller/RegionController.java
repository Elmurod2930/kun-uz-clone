package com.example.kunuz.controller;

import com.example.kunuz.dto.region.RegionDTO;
import com.example.kunuz.dto.region.RegionRequestDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.RegionService;
import com.example.kunuz.util.JwtUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")
@AllArgsConstructor
public class RegionController {
    private final RegionService regionService;

    @PostMapping("/")
    public ResponseEntity<RegionRequestDTO> create(@RequestBody @Valid RegionRequestDTO dto,
                                                   @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization,ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.create(dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<RegionDTO> update(@RequestBody RegionDTO dto,
                                            @RequestHeader("Authorization") String authorization,
                                            @PathVariable Integer id) {
        JwtUtil.getJwtDTO(authorization,ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization,ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.deleteById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<RegionDTO>> getAll(@RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization,ProfileRole.ADMIN);
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/{lang}")
    public ResponseEntity<List<RegionDTO>> getByLang(@PathVariable String lang) {
        return ResponseEntity.ok(regionService.getByLang(lang));
    }
}
