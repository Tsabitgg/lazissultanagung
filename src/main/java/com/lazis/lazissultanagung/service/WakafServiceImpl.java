package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.enumeration.ERole;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Wakaf;
import com.lazis.lazissultanagung.repository.AdminRepository;
import com.lazis.lazissultanagung.repository.WakafRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WakafServiceImpl implements WakafService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private WakafRepository wakafRepository;

    @Override
    public List<Wakaf> getAllWakaf(){
        return wakafRepository.findAll();
    }

    @Override
    public Wakaf createWakaf(Wakaf wakaf){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin not found"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN) && !existingAdmin.getRole().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            return wakafRepository.save(wakaf);
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    public Wakaf updateWakaf(Long id, Wakaf wakaf){
        Wakaf updateWakaf = wakafRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("Wakaf Not Found"));

        updateWakaf.setWakafCategory(wakaf.getWakafCategory());
        updateWakaf.setAmount(wakaf.getAmount());
        updateWakaf.setDistribution(wakaf.getDistribution());
        updateWakaf.setCoa(wakaf.getCoa());
        updateWakaf.setEmergency(wakaf.isEmergency());

        return wakafRepository.save(updateWakaf);
    }
}
