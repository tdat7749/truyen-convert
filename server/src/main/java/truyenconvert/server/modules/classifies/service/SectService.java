package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Sect;
import truyenconvert.server.modules.classifies.dto.CreateSectDTO;
import truyenconvert.server.modules.classifies.dto.EditSectDTO;
import truyenconvert.server.modules.classifies.vm.SectVm;

import java.util.List;
import java.util.Optional;

@Service
public interface SectService {
    ResponseSuccess<SectVm> createSect(CreateSectDTO dto);
    ResponseSuccess<List<SectVm>> getAllSect();
    ResponseSuccess<SectVm> editSect(EditSectDTO dto);
    ResponseSuccess<Boolean> deleteSect(int id);
    Optional<Sect> findById(int id);
}
