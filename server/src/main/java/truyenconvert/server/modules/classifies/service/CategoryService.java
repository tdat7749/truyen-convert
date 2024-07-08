package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Category;
import truyenconvert.server.modules.classifies.dto.CreateCategoryDTO;
import truyenconvert.server.modules.classifies.dto.EditCategoryDTO;
import truyenconvert.server.modules.classifies.vm.CategoryVm;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    ResponseSuccess<CategoryVm> createCategory(CreateCategoryDTO dto);
    ResponseSuccess<List<CategoryVm>> getAllCategory();
    ResponseSuccess<CategoryVm> editCategory(EditCategoryDTO dto);
    ResponseSuccess<Boolean> deleteCategory(int id);
    Optional<Category> findById(int id);
}
