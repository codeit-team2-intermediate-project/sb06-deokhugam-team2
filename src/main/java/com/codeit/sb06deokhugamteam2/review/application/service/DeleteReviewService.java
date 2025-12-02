package com.codeit.sb06deokhugamteam2.review.application.service;

import com.codeit.sb06deokhugamteam2.review.application.port.in.DeleteReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.domain.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteReviewService implements DeleteReviewUseCase {

    private final ReviewService reviewService;

    public DeleteReviewService(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Override
    @Transactional
    public void deleteReview(String request, String header) {
        String reviewId = request;
        String requestUserId = header;

        reviewService.delete(reviewId, requestUserId);
    }
}
