package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.enumeration.ERole;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Zakat;
import com.lazis.lazissultanagung.repository.AdminRepository;
import com.lazis.lazissultanagung.repository.ZakatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZakatServiceImpl implements ZakatService {

    @Autowired
    private ZakatRepository zakatRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Zakat> getAllZakat(){
        return zakatRepository.findAll();
    }

    @Override
    public Zakat crateZakat(Zakat zakat) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin not found"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN) && !existingAdmin.getRole().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            return zakatRepository.save(zakat);
        }
        throw new BadRequestException("Admin not found");
    }


}
