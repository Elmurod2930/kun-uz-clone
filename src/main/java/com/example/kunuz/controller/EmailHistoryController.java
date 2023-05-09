package com.example.kunuz.controller;

import com.example.kunuz.dto.email.EmailHistoryDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.EmailHistoryService;
import com.example.kunuz.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/email-history")
@AllArgsConstructor
public class EmailHistoryController {
    private final EmailHistoryService emailHistoryService;

    @GetMapping("/{email}")
    public ResponseEntity<List<EmailHistoryDTO>> getEmailHistoryByEmail(@PathVariable String email) {
        List<EmailHistoryDTO> list = emailHistoryService.getHistory(email);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/")
    public ResponseEntity<List<EmailHistoryDTO>> getEmailHistoryByDate(@RequestParam("email") String email,
                                                                       @RequestParam("date") LocalDate date) {
        List<EmailHistoryDTO> list = emailHistoryService.getHistory(email, date);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/private/pagination")
    public ResponseEntity<Page<EmailHistoryDTO>> pagination(@RequestParam("page") int page, @RequestParam("size") int size,
                                                            HttpServletRequest request) {
        JwtUtil.checkForRequiredRole(request, ProfileRole.ADMIN);
        return ResponseEntity.ok(emailHistoryService.pagination(page, size));
    }
}
