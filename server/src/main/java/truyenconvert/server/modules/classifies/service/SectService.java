package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.classifies.dto.CreateSectDTO;
import truyenconvert.server.modules.classifies.dto.EditSectDTO;
import truyenconvert.server.modules.classifies.vm.SectVm;

import java.util.List;

@Service
public interface SectService {
    public ResponseSuccess<SectVm> createSect(CreateSectDTO dto);
    public ResponseSuccess<List<SectVm>> getAllSect();
    public ResponseSuccess<SectVm> editSect(EditSectDTO dto);
    public ResponseSuccess<Boolean> deleteSect(int id);
}
