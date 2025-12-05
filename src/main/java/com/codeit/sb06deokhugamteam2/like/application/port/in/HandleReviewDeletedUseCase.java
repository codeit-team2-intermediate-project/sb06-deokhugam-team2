package com.codeit.sb06deokhugamteam2.like.application.port.in;

import java.util.UUID;

public interface HandleReviewDeletedUseCase {

    void handle(UUID reviewId);
}
