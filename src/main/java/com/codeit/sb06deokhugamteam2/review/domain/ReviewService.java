package com.codeit.sb06deokhugamteam2.review.domain;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService {

    public ReviewDomain registerReview(
            UUID userId,
            ReviewRating rating,
            ReviewContent content,
            ReviewBookDomain book
    ) {
        UUID bookId = book.id();
        ReviewDomain review = ReviewDomain.write(bookId, userId, rating, content);
        book.increaseReviewCount().plusReviewRating(review.rating());
        return review;
    }

    public void deleteReview(ReviewDomain review, UUID requestUserId, ReviewBookDomain book) {
        review.verifyOwner(requestUserId);
        book.decreaseReviewCount().minusReviewRating(review.rating());
    }

    public ReviewDomain editReview(
            ReviewDomain review,
            ReviewRating rating,
            ReviewContent content,
            UUID requestUserId,
            ReviewBookDomain book
    ) {
        ReviewRating oldRating = review.rating();
        ReviewRating newRating = rating;
        review.verifyOwner(requestUserId).edit(newRating, content);
        book.minusReviewRating(oldRating).plusReviewRating(newRating);
        return review;
    }
}
