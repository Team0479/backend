package com.team0479.myplay.service.mission;

import com.team0479.myplay.dto.mission.MissionDto;
import com.team0479.myplay.dto.mission.UserMissionDto;
import com.team0479.myplay.repository.mission.MissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;

    public MissionService(MissionRepository missionRepository) {
        this.missionRepository = missionRepository;
    }

    /**
     * 사용자의 미션 목록 조회
     */
    public List<UserMissionDto> getUserMissions(Long userId) {
        try {
            return missionRepository.getUserMissions(userId);
        } catch (Exception e) {
            throw new RuntimeException("사용자의 미션 목록을 조회할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 새로운 사용자에게 모든 활성 미션 할당
     */
    public void assignAllMissionsToNewUser(Long userId) {
        try {
            List<MissionDto> activeMissions = missionRepository.getAllActiveMissions();
            
            for (MissionDto mission : activeMissions) {
                // 이미 할당된 미션이 아닌 경우에만 할당
                if (!missionRepository.isUserMissionExists(userId, mission.getId())) {
                    missionRepository.assignMissionToUser(userId, mission.getId());
                }
            }
            
            System.out.println("사용자 " + userId + "에게 " + activeMissions.size() + "개의 미션을 할당했습니다.");
        } catch (Exception e) {
            throw new RuntimeException("미션 할당에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 미션 진행도 업데이트 (특정 액션 수행 시 호출)
     */
    public void updateMissionProgress(Long userId, String targetAction) {
        updateMissionProgress(userId, targetAction, 1);
    }

    /**
     * 미션 진행도 업데이트 (증가값 지정)
     */
    public void updateMissionProgress(Long userId, String targetAction, int incrementValue) {
        try {
            System.out.println("=== 미션 진행도 업데이트 ===");
            System.out.println("사용자 ID: " + userId);
            System.out.println("대상 액션: " + targetAction);
            System.out.println("증가값: " + incrementValue);
            
            missionRepository.updateMissionProgress(userId, targetAction, incrementValue);
            System.out.println("미션 진행도 업데이트 완료");
        } catch (Exception e) {
            System.out.println("미션 진행도 업데이트 실패: " + e.getMessage());
            throw new RuntimeException("미션 진행도 업데이트에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 캘린더 일정 등록 시 미션 진행도 업데이트
     */
    public void onCalendarEntryCreated(Long userId) {
        updateMissionProgress(userId, "CALENDAR_ENTRY", 1);
    }

    /**
     * 리뷰 작성 시 미션 진행도 업데이트
     */
    public void onReviewCreated(Long userId) {
        updateMissionProgress(userId, "REVIEW_WRITE", 1);
    }

    /**
     * 다른 플레이어 리뷰에 좋아요 누를 시 미션 진행도 업데이트
     */
    public void onReviewLiked(Long userId) {
        updateMissionProgress(userId, "REVIEW_LIKE", 1);
    }

    /**
     * 월별 미션 진행도 초기화 (스케줄러에서 호출)
     */
    public void resetMonthlyMissions() {
        try {
            missionRepository.resetMonthlyMissions();
            System.out.println("월별 미션 진행도가 초기화되었습니다.");
        } catch (Exception e) {
            throw new RuntimeException("월별 미션 초기화에 실패했습니다: " + e.getMessage());
        }
    }

    /**
     * 완료된 미션 개수 조회
     */
    public int getCompletedMissionCount(Long userId) {
        try {
            List<UserMissionDto> missions = getUserMissions(userId);
            return (int) missions.stream().filter(UserMissionDto::getIsCompleted).count();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 전체 미션 개수 조회
     */
    public int getTotalMissionCount(Long userId) {
        try {
            return getUserMissions(userId).size();
        } catch (Exception e) {
            return 0;
        }
    }
} 