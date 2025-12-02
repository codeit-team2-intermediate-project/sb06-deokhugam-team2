package com.codeit.sb06deokhugamteam2.review.domain.repository;

import java.util.UUID;

public interface BookRepository {

    boolean existsById(UUID bookId);

    void updateForReviewCreation(UUID bookId, int rating);
}
