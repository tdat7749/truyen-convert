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
import java.time.LocalDateTime;

@Service
public class MappingServiceImpl implements MappingService{

    private final UtilitiesService utilitiesService;

    public MappingServiceImpl(UtilitiesService utilitiesService){
        this.utilitiesService = utilitiesService;
    }

    @Override
    public ReportTypeVm getReportTypeVm(ReportType reportType) {
        return ReportTypeVm.builder()
                .title(reportType.getTitle())
                .description(reportType.getDescription())
                .note(reportType.getNote() != null ? reportType.getNote() : null)
                .build();
    }

    @Override
    public ReportVm getReportVm(Report report) {
        return ReportVm.builder()
                .createdAt(report.getCreatedAt().toString())
                .updatedAt(report.getUpdatedAt().toString())
                .content(report.getContent())
                .user(this.getUserVm(report.getUser()))
                .handler(report.getUser() != null ? this.getUserVm(report.getUser()) : null)
                .reportStatus(report.getStatus().name())
                .build();
    }

    @Override
    public UserVm getUserVm(User user) {
        return UserVm.builder()
                .id(user.getId())
                .avatar(user.getAvatar())
                .email(user.getEmail())
                .displayName(user.getDisplayName())
                .build();
    }

    @Override
    public AuthorVm getAuthorVm(Author author) {
        return AuthorVm.builder()
                .id(author.getId())
                .authorName(author.getAuthorName())
                .originalAuthorName(author.getOriginalAuthorName())
                .build();
    }

    @Override
    public ChapterVm getChapterVm(Chapter chapter) {
        return ChapterVm.builder()
                .createdAt(chapter.getCreatedAt().toString())
                .updatedAt(chapter.getUpdatedAt().toString())
                .id(chapter.getId())
                .title(chapter.getTitle())
                .timeExpired(chapter.getTimeExpired() != null ? chapter.getTimeExpired().toString() : null)
                .unLockCoin(chapter.getUnLockCoin())
                .chapter(chapter.getChapter())
                .build();
    }

    @Override
    public ChapterDetailVm getChapterDetailVm(Chapter chapter, String hashCode,boolean isUnlocked) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String chapterContent = chapter.getContent();
        ChapterDetailVm chapterDetailVm = ChapterDetailVm.builder()
                .createdAt(chapter.getCreatedAt().toString())
                .updatedAt(chapter.getUpdatedAt().toString())
                .id(chapter.getId())
                .title(chapter.getTitle())
                .timeExpired(chapter.getTimeExpired() != null ? chapter.getTimeExpired().toString() : null)
                .unLockCoin(chapter.getUnLockCoin())
                .chapter(chapter.getChapter())
                .build();

        if(isUnlocked){
            chapterDetailVm.setContent(utilitiesService.AesEncrypt(chapterContent,hashCode));
            return chapterDetailVm;
        }

        if(chapter.getUnLockCoin() > 0 && chapter.getTimeExpired() != null && chapter.getTimeExpired().isAfter(LocalDateTime.now())){
            chapterDetailVm.setContent(utilitiesService.AesEncrypt(chapterContent.substring(0,200),hashCode));
            return chapterDetailVm;
        }

        if(chapter.getUnLockCoin() > 0 && chapter.getTimeExpired() == null){
            chapterDetailVm.setContent(utilitiesService.AesEncrypt(chapterContent.substring(0,200),hashCode));
            return chapterDetailVm;
        }

        chapterDetailVm.setContent(utilitiesService.AesEncrypt(chapterContent,hashCode));

        return chapterDetailVm;
    }
}
