package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.classifies.dto.CreateSectDTO;
import truyenconvert.server.modules.classifies.dto.EditSectDTO;
import truyenconvert.server.modules.classifies.vm.SectVm;

import java.util.List;

@Service
public class SectServiceImpl implements SectService{
    @Override
    public ResponseSuccess<SectVm> createSect(CreateSectDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<List<SectVm>> getAllSect() {
        return null;
    }

    @Override
    public ResponseSuccess<SectVm> editSect(EditSectDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteSect(int id) {
        return null;
    }
}
