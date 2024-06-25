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
public class ChapterServiceImpl implements ChapterService {
    @Override
    public ResponseSuccess<Boolean> createChapter(CreateChapterDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<Boolean> editChapter(EditChapterDTO dto, User user) {
        return null;
    }

    @Override
    public ResponseSuccess<List<ChapterVm>> getAllChapter(int pageIndex, String bookSlug) {
        return null;
    }

    @Override
    public ResponseSuccess<ChapterDetailVm> getChapterContent(int id,String hashCode) {
        return null;
    }

    @Override
    public ResponseSuccess<ChapterDetailVm> unlockChapter(int chapterId, User user) {
        return null;
    }
}
