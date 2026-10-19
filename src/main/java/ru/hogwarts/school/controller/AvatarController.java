package ru.hogwarts.school.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;

@RestController
@RequestMapping("avatar")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    //Загрузка картинки
    @PostMapping(value = "{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long studentId,
                                               @RequestParam MultipartFile file) throws IOException {
        if (file.getSize() >= 1024 * 300) { // Ограничение на 300 Кб (опционально)
            return ResponseEntity.badRequest().body("Файл слишком большой!");
        }
        avatarService.uploadAvatar(studentId, file);
        return ResponseEntity.ok().build();
    }

    //Получение картинки из БД
    @GetMapping("{studentId}/from-db")
    public ResponseEntity<byte[]> downloadFromDb(@PathVariable Long studentId) {
        Avatar avatar = avatarService.findAvatar(studentId);
        if (avatar == null || avatar.getData() == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .contentLength(avatar.getFileSize())
                .body(avatar.getData());
    }


    //Получение картинки из директории
    @GetMapping("{studentId}/from-disk")
    public void downloadFromDisk(@PathVariable Long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.findAvatar(studentId);
        if (avatar == null || avatar.getFilePath() == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path path = Path.of(avatar.getFilePath()).toAbsolutePath();

        if (!Files.exists(path)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setContentType(avatar.getMediaType());
        response.setContentLength((int) avatar.getFileSize());

        try (InputStream is = Files.newInputStream(path);
             OutputStream os = response.getOutputStream()) {
            is.transferTo(os);
        }
    }
    @GetMapping("/page")
    public ResponseEntity<Collection<Avatar>> getAllAvatars(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {

        Page<Avatar> avatarPage = avatarService.getAllAvatars(page, size);

        return ResponseEntity.ok(avatarPage.getContent());
    }
}