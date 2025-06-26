package com.team0479.myplay.controller.badge;

import com.team0479.myplay.config.jwt.JwtProvider;
import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.badge.UserBadgeDto;
import com.team0479.myplay.service.badge.BadgeService;
import com.team0479.myplay.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/badges")
@CrossOrigin(origins = "*")
public class BadgeController {

    private final BadgeService badgeService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public BadgeController(BadgeService badgeService, UserService userService, JwtProvider jwtProvider) {
        this.badgeService = badgeService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 현재 로그인한 사용자의 모든 칭호 목록 조회 (획득 여부 포함)
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUserBadges(@RequestHeader(value = "Authorization", required = false) String token) {
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
            List<UserBadgeDto> badges = badgeService.getUserBadges(user.getId());
            BadgeService.BadgeStatsDto stats = badgeService.getUserBadgeStats(user.getId());
            
            return ResponseEntity.ok(Map.of(
                "badges", badges,
                "stats", Map.of(
                    "acquiredCount", stats.getAcquiredCount(),
                    "totalCount", stats.getTotalCount(),
                    "completionRate", Math.round(stats.getCompletionRate() * 100.0) / 100.0
                )
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 현재 로그인한 사용자가 획득한 칭호만 조회
     */
    @GetMapping("/me/acquired")
    public ResponseEntity<?> getCurrentUserAcquiredBadges(@RequestHeader(value = "Authorization", required = false) String token) {
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
            List<UserBadgeDto> acquiredBadges = badgeService.getUserAcquiredBadges(user.getId());
            
            return ResponseEntity.ok(Map.of(
                "acquiredBadges", acquiredBadges,
                "count", acquiredBadges.size()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류가 발생했습니다: " + e.getMessage()));
        }
    }

    /**
     * 특정 사용자의 칭호 목록 조회 (테스트용 - 토큰 없이)
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserBadges(@PathVariable Long userId) {
        try {
            List<UserBadgeDto> badges = badgeService.getUserBadges(userId);
            BadgeService.BadgeStatsDto stats = badgeService.getUserBadgeStats(userId);
            
            return ResponseEntity.ok(Map.of(
                "badges", badges,
                "stats", Map.of(
                    "acquiredCount", stats.getAcquiredCount(),
                    "totalCount", stats.getTotalCount(),
                    "completionRate", Math.round(stats.getCompletionRate() * 100.0) / 100.0
                )
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "칭호 목록을 조회할 수 없습니다: " + e.getMessage()));
        }
    }

    /**
     * 특정 사용자가 획득한 칭호만 조회 (테스트용 - 토큰 없이)
     */
    @GetMapping("/user/{userId}/acquired")
    public ResponseEntity<?> getUserAcquiredBadges(@PathVariable Long userId) {
        try {
            List<UserBadgeDto> acquiredBadges = badgeService.getUserAcquiredBadges(userId);
            
            return ResponseEntity.ok(Map.of(
                "acquiredBadges", acquiredBadges,
                "count", acquiredBadges.size()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "획득한 칭호 목록을 조회할 수 없습니다: " + e.getMessage()));
        }
    }

    /**
     * 사용자에게 칭호 부여 (관리자용)
     */
    @PostMapping("/award")
    public ResponseEntity<?> awardBadge(@RequestBody Map<String, Object> request) {
        try {
            Long userId = Long.valueOf(request.get("userId").toString());
            Long badgeId = Long.valueOf(request.get("badgeId").toString());
            
            badgeService.awardBadgeToUser(userId, badgeId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "칭호가 성공적으로 부여되었습니다."
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "칭호 부여에 실패했습니다: " + e.getMessage()));
        }
    }

    /**
     * 특정 사용자의 모든 칭호 조건 체크 (테스트/관리자용)
     */
    @PostMapping("/check/{userId}")
    public ResponseEntity<?> checkAllBadges(@PathVariable Long userId) {
        try {
            badgeService.checkAllBadgesForUser(userId);
            badgeService.checkBestPlayerRankingBadges();
            badgeService.checkBestReviewRankingBadges();
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "사용자 " + userId + "의 모든 칭호 조건을 체크했습니다."
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "칭호 조건 체크에 실패했습니다: " + e.getMessage()));
        }
    }

    /**
     * 댓글 작성 후 칭호 체크 (댓글 시스템에서 호출)
     */
    @PostMapping("/check-comment/{userId}")
    public ResponseEntity<?> checkCommentBadges(@PathVariable Long userId) {
        try {
            badgeService.checkCommentBadges(userId);
            
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "댓글 관련 칭호 조건을 체크했습니다."
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "댓글 칭호 조건 체크에 실패했습니다: " + e.getMessage()));
        }
    }
} 