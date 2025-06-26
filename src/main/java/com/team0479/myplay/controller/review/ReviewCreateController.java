package com.team0479.myplay.controller.review;

import com.team0479.myplay.dto.calendar.PerformanceSearchDto;
import com.team0479.myplay.dto.review.ReviewCreateDto;
import com.team0479.myplay.service.calendar.PerformanceSearchService;
import com.team0479.myplay.service.review.ReviewCreateService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@CrossOrigin(origins = "*")
public class ReviewCreateController {

    private final ReviewCreateService reviewCreateService;
    private final PerformanceSearchService performanceSearchService;

    public ReviewCreateController(ReviewCreateService reviewCreateService, PerformanceSearchService performanceSearchService) {
        this.reviewCreateService = reviewCreateService;
        this.performanceSearchService = performanceSearchService;
    }

    /**
     * 리뷰 등록
     * POST /api/reviews/create
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createReview(@RequestBody ReviewCreateDto reviewCreateDto) {
        try {
            // 필수 필드 검증
            if (reviewCreateDto.getTitle() == null || reviewCreateDto.getTitle().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "리뷰 제목은 필수입니다."));
            }
            
            if (reviewCreateDto.getContent() == null || reviewCreateDto.getContent().trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "리뷰 내용은 필수입니다."));
            }
            
            if (reviewCreateDto.getRating() == null || reviewCreateDto.getRating() < 1 || reviewCreateDto.getRating() > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "평점은 1~5 사이의 값이어야 합니다."));
            }
            
            if (reviewCreateDto.getPerformanceId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "공연 ID는 필수입니다."));
            }
            
            if (reviewCreateDto.getUserId() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "사용자 ID는 필수입니다."));
            }

            Map<String, Object> result = reviewCreateService.createReview(reviewCreateDto);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "리뷰 등록에 실패했습니다: " + e.getMessage()));
        }
    }

    /**
     * 공연명 자동완성 검색
     * GET /api/reviews/performances/search?title={title}
     */
    @GetMapping("/performances/search")
    public ResponseEntity<List<PerformanceSearchDto>> searchPerformances(@RequestParam String title) {
        try {
            List<PerformanceSearchDto> performances = performanceSearchService.searchPerformancesByTitle(title);
            return ResponseEntity.ok(performances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 