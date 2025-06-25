package com.team0479.myplay.controller.plaza;

import com.team0479.myplay.dto.plaza.ReviewListDto;
import com.team0479.myplay.service.plaza.ReviewPlazaService;
import com.team0479.myplay.service.calendar.PerformanceSearchService;
import com.team0479.myplay.dto.calendar.PerformanceSearchDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plaza")
@CrossOrigin(origins = "*")
public class ReviewPlazaController {

    private final ReviewPlazaService reviewPlazaService;
    private final PerformanceSearchService performanceSearchService;

    public ReviewPlazaController(ReviewPlazaService reviewPlazaService, 
                                PerformanceSearchService performanceSearchService) {
        this.reviewPlazaService = reviewPlazaService;
        this.performanceSearchService = performanceSearchService;
    }

    /**
     * 공연명 자동완성 검색
     * GET /api/plaza/performances/search?title={title}
     */
    @GetMapping("/performances/search")
    public ResponseEntity<List<PerformanceSearchDto>> searchPerformancesForAutocomplete(@RequestParam String title) {
        try {
            List<PerformanceSearchDto> performances = performanceSearchService.searchPerformancesByTitle(title);
            return ResponseEntity.ok(performances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공연명으로 리뷰 검색
     * 검색 결과가 없으면 조회수 많은 순으로 전체 리뷰 반환
     * GET /api/plaza/reviews/search?title={title}
     */
    @GetMapping("/reviews/search")
    public ResponseEntity<List<ReviewListDto>> searchReviewsByPerformance(@RequestParam(required = false) String title) {
        try {
            List<ReviewListDto> reviews = reviewPlazaService.searchReviewsByPerformance(title);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 모든 리뷰 목록 조회 (조회수 순)
     * GET /api/plaza/reviews
     */
    @GetMapping("/reviews")
    public ResponseEntity<List<ReviewListDto>> getAllReviews() {
        try {
            List<ReviewListDto> reviews = reviewPlazaService.getAllReviewsByViewCount();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 카테고리별 리뷰 목록 조회
     * GET /api/plaza/reviews/category/{category}
     */
    @GetMapping("/reviews/category/{category}")
    public ResponseEntity<List<ReviewListDto>> getReviewsByCategory(@PathVariable String category) {
        try {
            List<ReviewListDto> reviews = reviewPlazaService.getReviewsByCategory(category);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 뮤지컬 리뷰 목록 조회
     * GET /api/plaza/reviews/musicals
     */
    @GetMapping("/reviews/musicals")
    public ResponseEntity<List<ReviewListDto>> getMusicalReviews() {
        try {
            List<ReviewListDto> reviews = reviewPlazaService.getReviewsByCategory("musical");
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 콘서트 리뷰 목록 조회
     * GET /api/plaza/reviews/concerts
     */
    @GetMapping("/reviews/concerts")
    public ResponseEntity<List<ReviewListDto>> getConcertReviews() {
        try {
            List<ReviewListDto> reviews = reviewPlazaService.getReviewsByCategory("concert");
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 인기 공연의 리뷰 목록 조회
     * GET /api/plaza/reviews/popular
     */
    @GetMapping("/reviews/popular")
    public ResponseEntity<List<ReviewListDto>> getPopularPerformanceReviews() {
        try {
            List<ReviewListDto> reviews = reviewPlazaService.getPopularPerformanceReviews();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 최신 리뷰 목록 조회
     * GET /api/plaza/reviews/recent
     */
    @GetMapping("/reviews/recent")
    public ResponseEntity<List<ReviewListDto>> getRecentReviews() {
        try {
            List<ReviewListDto> reviews = reviewPlazaService.getRecentReviews();
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 