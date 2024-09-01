package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.enumeration.ERole;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.DSKL;
import com.lazis.lazissultanagung.repository.AdminRepository;
import com.lazis.lazissultanagung.repository.DSKLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DSKLServiceImpl implements DSKLService{

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private DSKLRepository dsklRepository;

    @Override
    public List<DSKL> getAllDSKL(){
        return dsklRepository.findAll();
    }

    @Override
    public DSKL createDSKL(DSKL dskl){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin not found"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN) && !existingAdmin.getRole().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            return dsklRepository.save(dskl);
        }
        throw new BadRequestException("Admin not found");
    }


    @Override
    public DSKL updateDSKL(Long id, DSKL dskl){
        DSKL updateDSKL = dsklRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("DSKL Not Found"));

        updateDSKL.setDsklCategory(dskl.getDsklCategory());
        updateDSKL.setAmount(dskl.getAmount());
        updateDSKL.setDistribution(dskl.getDistribution());
        updateDSKL.setCoa(dskl.getCoa());
        updateDSKL.setEmergency(dskl.isEmergency());

        return dsklRepository.save(updateDSKL);
    }

    @Override
    public void deleteDSKL(Long id){
        DSKL deleteDSKL = dsklRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("DSKL Not Found"));

        dsklRepository.delete(deleteDSKL);
    }
}
