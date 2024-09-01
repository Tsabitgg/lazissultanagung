package com.lazis.lazissultanagung.service;

import com.lazis.lazissultanagung.dto.request.CampaignRequest;
import com.lazis.lazissultanagung.dto.response.CampaignResponse;
import com.lazis.lazissultanagung.dto.response.ResponseMessage;
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
import java.util.Optional;
import java.util.stream.Collectors;

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
                .orElseThrow(() -> new BadRequestException("Kategori Tidak ditemukan"));

        // Get current authenticated admin
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin tidak ditemukan"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN) && !existingAdmin.getRole().equals(ERole.SUB_ADMIN)) {
                throw new BadRequestException("Hanya Admin dan Sub Admin yang bisa membuat campaign");
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
            campaignResponse.setCampaignCategory(campaign.getCampaignCategory().getCampaignCategory());
            campaignResponse.setCreator(existingAdmin.getUsername());

            return campaignResponse;
        }
        throw new BadRequestException("Admin tidak ditemukan");
    }

    @Override
    public List<CampaignResponse> getAllCampaign() {
        List<Campaign> campaigns = campaignRepository.findAll();
        return campaigns.stream()
                .map(campaign -> {
                    CampaignResponse response = modelMapper.map(campaign, CampaignResponse.class);
                    response.setCampaignCategory(campaign.getCampaignCategory().getCampaignCategory());
                    response.setCreator(campaign.getAdmin().getUsername()); // Set creator's username
                    // Tambahan mapping yang diperlukan
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CampaignResponse> getCampaignById(Long id) {
        return campaignRepository.findById(id)
                .map(campaign -> {
                    CampaignResponse response = modelMapper.map(campaign, CampaignResponse.class);
                    response.setCreator(campaign.getAdmin().getUsername());
                    return response;
                });
    }


    @Override
    public ResponseMessage deleteCampaign(Long id){
        Campaign deleteCampaign = campaignRepository.findById(id)
                .orElseThrow(()-> new BadRequestException("Campaign tidak ditemukan"));
        campaignRepository.delete(deleteCampaign);
        return new ResponseMessage(true, "Campaign Berhasil Dihapus");
    }

    @Override
    @Transactional
    public Campaign approveCampaign(Long id) throws BadRequestException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
                    .orElseThrow(() -> new BadRequestException("Admin tidak ditemukan"));

            if (!existingAdmin.getRole().equals(ERole.ADMIN)) {
                throw new BadRequestException("Hanya Admin yang bisa menyetujui campaign");
            }

            Campaign campaign = campaignRepository.findById(id)
                            .orElseThrow(()-> new BadRequestException("Campaign tidak ditemukan"));

            campaign.setApproved(true);

            return campaignRepository.save(campaign);
        }
        throw new BadRequestException("Admin tidak ditemukan");
    }

    @Override
    public Page<CampaignResponse> getCampaignByActiveAndApproved(Pageable pageable) {
        Page<Campaign> campaigns = campaignRepository.findCampaignByActiveAndApproved(pageable);

        // Update setiap Campaign jika `currentAmount` >= `targetAmount`
        campaigns.forEach(campaign -> {
            if (campaign.getCurrentAmount() >= campaign.getTargetAmount()) {
                campaign.setActive(false);
                campaignRepository.save(campaign);
            }
        });

        // Menerapkan mapping dari Campaign ke CampaignResponse
        return campaigns.map(campaign -> {
            CampaignResponse response = modelMapper.map(campaign, CampaignResponse.class);
            response.setCampaignCategory(campaign.getCampaignCategory().getCampaignCategory());
            response.setCreator(campaign.getAdmin().getUsername());
            return response;
        });
    }

}
