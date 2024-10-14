package truyenconvert.server.modules.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateBookDTO;
import truyenconvert.server.modules.book.service.BookService;
import truyenconvert.server.modules.book.vm.BookVm;

@RestController
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    public BookController(
            BookService bookService
    ){
        this.bookService = bookService;
    }

    @GetMapping("/{slug}")
    @ResponseBody
    private ResponseEntity<ResponseSuccess<BookVm>> getBookBySlug(
            @PathVariable("slug") String slug
    ){
        var result = bookService.getBookBySlug(slug);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/")
    @ResponseBody
    private ResponseEntity<ResponseSuccess<BookVm>> createBook(
            @RequestBody CreateBookDTO dto,
            @AuthenticationPrincipal User user
    ){
        var result = bookService.createBook(dto,user);
        if(result.getData() == null){
            return new ResponseEntity<>(result,HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }
}
