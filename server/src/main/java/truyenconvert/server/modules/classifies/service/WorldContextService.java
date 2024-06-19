package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.classifies.dto.CreateWorldContextDTO;
import truyenconvert.server.modules.classifies.dto.EditWorldContextDTO;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;

import java.util.List;

@Service
public interface WorldContextService {
    public ResponseSuccess<WorldContextVm> createWorldContext(CreateWorldContextDTO dto);
    public ResponseSuccess<List<WorldContextVm>> getAllWorldContext();
    public ResponseSuccess<WorldContextVm> editWorldContext(EditWorldContextDTO dto);
    public ResponseSuccess<Boolean> deleteWorldContext(int id);
}
