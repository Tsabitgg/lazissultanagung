package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.ResetPasswordRequest;
import com.lazis.lazissultanagung.dto.response.JwtResponse;
import com.lazis.lazissultanagung.dto.request.SignInRequest;
import com.lazis.lazissultanagung.dto.request.SignUpRequest;
import com.lazis.lazissultanagung.dto.response.ResponseMessage;
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

import java.util.Random;

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

    @Autowired
    private EmailSenderService emailSenderService;

    @Override
    public JwtResponse authenticateUser(SignInRequest signinRequest, HttpServletResponse response, String userType) throws BadRequestException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getEmailOrPhoneNumber(), signinRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (userType.equals("ADMIN")) {
            boolean isAdmin = userDetails.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN") ||
                            grantedAuthority.getAuthority().equals("SUB_ADMIN"));

            if (!isAdmin) {
                throw new BadRequestException("Akses ditolak!!!, anda tidak signin sebagai admin");
            }
        } else if (userType.equals("DONATUR")) {
            boolean isDonatur = userDetails.getAuthorities().isEmpty();

            if (!isDonatur) {
                throw new BadRequestException("Akses ditolak!!!, anda tidak signin sebagai donatur");
            }
        }

        String jwt = jwtUtils.generateJwtToken(authentication);
        return new JwtResponse(jwt);
    }


    @Override
    public Admin registerAdmin(SignUpRequest signUpRequest) throws BadRequestException {
        if (adminRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Error: Email sudah digunakan!");
        }

        if (adminRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new BadRequestException("Error: Nomor Handphone sudah digunakan!");
        }

        Admin admin = new Admin(
                signUpRequest.getUsername(),
                signUpRequest.getPhoneNumber(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAddress(),
                ERole.ADMIN);

        admin.setImage("https://res.cloudinary.com/donation-application/image/upload/v1711632747/default-avatar-icon-of-social-media-user-vector_thrtbz.jpg");

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
        adminRepository.save(admin);

        return admin;
    }

    @Override
    public Donatur registerDonatur(SignUpRequest signUpRequest) throws BadRequestException {
        if (donaturRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Error: Email Sudah digunakan!");
        }

        if (donaturRepository.existsByPhoneNumber(signUpRequest.getPhoneNumber())) {
            throw new BadRequestException("Error: Nomor Handphone sudah digunakan!");
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

    @Override
    public ResponseMessage resetPassword(ResetPasswordRequest resetPasswordRequest) {
        // Mencari donatur berdasarkan email
        Donatur existingDonatur = donaturRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(() -> new BadRequestException("Akun dengan email ini tidak ditemukan"));

        // Generate password random
        String randomPassword = generateRandomPassword(10);

        // Hash password random dan simpan ke database
        existingDonatur.setPassword(encoder.encode(randomPassword));
        donaturRepository.save(existingDonatur);

        // Kirim email dengan password random ke pengguna
        emailSenderService.sendEmailResetPassword(existingDonatur.getEmail(), "Reset Password",
                "Password baru Anda adalah: " + randomPassword);

        return new ResponseMessage(true, "Password berhasil direset. Silakan cek email Anda untuk password baru.");
    }

    // Fungsi untuk generate password acak
    private String generateRandomPassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            password.append(characters.charAt(random.nextInt(characters.length())));
        }

        return password.toString();
    }

}
