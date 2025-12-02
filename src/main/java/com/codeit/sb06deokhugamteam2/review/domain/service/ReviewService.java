package com.codeit.sb06deokhugamteam2.review.domain.service;

import com.codeit.sb06deokhugamteam2.review.domain.ReviewDomain;
import com.codeit.sb06deokhugamteam2.review.domain.exception.AlreadyExistsReviewException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewBookNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewUserNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.repository.BookRepository;
import com.codeit.sb06deokhugamteam2.review.domain.repository.ReviewRepository;
import com.codeit.sb06deokhugamteam2.review.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReviewService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public ReviewService(
            BookRepository bookRepository,
            UserRepository userRepository,
            ReviewRepository reviewRepository
    ) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public ReviewDomain create(String bookId, String userId, Integer rating, String content) {
        UUID bookIdUuid = UUID.fromString(bookId);
        UUID userIdUuid = UUID.fromString(userId);
        return create(bookIdUuid, userIdUuid, rating, content);

    }

    private ReviewDomain create(UUID bookId, UUID userId, Integer rating, String content) {
        if (!bookRepository.existsById(bookId)) {
            throw new ReviewBookNotFoundException(bookId);
        }
        if (!userRepository.existsById(userId)) {
            throw new ReviewUserNotFoundException(userId);
        }
        if (reviewRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new AlreadyExistsReviewException(bookId);
        }
        ReviewDomain newReview = ReviewDomain.create(bookId, userId, rating, content);
        reviewRepository.addReview(newReview);
        bookRepository.updateForReviewCreation(newReview.bookId(), newReview.rating());
        return newReview;
    }

    public void delete(String reviewId, String requestUserId) {
        UUID reviewIdUuid = UUID.fromString(reviewId);
        UUID requestUserIdUuid = UUID.fromString(requestUserId);
        delete(reviewIdUuid, requestUserIdUuid);
    }

    private void delete(UUID reviewId, UUID requestUserId) {
        ReviewDomain reviewToDelete = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        reviewToDelete.requireOwner(requestUserId);
        reviewRepository.delete(reviewToDelete);
    }

    public void hardDelete(String reviewId, String requestUserId) {
        UUID reviewIdUuid = UUID.fromString(reviewId);
        UUID requestUserIdUuid = UUID.fromString(requestUserId);
        hardDelete(reviewIdUuid, requestUserIdUuid);
    }

    private void hardDelete(UUID reviewId, UUID requestUserId) {
        ReviewDomain reviewToDelete = reviewRepository.findByIdWithoutDeleted(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        reviewToDelete.requireOwner(requestUserId);
        reviewRepository.hardDelete(reviewToDelete);
    }
}
