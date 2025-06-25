package com.team0479.myplay.controller.review;

import com.team0479.myplay.dto.review.ReviewDetailDto;
import com.team0479.myplay.service.review.ReviewDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewDetailController {

    private final ReviewDetailService reviewDetailService;

    public ReviewDetailController(ReviewDetailService reviewDetailService) {
        this.reviewDetailService = reviewDetailService;
    }

    /**
     * 리뷰 상세 정보 조회
     * GET /api/reviews/{reviewId}?userId={userId}
     */
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDetailDto> getReviewDetail(
            @PathVariable Long reviewId,
            @RequestParam(required = false) Long userId) {
        try {
            ReviewDetailDto reviewDetail = reviewDetailService.getReviewDetail(reviewId, userId);
            return ResponseEntity.ok(reviewDetail);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 리뷰 좋아요 토글 (좋아요/좋아요 취소)
     * POST /api/reviews/{reviewId}/like
     */
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Map<String, Object>> toggleLike(
            @PathVariable Long reviewId,
            @RequestBody Map<String, Long> requestBody) {
        try {
            Long userId = requestBody.get("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "userId is required"));
            }

            Map<String, Object> result = reviewDetailService.toggleLike(reviewId, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to toggle like"));
        }
    }
} 