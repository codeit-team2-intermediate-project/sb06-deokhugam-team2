package com.codeit.sb06deokhugamteam2.review.adapter.out;

import com.codeit.sb06deokhugamteam2.review.adapter.out.entity.Review;
import com.codeit.sb06deokhugamteam2.review.domain.ReviewDomain;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ReviewJpaMapper {

    public Review toReview(ReviewDomain review) {
        ReviewDomain.Snapshot snapshot = review.createSnapshot();

        return new Review().id(snapshot.id())
                .rating(snapshot.rating())
                .content(snapshot.content())
                .likeCount(snapshot.likeCount())
                .commentCount(snapshot.commentCount())
                .createdAt(snapshot.createdAt())
                .updatedAt(snapshot.updatedAt());
    }

    public ReviewDomain toReviewDomain(Review review) {
        UUID id = review.id();
        UUID bookId = review.book().getId();
        UUID userId = review.user().getId();
        Integer rating = review.rating();
        String content = review.content();
        Integer likeCount = review.likeCount();
        Integer commentCount = review.commentCount();
        Instant createdAt = review.createdAt();
        Instant updatedAt = review.updatedAt();

        var snapshot = new ReviewDomain.Snapshot(
                id,
                bookId,
                userId,
                rating,
                content,
                likeCount,
                commentCount,
                createdAt,
                updatedAt
        );

        return ReviewDomain.loadSnapshot(snapshot);
    }
}
