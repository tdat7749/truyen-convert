package truyenconvert.server.modules.read_histories.service;

import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.read_histories.vm.ReadHistoryVm;
import truyenconvert.server.modules.read_histories.dtos.SaveHistoryDTO;

import java.util.List;

public class ReadHistoryServiceImpl implements ReadHistoryService{

    @Override
    public ResponseSuccess<Boolean> saveHistory(SaveHistoryDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteHistory(int id, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<List<ReadHistoryVm>> getAllReadHistory(int pageIndex) {
        return null;
    }
}
