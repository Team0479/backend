package com.team0479.myplay.service.user;

import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.user.UserProfileDto;
import com.team0479.myplay.dto.user.UserExistenceDto;
import com.team0479.myplay.dto.user.InitialProfileSetupDto;
import com.team0479.myplay.repository.user.UserRepository;
import com.team0479.myplay.service.mission.MissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private MissionService missionService;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    @Lazy
    public void setMissionService(MissionService missionService) {
        this.missionService = missionService;
    }

    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return; // 이미 존재하면 저장 안 함
        }
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        
        // 신규 사용자에게 미션 할당
        try {
            // 사용자 ID를 얻기 위해 다시 조회
            Optional<User> savedUser = userRepository.findByEmail(user.getEmail());
            if (savedUser.isPresent() && missionService != null) {
                missionService.assignAllMissionsToNewUser(savedUser.get().getId());
            }
        } catch (Exception e) {
            System.out.println("신규 사용자 미션 할당 실패: " + e.getMessage());
        }
    }

    /**
     * 사용자 프로필 조회 (마이페이지용)
     */
    public UserProfileDto getUserProfile(Long userId) {
        try {
            return userRepository.getUserProfile(userId);
        } catch (Exception e) {
            throw new RuntimeException("사용자 프로필을 조회할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 이메일로 사용자 조회
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 이메일로 사용자 ID 조회
     */
    public Long getUserIdByEmail(String email) {
        return userRepository.getUserIdByEmail(email);
    }

    /**
     * 사용자 ID로 존재 여부 및 프로필 완성도 확인
     */
    public UserExistenceDto checkUserExistence(Long userId) {
        System.out.println("=== UserService.checkUserExistence 시작 ===");
        System.out.println("userId: " + userId);
        
        try {
            UserExistenceDto result = userRepository.checkUserExistence(userId);
            System.out.println("조회 결과: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("사용자 존재 확인 실패: " + e.getMessage());
            e.printStackTrace();
            return new UserExistenceDto(null, false, false, null, null);
        }
    }

    /**
     * 이메일로 사용자 존재 여부 및 프로필 완성도 확인
     */
    public UserExistenceDto checkUserExistenceByEmail(String email) {
        System.out.println("=== UserService.checkUserExistenceByEmail 시작 ===");
        System.out.println("email: " + email);
        
        try {
            UserExistenceDto result = userRepository.checkUserExistenceByEmail(email);
            System.out.println("조회 결과: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("사용자 존재 확인 실패 (이메일): " + e.getMessage());
            e.printStackTrace();
            return new UserExistenceDto(null, false, false, null, null);
        }
    }

    /**
     * 초기 프로필 설정 (닉네임, 프로필 이미지)
     */
    @Transactional
    public Map<String, Object> setupInitialProfile(Long userId, InitialProfileSetupDto setupDto) {
        System.out.println("=== UserService.setupInitialProfile 시작 ===");
        System.out.println("userId: " + userId);
        System.out.println("setupDto: " + setupDto);
        
        try {
            // 입력값 유효성 검사
            if (setupDto.getNickname() == null || setupDto.getNickname().trim().isEmpty()) {
                return Map.of("success", false, "message", "닉네임은 필수입니다.");
            }
            
            if (setupDto.getProfileImage() == null || setupDto.getProfileImage().trim().isEmpty()) {
                return Map.of("success", false, "message", "프로필 이미지는 필수입니다.");
            }
            
            // 사용자 존재 확인
            UserExistenceDto userCheck = userRepository.checkUserExistence(userId);
            if (!userCheck.isExists()) {
                return Map.of("success", false, "message", "존재하지 않는 사용자입니다.");
            }
            
            // 닉네임 중복 체크 (자신 제외)
            if (userRepository.isNicknameExistsExcludeUser(setupDto.getNickname().trim(), userId)) {
                return Map.of("success", false, "message", "이미 사용 중인 닉네임입니다.");
            }
            
            // 프로필 업데이트
            boolean success = userRepository.updateUserProfile(
                userId, 
                setupDto.getNickname().trim(), 
                setupDto.getProfileImage().trim()
            );
            
            if (success) {
                System.out.println("프로필 설정 완료");
                return Map.of(
                    "success", true, 
                    "message", "프로필이 성공적으로 설정되었습니다.",
                    "userId", userId,
                    "nickname", setupDto.getNickname().trim(),
                    "profileImage", setupDto.getProfileImage().trim()
                );
            } else {
                return Map.of("success", false, "message", "프로필 설정에 실패했습니다.");
            }
            
        } catch (Exception e) {
            System.out.println("프로필 설정 실패: " + e.getMessage());
            e.printStackTrace();
            return Map.of("success", false, "message", "프로필 설정 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 닉네임 중복 체크
     */
    public Map<String, Object> checkNicknameAvailability(String nickname) {
        System.out.println("=== UserService.checkNicknameAvailability 시작 ===");
        System.out.println("nickname: " + nickname);
        
        try {
            if (nickname == null || nickname.trim().isEmpty()) {
                return Map.of("available", false, "message", "닉네임을 입력해주세요.");
            }
            
            boolean exists = userRepository.isNicknameExists(nickname.trim());
            
            if (exists) {
                return Map.of("available", false, "message", "이미 사용 중인 닉네임입니다.");
            } else {
                return Map.of("available", true, "message", "사용 가능한 닉네임입니다.");
            }
            
        } catch (Exception e) {
            System.out.println("닉네임 중복 체크 실패: " + e.getMessage());
            e.printStackTrace();
            return Map.of("available", false, "message", "닉네임 확인 중 오류가 발생했습니다.");
        }
    }

    /**
     * 닉네임 중복 체크 (특정 사용자 제외)
     */
    public Map<String, Object> checkNicknameAvailabilityExcludeUser(String nickname, Long userId) {
        System.out.println("=== UserService.checkNicknameAvailabilityExcludeUser 시작 ===");
        System.out.println("nickname: " + nickname + ", userId: " + userId);
        
        try {
            if (nickname == null || nickname.trim().isEmpty()) {
                return Map.of("available", false, "message", "닉네임을 입력해주세요.");
            }
            
            boolean exists = userRepository.isNicknameExistsExcludeUser(nickname.trim(), userId);
            
            if (exists) {
                return Map.of("available", false, "message", "이미 사용 중인 닉네임입니다.");
            } else {
                return Map.of("available", true, "message", "사용 가능한 닉네임입니다.");
            }
            
        } catch (Exception e) {
            System.out.println("닉네임 중복 체크 실패 (사용자 제외): " + e.getMessage());
            e.printStackTrace();
            return Map.of("available", false, "message", "닉네임 확인 중 오류가 발생했습니다.");
        }
    }
}
