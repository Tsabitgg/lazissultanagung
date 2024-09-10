//package com.lazis.lazissultanagung.service;
//
//import com.cloudinary.Cloudinary;
//import com.lazis.lazissultanagung.dto.request.DistributionRequest;
//import com.lazis.lazissultanagung.dto.response.CampaignResponse;
//import com.lazis.lazissultanagung.enumeration.ERole;
//import com.lazis.lazissultanagung.exception.BadRequestException;
//import com.lazis.lazissultanagung.model.*;
//import com.lazis.lazissultanagung.repository.*;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class DistributionServiceImpl implements DistributionService{
//
//    @Autowired
//    private DistributionRepository distributionRepository;
//
//    @Autowired
//    private CampaignRepository campaignRepository;
//
//    @Autowired
//    private ZakatRepository zakatRepository;
//
//    @Autowired
//    private InfakRepository infakRepository;
//
//    @Autowired
//    private DSKLRepository dsklRepository;
//
//    @Autowired
//    private WakafRepository wakafRepository;
//
//    @Autowired
//    private Cloudinary cloudinary;
//
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @Autowired
//    private AdminRepository adminRepository;
//
//    @Autowired
//    private CloudinaryService cloudinaryService;
//
//
//    @Override
//    public Page<Distribution> getAllTransaction(Integer month, Integer year, Pageable pageable) {
//        return distributionRepository.findAllByMonthAndYear(month, year, pageable)
//                .map(distribution -> {
//                    Object categoryData = getCategoryData(distribution);
//                    return new Distribution();
//                });
//    }
//
//    @Override
//    public Distribution createDistribution(String categoryType, Long id, DistributionRequest distributionRequest) throws BadRequestException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//            Admin existingAdmin = adminRepository.findByPhoneNumber(userDetails.getUsername())
//                    .orElseThrow(() -> new BadRequestException("Admin not found"));
//
//            if (!existingAdmin.getRole().equals(ERole.ADMIN) && !existingAdmin.getRole().equals(ERole.SUB_ADMIN)) {
//                throw new BadRequestException("Only ADMIN and SUB ADMIN can Distribute");
//            }
//
//            Distribution distribution = modelMapper.map(distributionRequest, Distribution.class);
//
//            Object responseDto = null;
//            switch (categoryType) {
//                case "campaign":
//                    Campaign campaign = campaignRepository.findById(id)
//                            .orElseThrow(() -> new BadRequestException("Campaign tidak ditemukan"));
//                    distribution.setCampaign(campaign);
//                    responseDto = modelMapper.map(campaign, CampaignResponse.class);
//                    break;
//                case "zakat":
//                    Zakat zakat = zakatRepository.findById(id)
//                            .orElseThrow(() -> new BadRequestException("Zakat tidak ditemukan"));
//                    distribution.setZakat(zakat);
//                    responseDto = modelMapper.map(zakat, Zakat.class);
//                    break;
//                case "infak":
//                    Infak infak = infakRepository.findById(id)
//                            .orElseThrow(() -> new BadRequestException("Infak tidak ditemukan"));
//                    distribution.setInfak(infak);
//                    responseDto = modelMapper.map(infak, Infak.class);
//                    break;
//                case "dskl":
//                    DSKL dskl = dsklRepository.findById(id)
//                            .orElseThrow(() -> new BadRequestException("DSKL tidak ditemukan"));
//                    distribution.setDskl(dskl);
//                    responseDto = modelMapper.map(dskl, DSKL.class);
//                    break;
//                case "wakaf":
//                    Wakaf wakaf = wakafRepository.findById(id)
//                            .orElseThrow(() -> new BadRequestException("Wakaf tidak ditemukan"));
//                    distribution.setWakaf(wakaf);
//                    responseDto = modelMapper.map(wakaf, Wakaf.class);
//                    break;
//                default:
//                    throw new IllegalArgumentException("Invalid category type: " + categoryType);
//            }
//
//            String imageUrl = null;
//            if (distributionRequest.getImage() != null && !distributionRequest.getImage().isEmpty()) {
//                imageUrl = cloudinaryService.upload(distributionRequest.getImage());
//            }
//            distribution.setImage(imageUrl);
//            distribution.setCategory(categoryType);
//
//            distribution = distributionRepository.save(distribution);
//
//            switch (categoryType) {
//                case "campaign":
//                    campaignRepository.updateCampaignCurrentAmount(id, distributionRequest.getDistributionAmount());
//                    break;
//                case "zakat":
//                    zakatRepository.updateZakatCurrentAmount(id, distributionRequest.getDistributionAmount());
//                    break;
//                case "infak":
//                    infakRepository.updateInfakCurrentAmount(id, distributionRequest.getDistributionAmount());
//                    break;
//                case "dskl":
//                    dsklRepository.updateDSKLCurrentAmount(id, distributionRequest.getDistributionAmount());
//                    break;
//                case "wakaf":
//                    wakafRepository.updateWakafCurrentAmount(id, distributionRequest.getDistributionAmount());
//                    break;
//
//            }
//            throw new BadRequestException("Admin not found");
//        }
//    }
//
//    private Object getCategoryData(Distribution distribution) {
//        // Mengambil data berdasarkan kategori
//        switch (distribution.getCategory()) {
//            case "zakat":
//                return distribution.getZakat();
//            case "infak":
//                return distribution.getInfak();
//            case "wakaf":
//                return distribution.getWakaf();
//            case "dskl":
//                return distribution.getDskl();
//            case "campaign":
//                return distribution.getCampaign();
//            default:
//                return null;
//        }
//    }
//}
