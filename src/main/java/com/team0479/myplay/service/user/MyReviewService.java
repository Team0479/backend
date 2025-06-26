package com.team0479.myplay.service.user;

import com.team0479.myplay.dto.plaza.ReviewListDto;
import com.team0479.myplay.repository.user.MyReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MyReviewService {

    private final MyReviewRepository myReviewRepository;

    public MyReviewService(MyReviewRepository myReviewRepository) {
        this.myReviewRepository = myReviewRepository;
    }

    /**
     * 내가 작성한 모든 리뷰 목록 조회 (최신순)
     */
    public List<ReviewListDto> getMyReviews(Long userId) {
        System.out.println("=== MyReviewService.getMyReviews 시작 ===");
        System.out.println("userId: " + userId);
        
        try {
            List<ReviewListDto> reviews = myReviewRepository.findMyReviews(userId);
            System.out.println("조회된 리뷰 개수: " + reviews.size());
            return reviews;
        } catch (Exception e) {
            System.out.println("마이 리뷰 조회 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("마이 리뷰 조회에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 내가 작성한 리뷰 개수 조회
     */
    public int getMyReviewCount(Long userId) {
        return myReviewRepository.countMyReviews(userId);
    }

    /**
     * 내가 작성한 평점별 리뷰 목록 조회
     */
    public List<ReviewListDto> getMyReviewsByRating(Long userId, Integer rating) {
        System.out.println("=== MyReviewService.getMyReviewsByRating 시작 ===");
        System.out.println("userId: " + userId + ", rating: " + rating);
        
        try {
            List<ReviewListDto> reviews = myReviewRepository.findMyReviewsByRating(userId, rating);
            System.out.println("조회된 리뷰 개수: " + reviews.size());
            return reviews;
        } catch (Exception e) {
            System.out.println("평점별 마이 리뷰 조회 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("평점별 마이 리뷰 조회에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 내가 작성한 카테고리별 리뷰 목록 조회
     */
    public List<ReviewListDto> getMyReviewsByCategory(Long userId, String category) {
        System.out.println("=== MyReviewService.getMyReviewsByCategory 시작 ===");
        System.out.println("userId: " + userId + ", category: " + category);
        
        try {
            List<ReviewListDto> reviews = myReviewRepository.findMyReviewsByCategory(userId, category);
            System.out.println("조회된 리뷰 개수: " + reviews.size());
            return reviews;
        } catch (Exception e) {
            System.out.println("카테고리별 마이 리뷰 조회 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("카테고리별 마이 리뷰 조회에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 내 리뷰 통계 정보 조회
     */
    public Map<String, Object> getMyReviewStatistics(Long userId) {
        System.out.println("=== MyReviewService.getMyReviewStatistics 시작 ===");
        System.out.println("userId: " + userId);
        
        try {
            // 전체 리뷰 개수
            int totalCount = myReviewRepository.countMyReviews(userId);
            
            // 평점별 개수 (1~5점)
            int rating1 = myReviewRepository.findMyReviewsByRating(userId, 1).size();
            int rating2 = myReviewRepository.findMyReviewsByRating(userId, 2).size();
            int rating3 = myReviewRepository.findMyReviewsByRating(userId, 3).size();
            int rating4 = myReviewRepository.findMyReviewsByRating(userId, 4).size();
            int rating5 = myReviewRepository.findMyReviewsByRating(userId, 5).size();
            
            Map<String, Object> statistics = Map.of(
                "totalReviews", totalCount,
                "ratingStats", Map.of(
                    "rating1", rating1,
                    "rating2", rating2,
                    "rating3", rating3,
                    "rating4", rating4,
                    "rating5", rating5
                )
            );
            
            System.out.println("통계 정보: " + statistics);
            return statistics;
            
        } catch (Exception e) {
            System.out.println("마이 리뷰 통계 조회 실패: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("마이 리뷰 통계 조회에 실패했습니다: " + e.getMessage());
        }
    }
} 