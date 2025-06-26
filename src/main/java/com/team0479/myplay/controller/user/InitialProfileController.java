package com.team0479.myplay.controller.user;

import com.team0479.myplay.config.jwt.JwtProvider;
import com.team0479.myplay.dto.user.UserExistenceDto;
import com.team0479.myplay.dto.user.InitialProfileSetupDto;
import com.team0479.myplay.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users/profile")
@CrossOrigin(origins = "*")
public class InitialProfileController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public InitialProfileController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 현재 사용자 존재 여부 및 프로필 완성도 확인 (JWT 토큰)
     * GET /api/users/profile/check
     */
    @GetMapping("/check")
    public ResponseEntity<UserExistenceDto> checkCurrentUserExistence(@RequestHeader("Authorization") String authHeader) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            UserExistenceDto result = userService.checkUserExistenceByEmail(userEmail);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.out.println("사용자 존재 확인 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 사용자 ID로 존재 여부 및 프로필 완성도 확인 (테스트용)
     * GET /api/users/profile/check/{userId}
     */
    @GetMapping("/check/{userId}")
    public ResponseEntity<UserExistenceDto> checkUserExistence(@PathVariable Long userId) {
        try {
            UserExistenceDto result = userService.checkUserExistence(userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("사용자 존재 확인 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 현재 사용자 초기 프로필 설정 (JWT 토큰)
     * POST /api/users/profile/setup
     */
    @PostMapping("/setup")
    public ResponseEntity<Map<String, Object>> setupCurrentUserProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody InitialProfileSetupDto setupDto) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "유효하지 않은 토큰입니다."));
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            Long userId = userService.getUserIdByEmail(userEmail);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("success", false, "message", "사용자를 찾을 수 없습니다."));
            }

            Map<String, Object> result = userService.setupInitialProfile(userId, setupDto);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

        } catch (Exception e) {
            System.out.println("프로필 설정 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "프로필 설정 중 오류가 발생했습니다."));
        }
    }

    /**
     * 특정 사용자 초기 프로필 설정 (테스트용)
     * POST /api/users/profile/setup/{userId}
     */
    @PostMapping("/setup/{userId}")
    public ResponseEntity<Map<String, Object>> setupUserProfile(
            @PathVariable Long userId,
            @RequestBody InitialProfileSetupDto setupDto) {
        try {
            Map<String, Object> result = userService.setupInitialProfile(userId, setupDto);
            
            if ((Boolean) result.get("success")) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }

        } catch (Exception e) {
            System.out.println("프로필 설정 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "message", "프로필 설정 중 오류가 발생했습니다."));
        }
    }

    /**
     * 닉네임 중복 체크
     * GET /api/users/profile/nickname/check?nickname={nickname}
     */
    @GetMapping("/nickname/check")
    public ResponseEntity<Map<String, Object>> checkNicknameAvailability(@RequestParam String nickname) {
        try {
            Map<String, Object> result = userService.checkNicknameAvailability(nickname);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("닉네임 중복 체크 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("available", false, "message", "닉네임 확인 중 오류가 발생했습니다."));
        }
    }

    /**
     * 닉네임 중복 체크 (현재 사용자 제외, JWT 토큰)
     * GET /api/users/profile/nickname/check-exclude-me?nickname={nickname}
     */
    @GetMapping("/nickname/check-exclude-me")
    public ResponseEntity<Map<String, Object>> checkNicknameAvailabilityExcludeCurrentUser(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String nickname) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("available", false, "message", "유효하지 않은 토큰입니다."));
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            Long userId = userService.getUserIdByEmail(userEmail);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("available", false, "message", "사용자를 찾을 수 없습니다."));
            }

            Map<String, Object> result = userService.checkNicknameAvailabilityExcludeUser(nickname, userId);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            System.out.println("닉네임 중복 체크 실패 (사용자 제외): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("available", false, "message", "닉네임 확인 중 오류가 발생했습니다."));
        }
    }

    /**
     * 닉네임 중복 체크 (특정 사용자 제외, 테스트용)
     * GET /api/users/profile/nickname/check-exclude/{userId}?nickname={nickname}
     */
    @GetMapping("/nickname/check-exclude/{userId}")
    public ResponseEntity<Map<String, Object>> checkNicknameAvailabilityExcludeUser(
            @PathVariable Long userId,
            @RequestParam String nickname) {
        try {
            Map<String, Object> result = userService.checkNicknameAvailabilityExcludeUser(nickname, userId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println("닉네임 중복 체크 실패 (사용자 제외): " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("available", false, "message", "닉네임 확인 중 오류가 발생했습니다."));
        }
    }
} 