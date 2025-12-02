package com.codeit.sb06deokhugamteam2.review.application.port.in;

public interface DeleteReviewUseCase {

    void deleteReview(String request, String header);

    void hardDeleteReview(String request, String header);
}
