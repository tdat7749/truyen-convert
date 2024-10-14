package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.models.*;
import truyenconvert.server.modules.book.vm.*;
import truyenconvert.server.modules.classifies.vm.CategoryVm;
import truyenconvert.server.modules.classifies.vm.SectVm;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;
import truyenconvert.server.modules.comment.vm.CommentVm;
import truyenconvert.server.modules.report.vm.ReportTypeVm;
import truyenconvert.server.modules.report.vm.ReportVm;
import truyenconvert.server.modules.users.vm.UserVm;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public interface MappingService {
    ReportTypeVm getReportTypeVm(ReportType reportType);
    ReportVm getReportVm(Report report);
    UserVm getUserVm(User user);
    AuthorVm getAuthorVm(Author author);

    ChapterVm getChapterVm(Chapter chapter);

    ChapterDetailVm getChapterDetailVm(Book book,Chapter chapter, String hashCode,boolean isUnlocked,int currentNewestChapter) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;

    BookVm getBookVm(Book book);
    SectVm getSectVm(Sect sect);
    WorldContextVm getWorldContextVm(WorldContext worldContext);
    CategoryVm getCategoryVm(Category category);
    PosterVm getPosterVm(String thumbnail);

    CommentVm getCommentVm(Comment comment);

    BookSimpleVm getBookSimpleVm(Book book);
}
