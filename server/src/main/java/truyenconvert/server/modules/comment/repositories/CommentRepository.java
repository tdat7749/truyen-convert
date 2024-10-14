package truyenconvert.server.modules.comment.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import truyenconvert.server.models.Comment;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.comment.vm.CommentVm;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

//    @Query("SELECT new truyenconvert.server.modules.comment.vm.CommentVm(c.id,c.content,cast(c.updatedAt AS string),cast(c.createdAt AS string)," +
//            "count(li), " +
//            "(CASE WHEN (EXISTS (SELECT 1 FROM c.likes AS l WHERE l =: user)) THEN true ELSE false END), " +
//            "(count(cm))) " +
//            "FROM Comment AS c LEFT JOIN c.likes AS li " +
//            "LEFT JOIN  c.comments AS cm " +
//            "WHERE c.id =:id " +
//            "GROUP BY c.id,c.content,c.createdAt,c.updatedAt")
//    Optional<CommentVm> getById(int id, User user);


    @Query(value = "SELECT CASE WHEN count(lc) > 0 THEN true ELSE false END FROM like_comments as lc where lc.user_id =:userId AND lc.comment_id =:commentId",nativeQuery = true)
    boolean getIsLiked(int userId,int commentId);

    @Query("SELECT new truyenconvert.server.modules.comment.vm.CommentVm(c.id,c.content,cast(c.updatedAt AS string),cast(c.createdAt AS string)," +
            "(count(DISTINCT li)), " +
            "CASE WHEN :user IS NULL THEN false ELSE (CASE WHEN (EXISTS (SELECT 1 FROM c.likes AS l WHERE l =:user)) THEN true ELSE false END) END, " +
            "(count(DISTINCT cs))) " +
            "FROM Comment AS c LEFT JOIN c.likes AS li " +
            "INNER JOIN c.book as b " +
            "LEFT JOIN c.comments as cs " +
            "WHERE b.slug =:slug AND c.parent IS NULL " +
            "GROUP BY c.id,c.content,c.createdAt,c.updatedAt")
    Page<CommentVm> getAllComments(Pageable paging,String slug,User user);

    @Query("SELECT new truyenconvert.server.modules.comment.vm.CommentVm(c.id,c.content,cast(c.updatedAt AS string),cast(c.createdAt AS string)," +
            "count(li), " +
            "CASE WHEN :user IS NULL THEN false ELSE (CASE WHEN (EXISTS (SELECT 1 FROM c.likes AS l WHERE l =:user)) THEN true ELSE false END) END) " +
            "FROM Comment AS c LEFT JOIN c.likes AS li " +
            "WHERE c.parent =:comment " +
            "GROUP BY c.id,c.content,c.createdAt,c.updatedAt")
    List<CommentVm> getAllReplyComment(Comment comment, User user);
}
