package truyenconvert.server.modules.donation.service;

import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.donation.dtos.CreateDonationDTO;

public class DonationServiceImpl implements DonationService{
    @Override
    public ResponseSuccess<Boolean> donateToCreater(CreateDonationDTO dto, User user) {
        return null;
    }
}
