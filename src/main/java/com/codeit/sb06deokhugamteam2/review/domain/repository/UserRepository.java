package com.codeit.sb06deokhugamteam2.review.domain.repository;

import java.util.UUID;

public interface UserRepository {

    boolean existsById(UUID userId);
}
