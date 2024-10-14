package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.models.WorldContext;
import truyenconvert.server.modules.classifies.dto.CreateWorldContextDTO;
import truyenconvert.server.modules.classifies.dto.EditWorldContextDTO;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;

import java.util.List;
import java.util.Optional;

@Service
public interface WorldContextService {
    ResponseSuccess<WorldContextVm> createWorldContext(CreateWorldContextDTO dto, User user);
    ResponseSuccess<List<WorldContextVm>> getAllWorldContext();
    ResponseSuccess<WorldContextVm> editWorldContext(EditWorldContextDTO dto, int id, User user);
    ResponseSuccess<Boolean> deleteWorldContext(int id, User user);
    Optional<WorldContext> findById(int id);

    ResponseSuccess<WorldContextVm> getById(int id);
}
