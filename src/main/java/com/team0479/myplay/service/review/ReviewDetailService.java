package com.team0479.myplay.service.review;

import com.team0479.myplay.dto.review.ReviewDetailDto;
import com.team0479.myplay.repository.review.ReviewDetailRepository;
import com.team0479.myplay.service.mission.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ReviewDetailService {

    private final ReviewDetailRepository reviewDetailRepository;
    private MissionService missionService;

    public ReviewDetailService(ReviewDetailRepository reviewDetailRepository) {
        this.reviewDetailRepository = reviewDetailRepository;
    }

    @Autowired
    @Lazy
    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    /**
     * 리뷰 상세 정보 조회 (조회수 증가)
     */
    @Transactional
    public ReviewDetailDto getReviewDetail(Long reviewId, Long currentUserId) {
        System.out.println("=== ReviewDetailService.getReviewDetail 시작 ===");
        System.out.println("reviewId: " + reviewId);
        System.out.println("currentUserId: " + currentUserId);
        
        try {
            // 리뷰 기본 정보 조회
            ReviewDetailDto reviewDetail = reviewDetailRepository.findReviewDetailById(reviewId);
            System.out.println("reviewDetail 결과: " + reviewDetail);
            
            if (reviewDetail == null) {
                System.out.println("리뷰를 찾을 수 없음: " + reviewId);
                throw new RuntimeException("Review not found with id: " + reviewId);
            }

            // 리뷰 이미지들 조회
            List<String> imageUrls = reviewDetailRepository.findReviewImageUrls(reviewId);
            reviewDetail.setReviewImageUrls(imageUrls);

            // 현재 사용자의 좋아요 여부 확인
            if (currentUserId != null) {
                boolean isLiked = reviewDetailRepository.isLikedByUser(reviewId, currentUserId);
                reviewDetail.setLikedByCurrentUser(isLiked);
            }

            // 조회수 증가 (별도 트랜잭션)
            incrementViewCountAsync(reviewId);

            return reviewDetail;
        } catch (Exception e) {
            System.out.println("예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 조회수 증가 (비동기적으로 처리)
     */
    @Transactional
    public void incrementViewCountAsync(Long reviewId) {
        reviewDetailRepository.incrementViewCount(reviewId);
    }

    /**
     * 리뷰 좋아요 토글 (좋아요/좋아요 취소)
     */
    public Map<String, Object> toggleLike(Long reviewId, Long userId) {
        try {
            System.out.println("=== toggleLike 시작 ===");
            System.out.println("reviewId: " + reviewId);
            System.out.println("userId: " + userId);
            
            boolean isCurrentlyLiked = reviewDetailRepository.isLikedByUser(reviewId, userId);
            System.out.println("현재 좋아요 상태: " + isCurrentlyLiked);
            
            if (isCurrentlyLiked) {
                // 좋아요 취소
                System.out.println("좋아요 취소 실행");
                reviewDetailRepository.removeLike(reviewId, userId);
                return Map.of(
                    "liked", false,
                    "message", "좋아요가 취소되었습니다."
                );
            } else {
                // 좋아요 추가
                System.out.println("좋아요 추가 실행");
                reviewDetailRepository.addLike(reviewId, userId);
                
                // 좋아요를 누른 사용자에게 경험치 증가 (5 경험치)
                System.out.println("경험치 증가 실행");
                updateUserExperienceAndLevel(userId, 5);
                
                // 미션 진행도 업데이트 (리뷰 좋아요)
                try {
                    if (missionService != null) {
                        missionService.updateMissionProgress(userId, "REVIEW_LIKE", 1);
                        System.out.println("리뷰 좋아요 미션 진행도 업데이트 완료");
                    }
                } catch (Exception e) {
                    System.out.println("리뷰 좋아요 미션 진행도 업데이트 실패: " + e.getMessage());
                }
                
                return Map.of(
                    "liked", true,
                    "message", "좋아요가 추가되었습니다.",
                    "expGained", 5
                );
            }
        } catch (Exception e) {
            System.out.println("toggleLike 예외 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 사용자 경험치 및 레벨 업데이트
     */
    private void updateUserExperienceAndLevel(Long userId, int expGain) {
        // 현재 경험치와 레벨 조회
        int currentExp = reviewDetailRepository.getUserCurrentExp(userId);
        int currentLevel = reviewDetailRepository.getUserCurrentLevel(userId);
        
        // 경험치 증가
        reviewDetailRepository.updateUserExperience(userId, expGain);
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
            int nextLevelRequiredExp = reviewDetailRepository.getRequiredExpForLevel(currentLevel + 1);
            
            // 레벨 업 조건 체크
            if (nextLevelRequiredExp > 0 && currentExp >= nextLevelRequiredExp) {
                // 레벨 업!
                reviewDetailRepository.updateUserLevel(userId, currentLevel + 1);
                
                // 추가 레벨 업이 가능한지 재귀 체크
                checkAndUpdateLevel(userId, currentExp, currentLevel + 1);
            }
        } catch (Exception e) {
            // level_requirement 테이블에 다음 레벨 데이터가 없는 경우 (최고 레벨 도달)
            // 레벨 업 중단
        }
    }
} 