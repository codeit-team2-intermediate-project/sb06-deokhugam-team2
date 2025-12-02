package com.codeit.sb06deokhugamteam2.review.adapter.in;

import com.codeit.sb06deokhugamteam2.review.application.dto.CursorPageRequestReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.CursorPageResponseReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.dto.ReviewCreateRequest;
import com.codeit.sb06deokhugamteam2.review.application.dto.ReviewDto;
import com.codeit.sb06deokhugamteam2.review.application.port.in.CreateReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.DeleteReviewUseCase;
import com.codeit.sb06deokhugamteam2.review.application.port.in.GetReviewQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController implements ReviewApi {

    private final CreateReviewUseCase createReviewUseCase;
    private final GetReviewQuery getReviewQuery;
    private final DeleteReviewUseCase deleteReviewUseCase;

    public ReviewController(
            CreateReviewUseCase createReviewUseCase,
            GetReviewQuery getReviewQuery,
            DeleteReviewUseCase deleteReviewUseCase
    ) {
        this.createReviewUseCase = createReviewUseCase;
        this.getReviewQuery = getReviewQuery;
        this.deleteReviewUseCase = deleteReviewUseCase;
    }

    @Override
    @PostMapping
    public ResponseEntity<ReviewDto> postReview(@RequestBody ReviewCreateRequest request) {
        ReviewDto response = createReviewUseCase.createReview(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    @GetMapping
    public ResponseEntity<CursorPageResponseReviewDto> getReviews(
            @ModelAttribute CursorPageRequestReviewDto request,
            @RequestHeader(value = "Deokhugam-Request-User-ID") String header
    ) {
        CursorPageResponseReviewDto response = getReviewQuery.readReviews(request, header);
        return ResponseEntity.ok(response);
    }

    @Override
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable(name = "reviewId") String request,
            @RequestHeader(value = "Deokhugam-Request-User-ID") String header
    ) {
        ReviewDto response = getReviewQuery.readReview(request, header);
        return ResponseEntity.ok(response);
    }

    @Override
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable(name = "reviewId") String request,
            @RequestHeader(value = "Deokhugam-Request-User-ID") String header
    ) {
        deleteReviewUseCase.deleteReview(request, header);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Override
    @DeleteMapping("/{reviewId}/hard")
    public ResponseEntity<Void> hardDeleteReview(
            @PathVariable(name = "reviewId") String request,
            @RequestHeader(value = "Deokhugam-Request-User-ID") String header
    ) {
        deleteReviewUseCase.hardDeleteReview(request, header);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
