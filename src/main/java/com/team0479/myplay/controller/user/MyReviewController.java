package com.team0479.myplay.controller.user;

import com.team0479.myplay.config.jwt.JwtProvider;
import com.team0479.myplay.dto.plaza.ReviewListDto;
import com.team0479.myplay.service.user.MyReviewService;
import com.team0479.myplay.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews/my")
@CrossOrigin(origins = "*")
public class MyReviewController {

    private final MyReviewService myReviewService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    public MyReviewController(MyReviewService myReviewService, UserService userService, JwtProvider jwtProvider) {
        this.myReviewService = myReviewService;
        this.userService = userService;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 현재 사용자의 모든 리뷰 목록 조회 (JWT 토큰 필요)
     * GET /api/reviews/my
     */
    @GetMapping
    public ResponseEntity<List<ReviewListDto>> getMyReviews(@RequestHeader("Authorization") String authHeader) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            Long userId = userService.getUserIdByEmail(userEmail);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<ReviewListDto> myReviews = myReviewService.getMyReviews(userId);
            return ResponseEntity.ok(myReviews);

        } catch (Exception e) {
            System.out.println("마이 리뷰 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 특정 사용자의 리뷰 목록 조회 (테스트용)
     * GET /api/reviews/my/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewListDto>> getUserReviews(@PathVariable Long userId) {
        try {
            List<ReviewListDto> userReviews = myReviewService.getMyReviews(userId);
            return ResponseEntity.ok(userReviews);
        } catch (Exception e) {
            System.out.println("사용자 리뷰 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 현재 사용자의 평점별 리뷰 목록 조회
     * GET /api/reviews/my/rating/{rating}
     */
    @GetMapping("/rating/{rating}")
    public ResponseEntity<List<ReviewListDto>> getMyReviewsByRating(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Integer rating) {
        try {
            // 평점 유효성 검사
            if (rating < 1 || rating > 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            Long userId = userService.getUserIdByEmail(userEmail);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<ReviewListDto> myReviews = myReviewService.getMyReviewsByRating(userId, rating);
            return ResponseEntity.ok(myReviews);

        } catch (Exception e) {
            System.out.println("평점별 마이 리뷰 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 현재 사용자의 카테고리별 리뷰 목록 조회
     * GET /api/reviews/my/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReviewListDto>> getMyReviewsByCategory(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String category) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            Long userId = userService.getUserIdByEmail(userEmail);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<ReviewListDto> myReviews = myReviewService.getMyReviewsByCategory(userId, category);
            return ResponseEntity.ok(myReviews);

        } catch (Exception e) {
            System.out.println("카테고리별 마이 리뷰 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 현재 사용자의 리뷰 통계 정보 조회
     * GET /api/reviews/my/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getMyReviewStatistics(@RequestHeader("Authorization") String authHeader) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            Long userId = userService.getUserIdByEmail(userEmail);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            Map<String, Object> statistics = myReviewService.getMyReviewStatistics(userId);
            return ResponseEntity.ok(statistics);

        } catch (Exception e) {
            System.out.println("마이 리뷰 통계 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 현재 사용자의 리뷰 개수 조회
     * GET /api/reviews/my/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getMyReviewCount(@RequestHeader("Authorization") String authHeader) {
        try {
            // JWT 토큰에서 사용자 정보 추출
            String token = authHeader.replace("Bearer ", "");
            if (!jwtProvider.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String userEmail = jwtProvider.getUserEmailFromToken(token);
            Long userId = userService.getUserIdByEmail(userEmail);

            if (userId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            int count = myReviewService.getMyReviewCount(userId);
            return ResponseEntity.ok(Map.of("count", count));

        } catch (Exception e) {
            System.out.println("마이 리뷰 개수 조회 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 