package com.codeit.sb06deokhugamteam2.review.infra.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record CursorPageRequestReviewDto(
        @Schema(description = "작성자 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID userId,

        @Schema(description = "도서 ID", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID bookId,

        @Schema(description = "검색 키워드(작성자 닉네임 | 내용)", example = "홍길동")
        String keyword,

        @Schema(description = "정렬 기준(createdAt | rating)", defaultValue = "createdAt", example = "createdAt")
        String orderBy,

        @Schema(description = "정렬 방향", allowableValues = {"ASC", "DESC"}, defaultValue = "DESC", example = "DESC")
        String direction,

        @Schema(description = "커서 페이지네이션 커서")
        String cursor,

        @Schema(description = "보조 커서(createdAt)")
        LocalDateTime after,

        @Schema(description = "페이지 크기", defaultValue = "50", example = "50")
        Integer limit,

        UUID requestUserId
) {
    public CursorPageRequestReviewDto {
        if (orderBy == null) {
            orderBy = "createdAt";
        }
        if (direction == null) {
            direction = "DESC";
        }
        if (limit == null) {
            limit = 50;
        }
    }
}
