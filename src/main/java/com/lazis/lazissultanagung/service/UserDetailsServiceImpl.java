package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Donatur;
import com.lazis.lazissultanagung.repository.AdminRepository;
import com.lazis.lazissultanagung.repository.DonaturRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    DonaturRepository donaturRepository;

    @Autowired
    AdminRepository adminRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String input) throws UsernameNotFoundException {
        Optional<Donatur> donaturOptional = donaturRepository.findByPhoneNumber(input);
        Optional<Admin> adminOptional = adminRepository.findByPhoneNumber(input);

        Donatur donatur = donaturOptional.orElse(null);
        Admin admin = adminOptional.orElse(null);

        if (donatur == null && admin == null) {
            // Jika tidak ditemukan, coba cari dengan email
            donaturOptional = donaturRepository.findByEmail(input);
            adminOptional = adminRepository.findByEmail(input);

            donatur = donaturOptional.orElse(null);
            admin = adminOptional.orElse(null);
        }

        if (donatur != null) {
            return UserDetailsImpl.build(donatur);
        } else if (admin != null) {
            return UserDetailsImpl.build(admin);
        } else {
            throw new UsernameNotFoundException("User not found with input: " + input);
        }
    }
}
