package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.CampaignRequest;
import com.lazis.lazissultanagung.dto.response.CampaignResponse;
import com.lazis.lazissultanagung.enumeration.ERole;
import com.lazis.lazissultanagung.exception.BadRequestException;
import com.lazis.lazissultanagung.model.Admin;
import com.lazis.lazissultanagung.model.Campaign;
import com.lazis.lazissultanagung.model.CampaignCategory;
import com.lazis.lazissultanagung.repository.AdminRepository;
import com.lazis.lazissultanagung.repository.CampaignCategoryRepository;
import com.lazis.lazissultanagung.repository.CampaignRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignCategoryRepository campaignCategoryRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CampaignResponse createCampaign(CampaignRequest campaignRequest) {
        // Find category
        CampaignCategory campaignCategory = campaignCategoryRepository.findById(campaignRequest.getCategoryId())
                .orElseThrow(() -> new BadRequestException("Category not found"));

        // Get current authenticated admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin not found"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN) && !existingAdmin.getRole().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Only ADMIN users can create campaigns");
            }

            // Handle image upload to Cloudinary
            String imageUrl = null;
            if (campaignRequest.getCampaignImage() != null && !campaignRequest.getCampaignImage().isEmpty()) {
                imageUrl = cloudinaryService.upload(campaignRequest.getCampaignImage());
            }

            // Create new campaign
            Campaign campaign = new Campaign();
            campaign.setCampaignCategory(campaignCategory);
            campaign.setCampaignName(campaignRequest.getCampaignName());
            campaign.setCampaignCode(campaignRequest.getCampaignCode());
            campaign.setCampaignImage(imageUrl);
            campaign.setDescription(campaignRequest.getDescription());
            campaign.setLocation(campaignRequest.getLocation());
            campaign.setTargetAmount(campaignRequest.getTargetAmount());
            campaign.setCurrentAmount(campaignRequest.getCurrentAmount());
            campaign.setStartDate(campaignRequest.getStartDate());
            campaign.setEndDate(campaignRequest.getEndDate());
            campaign.setActive(campaignRequest.isActive());
            campaign.setAdmin(existingAdmin);
            campaign.setDistribution(0.0);
            if (existingAdmin.getRole().equals(ERole.ADMIN)){
                campaign.setApproved(true);
            } else if (existingAdmin.getRole().equals(ERole.SUB_ADMIN)){
                campaign.setApproved(false);
            }
            campaign.setEmergency(campaignRequest.isEmergency()); // Set initial emergency status

            // Save campaign to database
            Campaign savedCampaign = campaignRepository.save(campaign);

            // Map Campaign to CampaignResponse using ModelMapper
            CampaignResponse campaignResponse = modelMapper.map(savedCampaign, CampaignResponse.class);

            // Additional manual mappings, if necessary
            campaignResponse.setCreator(existingAdmin.getUsername());
            campaignResponse.setCategoryName(savedCampaign.getCampaignCategory().getCampaignCategory());

            return campaignResponse;
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    public List<Campaign> getAllCampaign() {
        return campaignRepository.findAll();
    }

    @Override
    @Transactional
    public Campaign approveCampaign(Long id) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin not found"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN)) {
                throw new BadRequestException("Only ADMIN users can approve campaigns");
            }

            Campaign campaign = campaignRepository.findById(id)
                            .orElseThrow(()-> new BadRequestException("Campaign tidak ditemukan"));

            campaign.setApproved(true);

            return campaignRepository.save(campaign);
        }
        throw new BadRequestException("Admin not found");
    }

    @Override
    public Page<Campaign> getCampaignByActiveAndApproved(Pageable pageable) {
        Page<Campaign> campaigns = campaignRepository.findCampaignByActiveAndApproved(pageable);
        for (Campaign campaign : campaigns.getContent()) {
            if (campaign.getCurrentAmount() >= campaign.getTargetAmount()) {
                campaign.setActive(false);
                campaignRepository.save(campaign);
            }
        }
        return campaigns;
    }
}
