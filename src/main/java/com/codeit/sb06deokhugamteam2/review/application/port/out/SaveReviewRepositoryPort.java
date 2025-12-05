package com.codeit.sb06deokhugamteam2.review.application.port.out;

import com.codeit.sb06deokhugamteam2.review.domain.model.ReviewDomain;

import java.util.UUID;

public interface SaveReviewRepositoryPort {

    void save(ReviewDomain.Snapshot reviewSnapshot);

    void softDelete(UUID reviewId);

    void hardDelete(UUID reviewId);

    void update(ReviewDomain.Snapshot reviewSnapshot);
}
