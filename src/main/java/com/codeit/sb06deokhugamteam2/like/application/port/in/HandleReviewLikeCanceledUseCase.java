package com.codeit.sb06deokhugamteam2.like.application.port.in;

import java.util.UUID;

public interface HandleReviewLikeCanceledUseCase {

    void handle(UUID reviewId, UUID userId);
}
