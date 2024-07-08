package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.WorldContext;
import truyenconvert.server.modules.classifies.dto.CreateWorldContextDTO;
import truyenconvert.server.modules.classifies.dto.EditWorldContextDTO;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;

import java.util.List;
import java.util.Optional;

@Service
public interface WorldContextService {
    ResponseSuccess<WorldContextVm> createWorldContext(CreateWorldContextDTO dto);
    ResponseSuccess<List<WorldContextVm>> getAllWorldContext();
    ResponseSuccess<WorldContextVm> editWorldContext(EditWorldContextDTO dto);
    ResponseSuccess<Boolean> deleteWorldContext(int id);
    Optional<WorldContext> findById(int id);
}
