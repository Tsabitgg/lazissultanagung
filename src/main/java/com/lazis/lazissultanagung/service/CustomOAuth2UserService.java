package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Donatur;
import com.lazis.lazissultanagung.repository.DonaturRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private DonaturRepository donaturRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Extracting user information from Google
        Map<String, Object> attributes = oauth2User.getAttributes();
        String email = (String) attributes.get("email");
        String username = (String) attributes.get("name");
        String image = (String) attributes.get("picture");

        // Simpan data pengguna ke database jika perlu
        Donatur donatur = donaturRepository.findByEmail(email)
                .orElseGet(() -> {
                    Donatur newDonatur = new Donatur();
                    newDonatur.setUsername(username);
                    newDonatur.setEmail(email);
                    newDonatur.setImage(image);
                    newDonatur.setCreatedAt(new Date());
                    return donaturRepository.save(newDonatur);
                });

        return oauth2User;
    }
}
