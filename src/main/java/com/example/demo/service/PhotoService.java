package com.example.demo.service;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class PhotoService {
    private static final String ROOT_NAME = "photos";
    private static final String PICTURE_EXTENSION = ".jpg";

    private final Path pictureRoot = Paths.get(ROOT_NAME);

    @PostConstruct
    private void createDirectories() {
        try {
            Files.createDirectories(pictureRoot);
        } catch (IOException e) {
            throw new RuntimeException("Could not create or find root folder");
        }
    }

    public void storePhoto(MultipartFile photo, String name) {
        try {
            Files.copy(photo.getInputStream(), pictureRoot.resolve(name + PICTURE_EXTENSION), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Could not store photo: " + name, e);
        }
    }

    public List<String> getAllPhotos() {
        try {
            return Files.walk(pictureRoot, 1)
                    .filter(path -> !path.equals(pictureRoot))
                    .map(path -> path.getFileName().toString())
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("Could not load photos", e);
        }
    }

}
