package com.codeit.sb06deokhugamteam2.review.application.service;

import com.codeit.sb06deokhugamteam2.review.application.dto.CursorPageRequestReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.CursorPageResponseReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.ReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.port.in.GetReviewQuery;
import com.codeit.sb06deokhugamteam2.review.application.port.out.QueryReviewPort;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class GetReviewService implements GetReviewQuery {

    private final QueryReviewPort queryReviewPort;

    public GetReviewService(QueryReviewPort queryReviewPort) {
        this.queryReviewPort = queryReviewPort;
    }

    @Override
    public CursorPageResponseReviewDto readReviews(CursorPageRequestReviewDto request, String header) {
        UUID requestUserId = UUID.fromString(header);
        return queryReviewPort.findAll(request, requestUserId);
    }

    @Override
    public ReviewDto readReview(String request, String header) {
        UUID reviewId = UUID.fromString(request);
        UUID requestUserId = UUID.fromString(header);
        return queryReviewPort.findById(reviewId, requestUserId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
    }
}
