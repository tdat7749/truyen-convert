package truyenconvert.server.modules.donation.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.donation.dtos.CreateDonationDTO;

@Service
public interface DonationService {
    ResponseSuccess<Boolean> donateToCreater(CreateDonationDTO dto, User user);
}
