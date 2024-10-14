package truyenconvert.server.modules.classifies.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Category;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.classifies.dto.CreateCategoryDTO;
import truyenconvert.server.modules.classifies.dto.EditCategoryDTO;
import truyenconvert.server.modules.classifies.vm.CategoryVm;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {
    ResponseSuccess<CategoryVm> createCategory(CreateCategoryDTO dto, User user);
    ResponseSuccess<List<CategoryVm>> getAllCategory();
    ResponseSuccess<CategoryVm> editCategory(EditCategoryDTO dto, int id, User user);
    ResponseSuccess<Boolean> deleteCategory(int id, User user);
    Optional<Category> findById(int id);

    ResponseSuccess<CategoryVm> getById(int id);
}
