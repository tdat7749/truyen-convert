package truyenconvert.server.modules.book.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponseSuccess;
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
}
