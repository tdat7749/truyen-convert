package truyenconvert.server.modules.classifies.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Category;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.classifies.dto.CreateCategoryDTO;
import truyenconvert.server.modules.classifies.dto.EditCategoryDTO;
import truyenconvert.server.modules.classifies.exceptions.WorldContextNotFoundException;
import truyenconvert.server.modules.classifies.repository.CategoryRepository;
import truyenconvert.server.modules.classifies.vm.CategoryVm;
import truyenconvert.server.modules.common.service.MappingService;
import truyenconvert.server.modules.common.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService{

    private final String CACHE_VALUE = "categories";

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final MappingService mappingService;
    private final MessageService messageService;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            MappingService mappingService,
            MessageService messageService
    ){
        this.categoryRepository = categoryRepository;
        this.mappingService = mappingService;
        this.messageService = messageService;
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<CategoryVm> createCategory(CreateCategoryDTO dto, User user) {
        Category category = Category.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        var result = mappingService.getCategoryVm(category);

        LOGGER.info(messageService.getMessage("category.log.created.success"),user.getId(),result.getId());
        return new ResponseSuccess<>(messageService.getMessage("category.created.success"),result);
    }

    @Override
    @Cacheable(value = CACHE_VALUE)
    public ResponseSuccess<List<CategoryVm>> getAllCategory() {
        var rawResult = categoryRepository.findAll(Sort.by(Sort.Direction.ASC,"id"));

        var result = rawResult.stream().map(mappingService::getCategoryVm).toList();

        return new ResponseSuccess<>("Thành công.",result);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<CategoryVm> editCategory(EditCategoryDTO dto, int id, User user) {
        var categoryFound = categoryRepository.findById(id).orElse(null);
        if(categoryFound == null){

            LOGGER.error(messageService.getMessage("category.log.not-found"));
            throw new WorldContextNotFoundException(messageService.getMessage("category.not-found"));
        }

        categoryFound.setDescription(dto.getDescription());
        categoryFound.setTitle(dto.getTitle());
        categoryFound.setUpdatedAt(LocalDateTime.now());

        var save = categoryRepository.save(categoryFound);

        var result = mappingService.getCategoryVm(save);

        LOGGER.info(messageService.getMessage("category.log.edit.success"),user.getId(),result.getId());
        return new ResponseSuccess<>(messageService.getMessage("category.edit.success"),result);
    }

    @Override
    @CacheEvict(value = CACHE_VALUE, allEntries = true)
    public ResponseSuccess<Boolean> deleteCategory(int id, User user) {
        return null;
    }

    @Override
    public Optional<Category> findById(int id) {
        return Optional.empty();
    }

    @Override
    @Cacheable(value = CACHE_VALUE, key = "#id")
    public ResponseSuccess<CategoryVm> getById(int id) {
        var categoryFound = categoryRepository.findById(id).orElse(null);
        if(categoryFound == null){

            LOGGER.error(messageService.getMessage("category.log.not-found"));
            throw new WorldContextNotFoundException(messageService.getMessage("category.not-found"));
        }

        var result = mappingService.getCategoryVm(categoryFound);

        return new ResponseSuccess<>("Thành công.",result);
    }
}
