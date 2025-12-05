package com.codeit.sb06deokhugamteam2.review.application.service;

import com.codeit.sb06deokhugamteam2.review.application.dto.request.ReviewCreateRequest;
import com.codeit.sb06deokhugamteam2.review.application.dto.request.ReviewUpdateRequest;
import com.codeit.sb06deokhugamteam2.review.application.dto.response.ReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.response.ReviewLikeDto;
import com.codeit.sb06deokhugamteam2.review.application.port.in.CreateReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.DeleteReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.ToggleReviewLikeUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.UpdateReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.out.*;
import com.codeit.sb06deokhugamteam2.review.domain.exception.AlreadyExistsReviewException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewBookNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.exception.ReviewUserNotFoundException;
import com.codeit.sb06deokhugamteam2.review.domain.model.*;
import com.codeit.sb06deokhugamteam2.review.domain.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class ReviewCommandService
        implements CreateReviewUseCase, UpdateReviewUseCase, DeleteReviewUseCase, ToggleReviewLikeUseCase {

    private final ReviewService service;
    private final ReviewEventPublisherPort eventPublisher;
    private final LoadReviewUserRepositoryPort loadUserRepository;
    private final LoadReviewBookRepositoryPort loadBookRepository;
    private final SaveReviewBookRepositoryPort saveBookRepository;
    private final LoadReviewRepositoryPort loadReviewRepository;
    private final SaveReviewRepositoryPort saveReviewRepository;
    private final LoadReviewLikeRepositoryPort loadReviewLikeRepository;

    public ReviewCommandService(
            ReviewService service,
            ReviewEventPublisherPort eventPublisher,
            LoadReviewUserRepositoryPort loadUserRepository,
            LoadReviewBookRepositoryPort loadBookRepository,
            SaveReviewBookRepositoryPort saveBookRepository,
            LoadReviewRepositoryPort loadReviewRepository,
            SaveReviewRepositoryPort saveReviewRepository,
            LoadReviewLikeRepositoryPort loadReviewLikeRepository
    ) {
        this.service = service;
        this.eventPublisher = eventPublisher;
        this.loadUserRepository = loadUserRepository;
        this.loadBookRepository = loadBookRepository;
        this.saveBookRepository = saveBookRepository;
        this.loadReviewRepository = loadReviewRepository;
        this.saveReviewRepository = saveReviewRepository;
        this.loadReviewLikeRepository = loadReviewLikeRepository;
    }

    @Override
    public ReviewDto createReview(ReviewCreateRequest requestBody) {
        UUID bookId = UUID.fromString(requestBody.bookId());
        UUID userId = UUID.fromString(requestBody.userId());
        var rating = new ReviewRatingDomain(requestBody.rating());
        var content = new ReviewContentDomain(requestBody.content());
        ReviewDomain review = ReviewDomain.create(bookId, userId, rating, content);

        if (!loadUserRepository.existsById(userId)) {
            throw new ReviewUserNotFoundException(userId);
        }
        if (loadReviewRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new AlreadyExistsReviewException(bookId);
        }
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(bookId)
                .orElseThrow(() -> new ReviewBookNotFoundException(bookId));
        service.registerReview(review, book);
        saveReviewRepository.save(review.toSnapshot());
        saveBookRepository.update(book);
        review.events().forEach(eventPublisher::publish);
        review.clearEvents();

        return loadReviewRepository.findById(review.id(), null)
                .orElseThrow(() -> new ReviewNotFoundException(review.id()));
    }

    @Override
    public void softDeleteReview(String path, String header) {
        UUID reviewId = UUID.fromString(path);
        UUID requestUserId = UUID.fromString(header);

        ReviewDomain review = loadReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(review.bookId())
                .orElseThrow(() -> new ReviewBookNotFoundException(review.bookId()));
        service.hideReview(review, requestUserId, book);
        saveReviewRepository.softDelete(review.id());
        saveBookRepository.update(book);
        review.events().forEach(eventPublisher::publish);
        review.clearEvents();
    }

    @Override
    public void deleteReview(String path, String header) {
        UUID reviewId = UUID.fromString(path);
        UUID requestUserId = UUID.fromString(header);

        ReviewDomain review = loadReviewRepository.findByIdWithoutDeleted(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(review.bookId())
                .orElseThrow(() -> new ReviewBookNotFoundException(review.bookId()));
        service.deleteReview(review, requestUserId, book);
        saveReviewRepository.hardDelete(review.id());
        saveBookRepository.update(book);
        review.events().forEach(eventPublisher::publish);
        review.clearEvents();
    }

    @Override
    public ReviewDto updateReview(String path, String header, ReviewUpdateRequest requestBody) {
        UUID reviewId = UUID.fromString(path);
        UUID requestUserId = UUID.fromString(header);
        var newRating = new ReviewRatingDomain(requestBody.rating());
        var newContent = new ReviewContentDomain(requestBody.content());

        ReviewDomain review = loadReviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        ReviewBookDomain book = loadBookRepository.findByIdForUpdate(review.bookId())
                .orElseThrow(() -> new ReviewBookNotFoundException(review.bookId()));
        service.editReview(review, newRating, newContent, requestUserId, book);
        saveReviewRepository.update(review.toSnapshot());
        saveBookRepository.update(book);
        review.events().forEach(eventPublisher::publish);
        review.clearEvents();

        return loadReviewRepository.findById(reviewId, requestUserId)
                .orElseThrow(() -> new ReviewNotFoundException(review.id()));
    }

    @Override
    public ReviewLikeDto toggleReviewLike(String path, String header) {
        UUID reviewId = UUID.fromString(path);
        UUID userId = UUID.fromString(header);

        if (!loadUserRepository.existsById(userId)) {
            throw new ReviewUserNotFoundException(userId);
        }
        ReviewDomain review = loadReviewRepository.findByIdForUpdate(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException(reviewId));
        ReviewLikeDomain reviewLike = loadReviewLikeRepository.findById(reviewId, userId)
                .orElseGet(() -> new ReviewLikeDomain(reviewId, userId, false));
        service.toggleReviewLike(review, reviewLike);
        saveReviewRepository.update(review.toSnapshot());
        review.events().forEach(eventPublisher::publish);
        review.clearEvents();

        return new ReviewLikeDto(reviewLike.reviewId(), reviewLike.userId(), reviewLike.isLiked());
    }
}
