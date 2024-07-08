package truyenconvert.server.modules.common.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.models.*;
import truyenconvert.server.models.enums.BookState;
import truyenconvert.server.models.enums.BookStatus;
import truyenconvert.server.modules.book.service.ChapterService;
import truyenconvert.server.modules.book.vm.*;
import truyenconvert.server.modules.classifies.vm.CategoryVm;
import truyenconvert.server.modules.classifies.vm.SectVm;
import truyenconvert.server.modules.classifies.vm.WorldContextVm;
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

    public MappingServiceImpl(
            UtilitiesService utilitiesService
    ){
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
                .displayName(user.getDisplayName())
                .level(user.getLevel())
                .exp(user.getExp())
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
    public ChapterDetailVm getChapterDetailVm(Book book,Chapter chapter, String hashCode,boolean isUnlocked,int newestChapter) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String chapterContent = chapter.getContent();
        ChapterDetailVm chapterDetailVm = ChapterDetailVm.builder()
                .createdAt(chapter.getCreatedAt().toString())
                .updatedAt(chapter.getUpdatedAt().toString())
                .id(chapter.getId())
                .title(chapter.getTitle())
                .timeExpired(chapter.getTimeExpired() != null ? chapter.getTimeExpired().toString() : null)
                .unLockCoin(chapter.getUnLockCoin())
                .chapter(chapter.getChapter())
                .previousChapter("/truyen/" + book.getSlug() + "/chuong-" + (chapter.getChapter() - 1))
                .nextChapter("/truyen/" + book.getSlug() + "/chuong-" + (chapter.getChapter() + 1))
                .totalChapter(newestChapter)
                .isUnlock(isUnlocked)
                .build();

        if(chapterDetailVm.getChapter() == 1){
            chapterDetailVm.setPreviousChapter("/truyen/" + book.getSlug());
        }
        if(chapterDetailVm.getChapter() >= newestChapter){
            chapterDetailVm.setNextChapter("/truyen/" + book.getSlug());
        }

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

    @Override
    public BookVm getBookVm(Book book) {
        return BookVm.builder()
                .id(book.getId())
                .title(book.getTitle())
                .introduction(book.getIntroduction())
                .posters(this.getPosterVm(book.getThumbnail()))
                .sect(this.getSectVm(book.getSect()))
                .worldContext(this.getWorldContextVm(book.getWorldContext()))
                .category(this.getCategoryVm(book.getCategory()))
                .isVip(book.isVip())
                .isDelete(book.isDeleted())
                .originalLink(book.getOriginalLink())
                .originalName(book.getOriginalName())
                .creater(this.getUserVm(book.getUser()))
                .author(this.getAuthorVm(book.getAuthor()))
                .score(book.getScore())
                .slug(book.getSlug())
                .newChapAt(book.getNewChapAt().toString())
                .createdAt(book.getCreatedAt().toString())
                .updatedAt(book.getUpdatedAt().toString())
                .view(book.getView())
                .stateName(book.getState() == BookState.Closed ? "Đã đóng" : book.getState() == BookState.Pending ? "Đang đợi" : "Đã xuất bản")
                .statusName(book.getStatus() == BookStatus.Continued ? "Còn tiếp" : book.getStatus() == BookStatus.Compeleted ? "Hoàn thành" : "Tạm dừng")
                .status(book.getStatus() == BookStatus.Continued ? 0 : book.getStatus() == BookStatus.Compeleted ? 1 : 2)
                .state(book.getState() == BookState.Closed ? 2 : book.getState() == BookState.Pending ? 0 : 1)
                .countComment(book.getCountComment())
                .countWord(book.getCountWord())
                .countEvaluation(book.getCountEvaluation())
                .build();
    }

    @Override
    public SectVm getSectVm(Sect sect) {
        return SectVm.builder()
                .id(sect.getId())
                .description(sect.getDescription())
                .title(sect.getTitle())
                .createdAt(sect.getCreatedAt().toString())
                .updatedAt(sect.getUpdatedAt().toString())
                .build();
    }

    @Override
    public WorldContextVm getWorldContextVm(WorldContext worldContext) {
        return WorldContextVm.builder()
                .id(worldContext.getId())
                .description(worldContext.getDescription())
                .title(worldContext.getTitle())
                .createdAt(worldContext.getCreatedAt().toString())
                .updatedAt(worldContext.getUpdatedAt().toString())
                .build();
    }

    @Override
    public CategoryVm getCategoryVm(Category category) {
        return CategoryVm.builder()
                .id(category.getId())
                .title(category.getTitle())
                .description(category.getDescription())
                .createdAt(category.getCreatedAt().toString())
                .updatedAt(category.getUpdatedAt().toString())
                .build();
    }

    @Override
    public PosterVm getPosterVm(String thumbnail) {
        String[] ob = thumbnail.split("default\\.");
        return PosterVm.builder()
                .xDefault(ob[0] + "default." + ob[1])
                .x150(ob[0] + "150." + ob[1])
                .x300(ob[0] + "300." + ob[1])
                .x600(ob[0] + "600." + ob[1])
                .build();
    }
}
