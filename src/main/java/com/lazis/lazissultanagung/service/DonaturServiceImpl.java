package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.EditProfileDonaturRequest;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Donatur;
import com.lazis.lazissultanagung.repository.DonaturRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class DonaturServiceImpl implements DonaturService {

    @Autowired
    private DonaturRepository donaturRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private PasswordEncoder encoder;


    @Override
    public Donatur getCurrentDonatur() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return donaturRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("User not found"));
        }
        throw new BadRequestException("User not found");
    }

    @Override
    public Donatur editProfileDonatur(EditProfileDonaturRequest editProfileRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Donatur existingDonatur = donaturRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            if (editProfileRequest.getUsername() != null) {
                existingDonatur.setUsername(editProfileRequest.getUsername());
            }
            if (editProfileRequest.getPhoneNumber() != null) {
                existingDonatur.setPhoneNumber(editProfileRequest.getPhoneNumber());
            }
            if (editProfileRequest.getEmail() != null) {
                existingDonatur.setEmail(editProfileRequest.getEmail());
            }
            if (editProfileRequest.getPassword() != null) {
                existingDonatur.setPassword(encoder.encode(editProfileRequest.getPassword()));
            }
            if (editProfileRequest.getAddress() != null) {
                existingDonatur.setAddress(editProfileRequest.getAddress());
            }

            if (editProfileRequest.getImage() != null && !editProfileRequest.getImage().isEmpty()) {
                try {
                    String fileName = userDetails.getUsername() + "_" + System.currentTimeMillis() + "_" + editProfileRequest.getImage().getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, fileName);

                    // Buat direktori jika belum ada
                    Files.createDirectories(filePath.getParent());

                    // Simpan file gambar ke sistem file lokal
                    Files.write(filePath, editProfileRequest.getImage().getBytes());

                    // Simpan path file ke dalam database
                    existingDonatur.setImage(filePath.toString());
                } catch (IOException e) {
                    throw new BadRequestException("Error uploading image" + e);
                }
            }
            return donaturRepository.save(existingDonatur);
        }
        throw new BadRequestException("User not found");
    }
}
