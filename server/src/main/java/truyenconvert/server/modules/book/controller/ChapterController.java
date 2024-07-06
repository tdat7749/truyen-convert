package truyenconvert.server.modules.book.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateChapterDTO;
import truyenconvert.server.modules.book.dtos.EditChapterDTO;
import truyenconvert.server.modules.book.dtos.SetCoinDTO;
import truyenconvert.server.modules.book.service.ChapterService;
import truyenconvert.server.modules.book.vm.ChapterDetailVm;
import truyenconvert.server.modules.book.vm.ChapterVm;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService){
        this.chapterService = chapterService;
    }

    @PostMapping("/")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> createChapter(
            @RequestBody @Valid CreateChapterDTO dto,
            @AuthenticationPrincipal  User user)
    {
        var result = chapterService.createChapter(dto,user);

        return new ResponseEntity<>(result,HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> editChapter(
            @RequestBody @Valid EditChapterDTO dto,
            @AuthenticationPrincipal  User user,
            @PathVariable("id") int id
    ) {
        var result = chapterService.editChapter(dto,id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/{slug}/all")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ResponsePaging<List<ChapterVm>>>> getAllChapter(
            @RequestParam(value = "page_index",defaultValue = "0") int pageIndex,
            @PathVariable("slug") String slug
    ) {
        var result = chapterService.getAllChapter(pageIndex,slug);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ChapterDetailVm>> getChapterContent(
            @PathVariable("id") int id,
            @RequestParam(value = "hash_code",defaultValue = "M0uYgNXka8Yb5qie") String hashCode,
            @AuthenticationPrincipal User user
    ) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        var result = chapterService.getChapterContent(id,hashCode,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @PatchMapping("/{id}/unlock")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> unlockChapter(
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user
    ) {
        var result = chapterService.unlockChapter(id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @PatchMapping("/{id}/coin")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<ChapterVm>> setCoinForChapter(
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user,
            @RequestBody @Valid SetCoinDTO dto
            ) {
        var result = chapterService.setCoinForChapter(id,dto,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity<ResponseSuccess<Boolean>> deleteChapter(
            @PathVariable("id") int id,
            @AuthenticationPrincipal User user
    ) {
        var result = chapterService.deleteChapter(id,user);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
}
