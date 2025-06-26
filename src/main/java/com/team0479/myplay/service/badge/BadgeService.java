package com.team0479.myplay.service.badge;

import com.team0479.myplay.dto.badge.UserBadgeDto;
import com.team0479.myplay.repository.badge.BadgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BadgeService {

    private final BadgeRepository badgeRepository;

    public BadgeService(BadgeRepository badgeRepository) {
        this.badgeRepository = badgeRepository;
    }

    /**
     * 사용자의 모든 칭호 목록 조회 (획득 여부 포함)
     */
    @Transactional(readOnly = true)
    public List<UserBadgeDto> getUserBadges(Long userId) {
        try {
            return badgeRepository.getUserBadges(userId);
        } catch (Exception e) {
            throw new RuntimeException("사용자의 칭호 목록을 조회할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자가 획득한 칭호만 조회
     */
    @Transactional(readOnly = true)
    public List<UserBadgeDto> getUserAcquiredBadges(Long userId) {
        try {
            return badgeRepository.getUserAcquiredBadges(userId);
        } catch (Exception e) {
            throw new RuntimeException("사용자가 획득한 칭호 목록을 조회할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자에게 칭호 부여
     */
    public void awardBadgeToUser(Long userId, Long badgeId) {
        try {
            badgeRepository.awardBadgeToUser(userId, badgeId);
            System.out.println("사용자 " + userId + "에게 칭호 " + badgeId + "를 부여했습니다.");
        } catch (Exception e) {
            throw new RuntimeException("칭호 부여에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 사용자 칭호 획득 통계 조회
     */
    @Transactional(readOnly = true)
    public BadgeStatsDto getUserBadgeStats(Long userId) {
        try {
            int acquiredCount = badgeRepository.getUserBadgeCount(userId);
            int totalCount = badgeRepository.getTotalBadgeCount();
            
            return new BadgeStatsDto(acquiredCount, totalCount);
        } catch (Exception e) {
            throw new RuntimeException("칭호 통계를 조회할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 캘린더 일정 등록 시 칭호 체크
     */
    public void checkCalendarBadges(Long userId) {
        try {
            // 문화생활 아티스트 - 캘린더 전체 일정 등록 수 30개 달성 시 획득
            int calendarCount = badgeRepository.getUserCalendarCount(userId);
            if (calendarCount >= 30) {
                awardBadgeToUser(userId, 2L); // badgeId = 2: 문화생활 아티스트
                System.out.println("사용자 " + userId + "가 '문화생활 아티스트' 칭호를 획득했습니다!");
            }
        } catch (Exception e) {
            System.out.println("캘린더 칭호 체크 실패: " + e.getMessage());
        }
    }

    /**
     * 리뷰 작성 시 칭호 체크
     */
    public void checkReviewBadges(Long userId) {
        try {
            // 문화 비평의 장인 - 전체 리뷰 등록 수 30개 달성 시 획득
            int reviewCount = badgeRepository.getUserReviewCount(userId);
            if (reviewCount >= 30) {
                awardBadgeToUser(userId, 3L); // badgeId = 3: 문화 비평의 장인
                System.out.println("사용자 " + userId + "가 '문화 비평의 장인' 칭호를 획득했습니다!");
            }
        } catch (Exception e) {
            System.out.println("리뷰 칭호 체크 실패: " + e.getMessage());
        }
    }

    /**
     * 댓글 작성 시 칭호 체크
     */
    public void checkCommentBadges(Long userId) {
        try {
            // 헤비 토커로 인정합니다 - 플레이어 광장에서 작성한 댓글 수 30개 달성 시 획득
            int commentCount = badgeRepository.getUserCommentCount(userId);
            if (commentCount >= 30) {
                awardBadgeToUser(userId, 5L); // badgeId = 5: 헤비 토커로 인정합니다
                System.out.println("사용자 " + userId + "가 '헤비 토커로 인정합니다' 칭호를 획득했습니다!");
            }
        } catch (Exception e) {
            System.out.println("댓글 칭호 체크 실패: " + e.getMessage());
        }
    }

    /**
     * 베스트 플레이어 랭킹 변경 시 칭호 체크
     */
    public void checkBestPlayerRankingBadges() {
        try {
            // 레전더리 플레이어 - 베스트 플레이어 랭킹 1위 최초 1회 달성 시 획득
            Long topPlayerId = badgeRepository.getCurrentBestPlayer();
            if (topPlayerId != null) {
                // 이미 레전더리 플레이어 칭호를 가지고 있는지 확인
                if (!badgeRepository.isUserBadgeExists(topPlayerId, 1L)) {
                    awardBadgeToUser(topPlayerId, 1L); // badgeId = 1: 레전더리 플레이어
                    System.out.println("사용자 " + topPlayerId + "가 '레전더리 플레이어' 칭호를 획득했습니다!");
                }
            }
        } catch (Exception e) {
            System.out.println("베스트 플레이어 칭호 체크 실패: " + e.getMessage());
        }
    }

    /**
     * 베스트 리뷰 랭킹 변경 시 칭호 체크
     */
    public void checkBestReviewRankingBadges() {
        try {
            // 내가 바로 평론가 - 베스트 리뷰 랭킹(상위 10위) 최초 1회 입성 시 획득
            List<Long> top10ReviewUsers = badgeRepository.getTop10ReviewUsers();
            for (Long userId : top10ReviewUsers) {
                // 이미 내가 바로 평론가 칭호를 가지고 있는지 확인
                if (!badgeRepository.isUserBadgeExists(userId, 4L)) {
                    awardBadgeToUser(userId, 4L); // badgeId = 4: 내가 바로 평론가
                    System.out.println("사용자 " + userId + "가 '내가 바로 평론가' 칭호를 획득했습니다!");
                }
            }
        } catch (Exception e) {
            System.out.println("베스트 리뷰 칭호 체크 실패: " + e.getMessage());
        }
    }

    /**
     * 모든 칭호 조건 일괄 체크 (정기적으로 실행)
     */
    public void checkAllBadgesForUser(Long userId) {
        try {
            checkCalendarBadges(userId);
            checkReviewBadges(userId);
            checkCommentBadges(userId);
            System.out.println("사용자 " + userId + "의 모든 칭호 조건을 체크했습니다.");
        } catch (Exception e) {
            System.out.println("칭호 일괄 체크 실패: " + e.getMessage());
        }
    }

    /**
     * 칭호 통계 DTO
     */
    public static class BadgeStatsDto {
        private int acquiredCount;
        private int totalCount;
        private double completionRate;

        public BadgeStatsDto(int acquiredCount, int totalCount) {
            this.acquiredCount = acquiredCount;
            this.totalCount = totalCount;
            this.completionRate = totalCount > 0 ? (double) acquiredCount / totalCount * 100 : 0.0;
        }

        public int getAcquiredCount() {
            return acquiredCount;
        }

        public int getTotalCount() {
            return totalCount;
        }

        public double getCompletionRate() {
            return completionRate;
        }
    }
} 