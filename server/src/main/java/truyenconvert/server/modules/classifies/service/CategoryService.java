package truyenconvert.server.modules.classifies.service;

import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.modules.classifies.dto.CreateCategoryDTO;
import truyenconvert.server.modules.classifies.dto.EditCategoryDTO;
import truyenconvert.server.modules.classifies.vm.CategoryVm;

import java.util.List;

public interface CategoryService {
    public ResponseSuccess<CategoryVm> createCategory(CreateCategoryDTO dto);
    public ResponseSuccess<List<CategoryVm>> getAllCategory();
    public ResponseSuccess<CategoryVm> editCategory(EditCategoryDTO dto);
    public ResponseSuccess<Boolean> deleteCategory(int id);
}
