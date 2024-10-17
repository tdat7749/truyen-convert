package truyenconvert.server.modules.review.service;

import org.springframework.stereotype.Service;
import truyenconvert.server.commons.ResponsePaging;
import truyenconvert.server.commons.ResponseSuccess;
import truyenconvert.server.models.User;
import truyenconvert.server.modules.review.dtos.CreateReviewDTO;
import truyenconvert.server.modules.review.vm.ReviewVm;

import java.util.List;

@Service
public interface ReviewService {
    ResponseSuccess<ReviewVm> createReview(CreateReviewDTO dto,int bookId, User user);
    ResponseSuccess<ResponsePaging<List<ReviewVm>>> getAllReviews(int pageIndex, String slug);
    ResponseSuccess<Boolean> deleteReview(int id, User user);
}
