package com.example.kunuz.service;

import com.example.kunuz.dto.attach.AttachDTO;
import com.example.kunuz.entity.AttachEntity;
import com.example.kunuz.exps.ItemNotFoundException;
import com.example.kunuz.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${server.host}")
    private String serverHost;

    public AttachDTO saveToSystem3(MultipartFile file) {
        try {
            String pathFolder = getYmDString(); // 2022/04/23
            File folder = new File("attaches/" + pathFolder);  // attaches/2023/04/26
            if (!folder.exists()) {
                folder.mkdirs();
            }
            byte[] bytes = file.getBytes();
            String extension = getExtension(file.getOriginalFilename());

            AttachEntity attachEntity = new AttachEntity();
            attachEntity.setId(UUID.randomUUID().toString());
            attachEntity.setCreatedData(LocalDateTime.now());
            attachEntity.setExtension(extension);
            attachEntity.setSize(file.getSize());
            attachEntity.setPath(pathFolder);
            attachEntity.setOriginalName(file.getOriginalFilename());
            attachRepository.save(attachEntity);

            Path path = Paths.get("attaches/" + pathFolder + "/" + attachEntity.getId() + "." + extension);
            // attaches/2023/04/26/uuid().jpg
            Files.write(path, bytes);

            AttachDTO dto = new AttachDTO();
            dto.setId(attachEntity.getId());
            dto.setOriginalName(file.getOriginalFilename());
            dto.setUrl(serverHost + "/api/v1/attach/open/" + attachEntity.getId() + "." + extension);

            return dto;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] open_generalById(String id) {
        AttachEntity attachEntity = get(id);
        byte[] data;
        try {
            Path file = Paths.get("attaches/" + attachEntity.getPath() + "/" + id + "." + attachEntity.getExtension());
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public Resource downloadById(String id) {
        try {
            AttachEntity attachEntity = get(id);
            Path file = Paths.get("attaches/" + attachEntity.getPath() + "/" + id + "." + attachEntity.getExtension());
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public Page<AttachDTO> pagination(int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdDate");
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<AttachEntity> pageObj = attachRepository.findAll(pageable);
        Long totalCount = pageObj.getTotalElements();
        List<AttachEntity> entityList = pageObj.getContent();
        List<AttachDTO> dtoList = new LinkedList<>();
        for (AttachEntity entity : entityList) {
            AttachDTO dto = new AttachDTO();
            dto.setSize(entity.getSize());
            dto.setPath(entity.getPath());
            dto.setExtension(entity.getExtension());
            dto.setId(entity.getId());
            dto.setCreatedData(entity.getCreatedData());
            dtoList.add(dto);
        }

        Page<AttachDTO> dtoPage = new PageImpl<>(dtoList, pageable, totalCount);
        return dtoPage;
    }

    //======================================================================================================================
    public String saveToSystem(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            // attaches/test_imge_1.jpg
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String saveToSystem2(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }
            byte[] bytes = file.getBytes();
            String extension = getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString();
            Path path = Paths.get("attaches/" + fileName + "." + extension);
            // attaches/test_imge_1.jpg
            // attaches/uuid().jpg
            Files.write(path, bytes);
            return fileName + "." + extension;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public byte[] loadImage(String fileName) {
        byte[] imageInByte;

        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + fileName));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    public byte[] loadImage2(String attachId) {
//        int lastIndex = attachName.lastIndexOf(".");
//        String id = attachName.substring(0, lastIndex);
        AttachEntity attachEntity = get(attachId);
        byte[] data;
        try {
            Path file = Paths.get("attaches/" + attachEntity.getPath() + "/" + attachId + "." + attachEntity.getExtension());
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] open_general(String attachName) {
        byte[] data;
        try {
            Path file = Paths.get("attaches/" + attachName);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] open_general2(String attachName) {
        // 20f0f915-93ec-4099-97e3-c1cb7a95151f.jpg
        int lastIndex = attachName.lastIndexOf(".");
        String id = attachName.substring(0, lastIndex);
        AttachEntity attachEntity = get(id);
        byte[] data;
        try {                                                     // attaches/2023/4/25/20f0f915-93ec-4099-97e3-c1cb7a95151f.jpg
            Path file = Paths.get("attaches/" + attachEntity.getPath() + "/" + attachName);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);
        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public AttachEntity get(String id) {
        return attachRepository.findById(id).orElseThrow(() -> {
            throw new ItemNotFoundException("Attach not found");
        });
    }

    public Resource download(String fileName) {
        try {
            int lastIndex = fileName.lastIndexOf(".");
            String id = fileName.substring(0, lastIndex);
            AttachEntity attachEntity = get(id);

            Path file = Paths.get("attaches/" + attachEntity.getPath() + "/" + fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public AttachDTO getAttachLink(String attachId) {
        AttachDTO dto = new AttachDTO();
        dto.setId(attachId);
        dto.setUrl(serverHost + "/api/v1/attach/open/" + attachId);
        return dto;
    }

}
