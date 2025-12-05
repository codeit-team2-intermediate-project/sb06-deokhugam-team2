package com.codeit.sb06deokhugamteam2.like.adapter.in.event;

import com.codeit.sb06deokhugamteam2.like.application.port.in.HandleReviewDeletedUseCase;
import com.codeit.sb06deokhugamteam2.like.application.port.in.HandleReviewLikeCanceledUseCase;
import com.codeit.sb06deokhugamteam2.like.application.port.in.HandleReviewLikedUseCase;
import com.codeit.sb06deokhugamteam2.review.domain.event.ReviewDeletedEvent;
import com.codeit.sb06deokhugamteam2.review.domain.event.ReviewLikeCanceledEvent;
import com.codeit.sb06deokhugamteam2.review.domain.event.ReviewLikedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class LikeReviewEventHandler {

    private final HandleReviewDeletedUseCase handleReviewDeletedUseCase;
    private final HandleReviewLikedUseCase handleReviewLikedUseCase;
    private final HandleReviewLikeCanceledUseCase handleReviewLikeCanceledUseCase;

    public LikeReviewEventHandler(
            HandleReviewDeletedUseCase handleReviewDeletedUseCase,
            HandleReviewLikedUseCase handleReviewLikedUseCase,
            HandleReviewLikeCanceledUseCase handleReviewLikeCanceledUseCase
    ) {
        this.handleReviewDeletedUseCase = handleReviewDeletedUseCase;
        this.handleReviewLikedUseCase = handleReviewLikedUseCase;
        this.handleReviewLikeCanceledUseCase = handleReviewLikeCanceledUseCase;
    }

    @EventListener(ReviewDeletedEvent.class)
    public void handleReviewDeletedEvent(ReviewDeletedEvent event) {
        handleReviewDeletedUseCase.handle(event.reviewId());
    }

    @EventListener(ReviewLikedEvent.class)
    public void handleReviewLikedEvent(ReviewLikedEvent event) {
        handleReviewLikedUseCase.handle(event.reviewId(), event.userId(), event.likedAt());
    }
    
    @EventListener(ReviewLikeCanceledEvent.class)
    public void handleReviewLikeCanceledEvent(ReviewLikeCanceledEvent event) {
        handleReviewLikeCanceledUseCase.handle(event.reviewId(), event.userId());
    }
}
