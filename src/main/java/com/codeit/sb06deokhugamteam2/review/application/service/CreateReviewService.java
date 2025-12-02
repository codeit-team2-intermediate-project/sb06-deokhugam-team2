package com.codeit.sb06deokhugamteam2.review.application.service;

import com.codeit.sb06deokhugamteam2.review.application.dto.ReviewCreateRequest;
import com.codeit.sb06deokhugamteam2.review.application.dto.ReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.port.in.CreateReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.out.QueryReviewPort;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewDomain;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreateReviewService implements CreateReviewUseCase {

    private final ReviewService reviewService;
    private final QueryReviewPort queryReviewPort;

    public CreateReviewService(ReviewService reviewService, QueryReviewPort queryReviewPort) {
        this.reviewService = reviewService;
        this.queryReviewPort = queryReviewPort;
    }

    @Override
    public ReviewDto createReview(ReviewCreateRequest request) {
        String bookId = request.bookId();
        String userId = request.userId();
        Integer rating = request.rating();
        String content = request.content();

        ReviewDomain newReview = reviewService.create(bookId, userId, rating, content);
        return queryReviewPort.findById(newReview.id(), null)
                .orElseThrow(() -> new ReviewNotFoundException(newReview.id()));
    }
}
