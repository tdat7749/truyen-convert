package truyenconvert.server.modules.classifies.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.classifies.dto.CreateCategoryDTO;
import truyenconvert.server.modules.classifies.dto.EditCategoryDTO;
import truyenconvert.server.modules.classifies.service.CategoryService;
import truyenconvert.server.modules.classifies.vm.CategoryVm;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(
            CategoryService categoryService
    ){
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<List<CategoryVm>>> getAllCategory(){
        var result = categoryService.getAllCategory();

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<CategoryVm>> getById(
            @PathVariable("id") int id
    ){
        var result = categoryService.getById(id);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<CategoryVm>> editCategory(
            @AuthenticationPrincipal User user,
            @PathVariable("id") int id,
            @RequestBody EditCategoryDTO dto
    ){
        var result = categoryService.editCategory(dto, id, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<CategoryVm>> createCategory(
            @AuthenticationPrincipal User user,
            @RequestBody CreateCategoryDTO dto
    ){
        var result = categoryService.createCategory(dto, user);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
