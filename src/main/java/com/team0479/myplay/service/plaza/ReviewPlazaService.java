package com.team0479.myplay.service.plaza;

import com.team0479.myplay.dto.plaza.ReviewListDto;
import com.team0479.myplay.repository.plaza.ReviewPlazaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ReviewPlazaService {

    private final ReviewPlazaRepository reviewPlazaRepository;

    public ReviewPlazaService(ReviewPlazaRepository reviewPlazaRepository) {
        this.reviewPlazaRepository = reviewPlazaRepository;
    }

    /**
     * 공연명으로 리뷰 검색
     * 검색 결과가 없으면 조회수 많은 순으로 전체 리뷰 반환
     */
    public List<ReviewListDto> searchReviewsByPerformance(String performanceTitle) {
        if (performanceTitle == null || performanceTitle.trim().isEmpty()) {
            // 검색어가 없으면 조회수 순으로 전체 리뷰 반환
            return reviewPlazaRepository.findAllReviewsByViewCount();
        }

        // 해당 공연의 리뷰 검색
        List<ReviewListDto> searchResults = reviewPlazaRepository.findReviewsByPerformanceTitle(performanceTitle.trim());
        
        if (searchResults.isEmpty()) {
            // 검색 결과가 없으면 조회수 순으로 전체 리뷰 반환
            return reviewPlazaRepository.findAllReviewsByViewCount();
        }
        
        return searchResults;
    }

    /**
     * 모든 리뷰 목록 조회 (조회수 순)
     */
    public List<ReviewListDto> getAllReviewsByViewCount() {
        return reviewPlazaRepository.findAllReviewsByViewCount();
    }

    /**
     * 카테고리별 리뷰 목록 조회
     */
    public List<ReviewListDto> getReviewsByCategory(String category) {
        return reviewPlazaRepository.findReviewsByCategory(category);
    }

    /**
     * 인기 공연의 리뷰 목록 조회
     */
    public List<ReviewListDto> getPopularPerformanceReviews() {
        return reviewPlazaRepository.findPopularPerformanceReviews();
    }

    /**
     * 최신 리뷰 목록 조회
     */
    public List<ReviewListDto> getRecentReviews() {
        return reviewPlazaRepository.findRecentReviews();
    }
} 