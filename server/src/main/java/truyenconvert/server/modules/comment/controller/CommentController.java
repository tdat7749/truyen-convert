package truyenconvert.server.modules.comment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.comment.dtos.CreateCommentDTO;
import truyenconvert.server.modules.comment.dtos.EditCommentDTO;
import truyenconvert.server.modules.comment.service.CommentService;
import truyenconvert.server.modules.comment.vm.CommentVm;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(
        CommentService commentService
    ){
        this.commentService = commentService;
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<CommentVm>>>> getAllComments(
            @PathVariable("slug") String slug,
            @RequestParam(value = "page_index", defaultValue = "0") int pageIndex,
            @AuthenticationPrincipal User user
    ){
        var result = commentService.getAllComments(pageIndex,slug,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/reply/{id}")
    public ResponseEntity<ResponseSuccess<List<CommentVm>>> getAllReplyComments(
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user
    ){
        var result = commentService.getAllReplyComment(id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/like/{id}")
    public ResponseEntity<ResponseSuccess<Boolean>> likeComment(
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user
    ){
        var result = commentService.likeComment(id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/unlike/{id}")
    public ResponseEntity<ResponseSuccess<Boolean>> unlikeComment(
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user
    ){
        var result = commentService.unlikeComment(id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<ResponseSuccess<CommentVm>> createComment(
            @AuthenticationPrincipal User user,
            @RequestBody CreateCommentDTO dto
    ){
        var result = commentService.createComment(dto,user);

        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ResponseSuccess<CommentVm>> editComment(
            @AuthenticationPrincipal User user,
            @RequestBody EditCommentDTO dto,
            @PathVariable("id") int id
    ){
        var result = commentService.editComment(dto,id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseSuccess<Boolean>> deleteComment(
            @AuthenticationPrincipal User user,
            @PathVariable("id") int id
    ){
        var result = commentService.deleteComment(id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
