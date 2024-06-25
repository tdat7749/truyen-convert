package truyenconvert.server.modules.book.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.book.dtos.CreateChapterDTO;
import truyenconvert.server.modules.book.dtos.EditChapterDTO;
import truyenconvert.server.modules.book.vm.ChapterDetailVm;
import truyenconvert.server.modules.book.vm.ChapterVm;

import java.util.List;

@Service
public interface ChapterService {
    ResponseSuccess<Boolean> createChapter(CreateChapterDTO dto, User user);
    ResponseSuccess<Boolean> editChapter(EditChapterDTO dto, User user);
    ResponseSuccess<List<ChapterVm>> getAllChapter(int pageIndex, String bookSlug);
    ResponseSuccess<ChapterDetailVm> getChapterContent(int id,String hashCode);
    ResponseSuccess<ChapterDetailVm> unlockChapter(int chapterId, User user);
}
