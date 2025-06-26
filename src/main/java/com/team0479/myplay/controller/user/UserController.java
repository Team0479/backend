package com.team0479.myplay.controller.user;

import com.team0479.myplay.config.jwt.JwtProvider;
import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.user.UserProfileDto;
import com.team0479.myplay.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    public UserController(UserService userService, JwtProvider jwtProvider) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.register(user);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("회원가입이 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    /**
     * 사용자 프로필 조회 (마이페이지용)
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long userId) {
        try {
            UserProfileDto userProfile = userService.getUserProfile(userId);
            return ResponseEntity.ok(userProfile);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    /**
     * 현재 로그인한 사용자의 프로필 조회 (JWT 토큰 기반)
     */
    @GetMapping("/me/profile")
    public ResponseEntity<?> getCurrentUserProfile(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            // Authorization 헤더가 없는 경우
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Authorization 헤더가 필요합니다."));
            }
            
            // Bearer 토큰에서 실제 토큰 추출
            if (!token.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Bearer 토큰 형식이 올바르지 않습니다."));
            }
            
            String jwtToken = token.replace("Bearer ", "");
            
            // 토큰 유효성 검증
            if (!jwtProvider.validateToken(jwtToken)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "유효하지 않은 토큰입니다."));
            }
            
            // 토큰에서 이메일 추출
            String email = jwtProvider.getUserEmailFromToken(jwtToken);
            
            // 이메일로 사용자 조회
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }
            
            // 사용자 프로필 조회
            User user = userOptional.get();
            UserProfileDto userProfile = userService.getUserProfile(user.getId());
            return ResponseEntity.ok(userProfile);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "서버 오류가 발생했습니다: " + e.getMessage()));
        }
    }
}
