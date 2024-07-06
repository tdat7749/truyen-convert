package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.models.*;
import truyenconvert.server.modules.book.vm.AuthorVm;
import truyenconvert.server.modules.book.vm.ChapterDetailVm;
import truyenconvert.server.modules.book.vm.ChapterVm;
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

    ChapterDetailVm getChapterDetailVm(Chapter chapter, String hashCode,boolean isUnlocked) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException;
}
