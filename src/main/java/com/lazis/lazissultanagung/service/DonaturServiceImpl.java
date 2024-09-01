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

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private CloudinaryService cloudinaryService;


    @Override
    public Donatur getCurrentDonatur() throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return donaturRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Donatur tidak ditemukan"));
        }
        throw new BadRequestException("Donatur tidak ditemukan");
    }

    @Override
    public Donatur editProfileDonatur(EditProfileDonaturRequest editProfileRequest) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Donatur existingDonatur = donaturRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Donatur"));

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
                String imageUrl = cloudinaryService.upload(editProfileRequest.getImage());
                if (imageUrl != null) {
                    existingDonatur.setImage(imageUrl);
                }
            }

            return donaturRepository.save(existingDonatur);
        }
        throw new BadRequestException("Donatur tidak ditemukan");
    }

}
