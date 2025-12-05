package com.codeit.sb06deokhugamteam2.like.application.service;

import com.codeit.sb06deokhugamteam2.like.application.port.in.HandleReviewDeletedUseCase;
import com.codeit.sb06deokhugamteam2.like.application.port.in.HandleReviewLikeCanceledUseCase;
import com.codeit.sb06deokhugamteam2.like.application.port.in.HandleReviewLikedUseCase;
import com.codeit.sb06deokhugamteam2.like.application.port.out.SaveLikeReviewRepositoryPort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class LikeReviewCommandService
        implements HandleReviewDeletedUseCase, HandleReviewLikedUseCase, HandleReviewLikeCanceledUseCase {

    private final SaveLikeReviewRepositoryPort saveReviewLikeRepository;

    public LikeReviewCommandService(SaveLikeReviewRepositoryPort saveReviewLikeRepository) {
        this.saveReviewLikeRepository = saveReviewLikeRepository;
    }

    @Override
    public void handle(UUID reviewId) {
        saveReviewLikeRepository.delete(reviewId);
    }

    @Override
    public void handle(UUID reviewId, UUID userId, Instant likedAt) {
        saveReviewLikeRepository.save(reviewId, userId, likedAt);
    }

    @Override
    public void handle(UUID reviewId, UUID userId) {
        saveReviewLikeRepository.delete(reviewId, userId);
    }
}
