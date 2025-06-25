package com.team0479.myplay.controller.review;

import com.team0479.myplay.dto.review.CommentDto;
import com.team0479.myplay.dto.review.CommentCreateDto;
import com.team0479.myplay.service.review.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 특정 리뷰의 댓글 목록 조회
     * GET /api/comments/review/{reviewId}
     */
    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<CommentDto>> getCommentsByReviewId(@PathVariable Long reviewId) {
        try {
            List<CommentDto> comments = commentService.getCommentsByReviewId(reviewId);
            return ResponseEntity.ok(comments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 댓글 작성
     * POST /api/comments
     */
    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentCreateDto createDto) {
        try {
            CommentDto createdComment = commentService.createComment(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * 댓글 삭제
     * DELETE /api/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Map<String, String>> deleteComment(
            @PathVariable Long commentId,
            @RequestBody Map<String, Long> requestBody) {
        try {
            Long userId = requestBody.get("userId");
            if (userId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "userId is required"));
            }

            commentService.deleteComment(commentId, userId);
            return ResponseEntity.ok(Map.of("message", "댓글이 삭제되었습니다."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "댓글 삭제에 실패했습니다."));
        }
    }
} 