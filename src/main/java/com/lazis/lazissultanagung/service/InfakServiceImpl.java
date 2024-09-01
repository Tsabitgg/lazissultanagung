package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.enumeration.ERole;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Infak;
import com.lazis.lazissultanagung.repository.AdminRepository;
import com.lazis.lazissultanagung.repository.InfakRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfakServiceImpl implements InfakService{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private InfakRepository infakRepository;

    @Override
    public List<Infak> getAllInfak(){
        return infakRepository.findAll();
    }

    @Override
    public Infak createInfak(Infak infak){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin not found"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN) && !existingAdmin.getRole().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            return infakRepository.save(infak);
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    public Infak updateInfak(Long id, Infak infak){
        Infak updateInfak = infakRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("Infak Not Found"));

        updateInfak.setInfakCategory(infak.getInfakCategory());
        updateInfak.setAmount(infak.getAmount());
        updateInfak.setDistribution(infak.getDistribution());
        updateInfak.setCoa(infak.getCoa());
        updateInfak.setEmergency(infak.isEmergency());

        return infakRepository.save(updateInfak);
    }
}
