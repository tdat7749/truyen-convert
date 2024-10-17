package truyenconvert.server.modules.donation.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.donation.dtos.CreateDonationDTO;
import truyenconvert.server.modules.donation.vm.DonationVm;

import java.util.List;

@Service
public interface DonationService {
    ResponseSuccess<Boolean> donateToCreater(CreateDonationDTO dto, User user);
    ResponseSuccess<ResponsePaging<List<DonationVm>>> getAllDonationForBook(int bookId,int pageIndex, User user);

}
