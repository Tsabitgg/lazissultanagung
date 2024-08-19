package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.response.JwtResponse;
import com.lazis.lazissultanagung.dto.request.SignInRequest;
import com.lazis.lazissultanagung.dto.request.SignUpRequest;
import com.lazis.lazissultanagung.enumeration.ERole;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Donatur;
import com.lazis.lazissultanagung.repository.AdminRepository;
import com.lazis.lazissultanagung.repository.DonaturRepository;
import com.lazis.lazissultanagung.security.jwt.JwtUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DonaturRepository donaturRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public JwtResponse authenticateUser(SignInRequest signInRequest, HttpServletResponse response) throws ResponseStatusException {
        // Autentikasi pengguna menggunakan username atau nomor telepon
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequest.getEmailOrPhoneNumber(), signInRequest.getPassword()));

        // Set autentikasi ke dalam konteks keamanan
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Mendapatkan informasi pengguna yang terotentikasi
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Generate token JWT
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Ambil daftar peran pengguna
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());

        // Buat dan kembalikan respons JWT
        return new JwtResponse(jwt);
    }

    @Override
    public Admin registerAdmin(SignUpRequest signUpRequest) throws BadRequestException {
        if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already taken!");
        }

        if (adminRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new BadRequestException("Error: Phone Number is already in use!");
        }

        Admin admin = new Admin(
                signUpRequest.getUsername(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAddress(),
                ERole.ADMIN);

        admin.setImage("https://res.cloudinary.com/donation-application/image/upload/v1711632747/default-avatar-icon-of-social-media-user-vector_thrtbz.jpg");

        // Periksa role yang diinginkan
        if (signUpRequest.getRole() != null) {
            if (signUpRequest.getRole().equalsIgnoreCase("admin")) {
                admin.setRole(ERole.ADMIN);
            } else if (signUpRequest.getRole().equalsIgnoreCase("sub admin")) {
                admin.setRole(ERole.SUB_ADMIN);
            }
        }

        // Generate nomor VA unik untuk donatur
        Random random = new Random();
        long min = 1000000000L;
        long max = 9999999999L;

        for (int i = 0; i < 10; i++) {
            long vaNumber = min + (long) (random.nextDouble() * (max - min));
            admin.setVaNumber(vaNumber);
        }

        // Simpan admin baru ke database
        adminRepository.save(admin);

        return admin;
    }

    @Override
    public Donatur registerDonatur(SignUpRequest signUpRequest) throws BadRequestException {
        if (donaturRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Error: Email is already taken!");
        }

        if (donaturRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new BadRequestException("Error: Phone Number is already in use!");
        }

        // Set role default sebagai DONATUR
        Donatur donatur = new Donatur(
                signUpRequest.getUsername(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),  // Encode password
                signUpRequest.getAddress()
        );

        donatur.setImage("https://res.cloudinary.com/donation-application/image/upload/v1711632747/default-avatar-icon-of-social-media-user-vector_thrtbz.jpg");

        // Generate nomor VA unik
        Random random = new Random();
        long min = 1000000000L;
        long max = 9999999999L;

        for (int i = 0; i < 10; i++) {
            long vaNumber = min + (long) (random.nextDouble() * (max - min));
            donatur.setVaNumber(vaNumber);
        }

        // Simpan donatur baru ke database
        donaturRepository.save(donatur);

        return donatur;
    }

}
