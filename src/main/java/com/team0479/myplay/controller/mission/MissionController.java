package com.team0479.myplay.controller.mission;

import com.team0479.myplay.config.jwt.JwtProvider;
import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.mission.UserMissionDto;
import com.team0479.myplay.service.mission.MissionService;
import com.team0479.myplay.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/missions")
@CrossOrigin(origins = "*")
public class MissionController {

    private final MissionService missionService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public MissionController(MissionService missionService, UserService userService, JwtProvider jwtProvider) {
        this.missionService = missionService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 현재 로그인한 사용자의 미션 목록 조회
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserMissions(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Authorization 헤더 검증
            if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "유효하지 않은 토큰입니다."));
            }

            String jwtToken = token.replace("Bearer ", "");
            
            if (!jwtProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "토큰이 유효하지 않습니다."));
            }

            // 토큰에서 사용자 정보 추출
            String email = jwtProvider.getUserEmailFromToken(jwtToken);
            Optional<User> userOptional = userService.findByEmail(email);
            
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            User user = userOptional.get();
            List<UserMissionDto> missions = missionService.getUserMissions(user.getId());
            
            return ResponseEntity.ok(Map.of(
                "missions", missions,
                "totalMissions", missions.size(),
                "completedMissions", missions.stream().filter(UserMissionDto::getIsCompleted).count()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 특정 사용자의 미션 목록 조회 (관리자용)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMissions(@PathVariable Long userId) {
        try {
            List<UserMissionDto> missions = missionService.getUserMissions(userId);
            
            return ResponseEntity.ok(Map.of(
                "missions", missions,
                "totalMissions", missions.size(),
                "completedMissions", missions.stream().filter(UserMissionDto::getIsCompleted).count()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "미션 목록을 조회할 수 없습니다: " + e.getMessage()));
        }
    }

    /**
     * 사용자에게 미션 할당 (신규 회원가입 시 사용)
     */
    @PostMapping("/assign/{userId}")
    public ResponseEntity<?> assignMissionsToUser(@PathVariable Long userId) {
        try {
            missionService.assignAllMissionsToNewUser(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "미션이 성공적으로 할당되었습니다."
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "미션 할당에 실패했습니다: " + e.getMessage()));
        }
    }

    /**
     * 미션 진행도 수동 업데이트 (테스트용)
     */
    @PostMapping("/progress")
    public ResponseEntity<?> updateMissionProgress(
            @RequestBody Map<String, Object> request,
            @RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Authorization 헤더 검증
            if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "유효하지 않은 토큰입니다."));
            }

            String jwtToken = token.replace("Bearer ", "");
            
            if (!jwtProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "토큰이 유효하지 않습니다."));
            }

            // 토큰에서 사용자 정보 추출
            String email = jwtProvider.getUserEmailFromToken(jwtToken);
            Optional<User> userOptional = userService.findByEmail(email);
            
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            String targetAction = (String) request.get("targetAction");
            Integer incrementValue = (Integer) request.getOrDefault("incrementValue", 1);

            User user = userOptional.get();
            missionService.updateMissionProgress(user.getId(), targetAction, incrementValue);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "미션 진행도가 업데이트되었습니다."
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "미션 진행도 업데이트에 실패했습니다: " + e.getMessage()));
        }
    }
} 