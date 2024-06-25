package truyenconvert.server.modules.evaluation.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.evaluation.dtos.CreateEvaluationDTO;
import truyenconvert.server.modules.evaluation.vm.EvaluationVm;

import java.util.List;

@Service
public interface EvaluationService {
    ResponseSuccess<EvaluationVm> createEvaluation(CreateEvaluationDTO dto, User user);
    ResponseSuccess<List<EvaluationVm>> getAllEvaluations(int pageIndex, String slug);
}
