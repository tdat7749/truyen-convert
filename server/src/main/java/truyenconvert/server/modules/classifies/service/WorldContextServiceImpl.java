package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.classifies.dto.CreateWorldContextDTO;
import truyenconvert.server.modules.classifies.dto.EditWorldContextDTO;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;

import java.util.List;

@Service
public class WorldContextServiceImpl implements WorldContextService{
    @Override
    public ResponseSuccess<WorldContextVm> createWorldContext(CreateWorldContextDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<List<WorldContextVm>> getAllWorldContext() {
        return null;
    }

    @Override
    public ResponseSuccess<WorldContextVm> editWorldContext(EditWorldContextDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteWorldContext(int id) {
        return null;
    }
}
