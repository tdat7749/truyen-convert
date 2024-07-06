package truyenconvert.server.modules.book.controller;

import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.EditAuthorDTO;
import truyenconvert.server.modules.book.service.AuthorService;
import truyenconvert.server.modules.book.vm.AuthorVm;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(
            AuthorService authorService
    ) {
        this.authorService = authorService;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<AuthorVm>>>> getAllAuthor(
        @RequestParam(value = "page_index",defaultValue = "0") int pageIndex,
        @RequestParam(value = "sort",defaultValue = "createdAt") String sort,
        @RequestParam(value = "keyword",defaultValue = "") String keyword
    ){
        var result = authorService.getAllAuthor(pageIndex,sort,keyword);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<AuthorVm>> getAuthorById(
            @PathVariable("id") int id
    ){
        var result = authorService.getAuthorById(id);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> editAuthor(
            @RequestBody @Valid EditAuthorDTO dto,
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user
    ){
        var result = authorService.editAuthor(dto,id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }


}
