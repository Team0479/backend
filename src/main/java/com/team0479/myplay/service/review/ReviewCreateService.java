package com.team0479.myplay.service.review;

import com.team0479.myplay.dto.review.ReviewCreateDto;
import com.team0479.myplay.repository.review.ReviewCreateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ReviewCreateService {

    private final ReviewCreateRepository reviewCreateRepository;

    public ReviewCreateService(ReviewCreateRepository reviewCreateRepository) {
        this.reviewCreateRepository = reviewCreateRepository;
    }

    /**
     * 리뷰 등록
     */
    @Transactional
    public Map<String, Object> createReview(ReviewCreateDto reviewCreateDto) {
        try {
            System.out.println("=== 리뷰 등록 시작 ===");
            System.out.println("제목: " + reviewCreateDto.getTitle());
            System.out.println("내용: " + reviewCreateDto.getContent());
            System.out.println("평점: " + reviewCreateDto.getRating());
            System.out.println("공연ID: " + reviewCreateDto.getPerformanceId());
            System.out.println("사용자ID: " + reviewCreateDto.getUserId());

            // 1. 리뷰 등록
            Long reviewId = reviewCreateRepository.createReview(
                reviewCreateDto.getTitle(),
                reviewCreateDto.getContent(),
                reviewCreateDto.getRating(),
                reviewCreateDto.getPerformanceId(),
                reviewCreateDto.getUserId()
            );
            System.out.println("생성된 리뷰 ID: " + reviewId);

            // 2. 리뷰 이미지 등록 (이미지가 있는 경우)
            List<String> imageUrls = reviewCreateDto.getImageUrls();
            if (imageUrls != null && !imageUrls.isEmpty()) {
                for (String imageUrl : imageUrls) {
                    reviewCreateRepository.createReviewImage(reviewId, imageUrl);
                }
                System.out.println("이미지 " + imageUrls.size() + "개 등록 완료");
            }

            // 3. 경험치 증가 (리뷰 작성 시 10 경험치)
            updateUserExperienceAndLevel(reviewCreateDto.getUserId(), 10);
            System.out.println("경험치 10점 추가 완료");

            return Map.of(
                "success", true,
                "reviewId", reviewId,
                "message", "리뷰가 성공적으로 등록되었습니다.",
                "expGained", 10
            );

        } catch (Exception e) {
            System.out.println("리뷰 등록 예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("리뷰 등록에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자 경험치 및 레벨 업데이트
     */
    private void updateUserExperienceAndLevel(Long userId, int expGain) {
        // 현재 경험치와 레벨 조회
        int currentExp = reviewCreateRepository.getUserCurrentExp(userId);
        int currentLevel = reviewCreateRepository.getUserCurrentLevel(userId);
        
        // 경험치 증가
        reviewCreateRepository.updateUserExperience(userId, expGain);
        int newExp = currentExp + expGain;
        
        // 레벨 업 체크
        checkAndUpdateLevel(userId, newExp, currentLevel);
    }

    /**
     * 레벨 업 체크 및 업데이트
     */
    private void checkAndUpdateLevel(Long userId, int currentExp, int currentLevel) {
        try {
            // 다음 레벨에 필요한 경험치 조회
            int nextLevelRequiredExp = reviewCreateRepository.getRequiredExpForLevel(currentLevel + 1);
            
            // 레벨 업 조건 체크
            if (nextLevelRequiredExp > 0 && currentExp >= nextLevelRequiredExp) {
                // 레벨 업!
                reviewCreateRepository.updateUserLevel(userId, currentLevel + 1);
                System.out.println("레벨 업! " + currentLevel + " -> " + (currentLevel + 1));
                
                // 추가 레벨 업이 가능한지 재귀 체크
                checkAndUpdateLevel(userId, currentExp, currentLevel + 1);
            }
        } catch (Exception e) {
            // level_requirement 테이블에 다음 레벨 데이터가 없는 경우 (최고 레벨 도달)
            System.out.println("최고 레벨 도달 또는 레벨업 체크 오류: " + e.getMessage());
        }
    }
} 