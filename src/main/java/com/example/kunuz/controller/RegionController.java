package com.example.kunuz.controller;

import com.example.kunuz.dto.JwtDTO;
import com.example.kunuz.dto.RegionDTO;
import com.example.kunuz.entity.RegionEntity;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.exps.MethodNotAllowedException;
import com.example.kunuz.service.RegionService;
import com.example.kunuz.util.JwtUtil;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @PostMapping("/")
    public ResponseEntity<RegionDTO> create(@RequestBody RegionDTO dto,
                                            @RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (jwtDTO.getRole() != ProfileRole.ADMIN) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(regionService.create(dto));
    }

    @PostMapping("/{id}")
    public ResponseEntity<RegionDTO> update(@RequestBody RegionDTO dto,
                                            @RequestHeader("Authorization") String authorization,
                                            @PathVariable Integer id) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (jwtDTO.getRole() != ProfileRole.ADMIN) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(regionService.update(dto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteById(@PathVariable Integer id,
                                              @RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (jwtDTO.getRole() != ProfileRole.ADMIN) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(regionService.deleteById(id));
    }

    @GetMapping("/")
    public ResponseEntity<List<RegionDTO>> getAll(@RequestHeader("Authorization") String authorization) {
        String[] str = authorization.split(" ");
        String jwt = str[1];
        JwtDTO jwtDTO = JwtUtil.decode(jwt);
        if (jwtDTO.getRole() != ProfileRole.ADMIN) {
            throw new MethodNotAllowedException("Method not allowed");
        }
        return ResponseEntity.ok(regionService.getAll());
    }

    @GetMapping("/{lang}")
    public ResponseEntity<List<RegionDTO>> getByLang(@PathVariable String lang) {
        return ResponseEntity.ok(regionService.getByLang(lang));
    }
}
