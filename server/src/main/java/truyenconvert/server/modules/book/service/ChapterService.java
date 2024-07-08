package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.Book;
import truyenconvert.server.models.Chapter;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateChapterDTO;
import truyenconvert.server.modules.book.dtos.EditChapterDTO;
import truyenconvert.server.modules.book.dtos.SetCoinDTO;
import truyenconvert.server.modules.book.vm.ChapterDetailVm;
import truyenconvert.server.modules.book.vm.ChapterVm;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public interface ChapterService {
    ResponseSuccess<Boolean> createChapter(CreateChapterDTO dto, User user);
    ResponseSuccess<Boolean> editChapter(EditChapterDTO dto,int id, User user);
    ResponseSuccess<ResponsePaging<List<ChapterVm>>> getAllChapter(int pageIndex, String bookSlug);
    ResponseSuccess<ChapterDetailVm> getChapterContent(int chapter,String slug,String hashCode,User user) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
    ResponseSuccess<Boolean> unlockChapter(int chapterId, User user);

    ResponseSuccess<Boolean> deleteChapter(int chapterId,User user);
    ResponseSuccess<ChapterVm> setCoinForChapter(int chapterId, SetCoinDTO dto, User user);
    Optional<Chapter> findById(int id);
}
