package com.example.kunuz.controller;

import com.example.kunuz.dto.attach.AttachDTO;
import com.example.kunuz.enums.ProfileRole;
import com.example.kunuz.service.AttachService;
import com.example.kunuz.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attach")
@AllArgsConstructor
public class AttachController {

    private final AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO> upload(@RequestParam("file") MultipartFile file) {
        AttachDTO attachDTO = attachService.saveToSystem3(file);
        return ResponseEntity.ok().body(attachDTO);
    }

    @GetMapping(value = "/open_general_by_id/{id}", produces = MediaType.ALL_VALUE)
    public byte[] open_generalById(@PathVariable("id") String id) {
        return attachService.open_generalById(id);
    }

    @GetMapping("/downloadById/{id}")
    public ResponseEntity<Resource> downloadById(@PathVariable("id") String id) {
        Resource file = attachService.downloadById(id);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<AttachDTO>> pagination(@RequestParam("page") int page, @RequestParam("size") int size,
                                                      @RequestHeader("Authorization") String authorization) {
        JwtUtil.getJwtDTO(authorization, ProfileRole.ADMIN);
        return ResponseEntity.ok(attachService.pagination(page, size));
    }


    //==================================================================================================================
    @GetMapping(value = "/open/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] open(@PathVariable("fileName") String fileName) {
        if (fileName != null && fileName.length() > 0) {
            try {
                return this.attachService.loadImage2(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                return new byte[0];
            }
        }
        return null;
    }

    @GetMapping(value = "/open_general_file_name/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open_generalByFileName(@PathVariable("fileName") String fileName) {
        return attachService.open_general2(fileName);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<Resource> download(@PathVariable("fileName") String fileName) {
        Resource file = attachService.download(fileName);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

}
