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
public class CategoryServiceImpl implements CategoryService{
    @Override
    public ResponseSuccess<CategoryVm> createCategory(CreateCategoryDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<List<CategoryVm>> getAllCategory() {
        return null;
    }

    @Override
    public ResponseSuccess<CategoryVm> editCategory(EditCategoryDTO dto) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> deleteCategory(int id) {
        return null;
    }

    @Override
    public Optional<Category> findById(int id) {
        return Optional.empty();
    }
}
