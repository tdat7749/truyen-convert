package truyenconvert.server.modules.read_histories.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.read_histories.vm.ReadHistoryVm;
import truyenconvert.server.modules.read_histories.dtos.SaveHistoryDTO;

import java.util.List;

@Service
public interface ReadHistoryService {
    ResponseSuccess<Boolean> saveHistory(SaveHistoryDTO dto, User user);
    ResponseSuccess<Boolean> deleteHistory(int id,User user);
    ResponseSuccess<List<ReadHistoryVm>> getAllReadHistory(int pageIndex); // sort by time update
}
