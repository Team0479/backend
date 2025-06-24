package com.team0479.myplay.controller.home;

import com.team0479.myplay.dto.home.BestReviewDto;
import com.team0479.myplay.service.home.BestReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/best-reviews")
@CrossOrigin(origins = "*")
public class BestReviewController {

    private final BestReviewService bestReviewService;

    public BestReviewController(BestReviewService bestReviewService) {
        this.bestReviewService = bestReviewService;
    }

    /**
     * 전체 베스트 리뷰 조회 API
     * 좋아요 수를 기준으로 내림차순 정렬하여 반환합니다.
     * 
     * @return 베스트 리뷰 목록
     */
    @GetMapping
    public ResponseEntity<List<BestReviewDto>> getBestReviews() {
        List<BestReviewDto> bestReviews = bestReviewService.getBestReviews();
        return ResponseEntity.ok(bestReviews);
    }

    /**
     * 특정 장르의 베스트 리뷰 조회 API
     * 좋아요 수를 기준으로 내림차순 정렬하여 반환합니다.
     * 
     * @param category 장르 (뮤지컬, 콘서트, 스포츠, 전시/행사)
     * @return 해당 장르의 베스트 리뷰 목록
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<BestReviewDto>> getBestReviewsByCategory(
            @PathVariable String category) {
        
        List<BestReviewDto> bestReviews = bestReviewService.getBestReviewsByCategory(category);
        return ResponseEntity.ok(bestReviews);
    }

    /**
     * 뮤지컬 베스트 리뷰 조회 API
     * 
     * @return 뮤지컬 베스트 리뷰 목록
     */
    @GetMapping("/musicals")
    public ResponseEntity<List<BestReviewDto>> getBestMusicalReviews() {
        List<BestReviewDto> musicalReviews = bestReviewService.getBestMusicalReviews();
        return ResponseEntity.ok(musicalReviews);
    }

    /**
     * 콘서트 베스트 리뷰 조회 API
     * 
     * @return 콘서트 베스트 리뷰 목록
     */
    @GetMapping("/concerts")
    public ResponseEntity<List<BestReviewDto>> getBestConcertReviews() {
        List<BestReviewDto> concertReviews = bestReviewService.getBestConcertReviews();
        return ResponseEntity.ok(concertReviews);
    }

    /**
     * 스포츠 베스트 리뷰 조회 API
     * 
     * @return 스포츠 베스트 리뷰 목록
     */
    @GetMapping("/sports")
    public ResponseEntity<List<BestReviewDto>> getBestSportsReviews() {
        List<BestReviewDto> sportsReviews = bestReviewService.getBestSportsReviews();
        return ResponseEntity.ok(sportsReviews);
    }

    /**
     * 전시/행사 베스트 리뷰 조회 API
     * 
     * @return 전시/행사 베스트 리뷰 목록
     */
    @GetMapping("/exhibitions")
    public ResponseEntity<List<BestReviewDto>> getBestExhibitionReviews() {
        List<BestReviewDto> exhibitionReviews = bestReviewService.getBestExhibitionReviews();
        return ResponseEntity.ok(exhibitionReviews);
    }
} 