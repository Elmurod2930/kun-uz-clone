package com.example.kunuz.controller;

import com.example.kunuz.dto.EmailHistoryDTO;
import com.example.kunuz.dto.JwtDTO;
import com.example.kunuz.entity.RegionEntity;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.EmailHistoryService;
import com.example.kunuz.util.JwtUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/email-history")
public class EmailHistoryController {
    @Autowired
    private EmailHistoryService emailHistoryService;

    @GetMapping("/{email}")
    public ResponseEntity<List<EmailHistoryDTO>> getEmailHistoryByEmail(@PathVariable String email) {
        List<EmailHistoryDTO> list = emailHistoryService.getHistory(email);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/")
    public ResponseEntity<List<EmailHistoryDTO>> getEmailHistoryByDate(@RequestParam String email,
                                                                       @RequestParam LocalDate date) {
        List<EmailHistoryDTO> list = emailHistoryService.getHistory(email, date);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<EmailHistoryDTO>> pagination(@RequestParam("page") int page, @RequestParam("size") int size,
                                                            @RequestHeader("Authorization") String authorization) {
        JwtDTO jwtDTO = JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.pagination(page,size));
    }
}
