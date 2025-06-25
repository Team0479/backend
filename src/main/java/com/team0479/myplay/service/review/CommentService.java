package com.team0479.myplay.service.review;

import com.team0479.myplay.dto.review.CommentDto;
import com.team0479.myplay.dto.review.CommentCreateDto;
import com.team0479.myplay.repository.review.CommentRepository;
import com.team0479.myplay.repository.review.ReviewDetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewDetailRepository reviewDetailRepository;

    public CommentService(CommentRepository commentRepository, ReviewDetailRepository reviewDetailRepository) {
        this.commentRepository = commentRepository;
        this.reviewDetailRepository = reviewDetailRepository;
    }

    /**
     * 특정 리뷰의 댓글 목록 조회
     */
    @Transactional(readOnly = true)
    public List<CommentDto> getCommentsByReviewId(Long reviewId) {
        return commentRepository.findCommentsByReviewId(reviewId);
    }

    /**
     * 댓글 작성 (경험치 증가 포함)
     */
    public CommentDto createComment(CommentCreateDto createDto) {
        // 댓글 작성
        Long commentId = commentRepository.createComment(createDto);
        
        // 댓글 작성자에게 경험치 증가 (3 경험치)
        updateUserExperienceAndLevel(createDto.getUserId(), 3);
        
        // 작성된 댓글 정보 반환
        return commentRepository.findCommentById(commentId);
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(Long commentId, Long userId) {
        CommentDto comment = commentRepository.findCommentById(commentId);
        if (comment == null) {
            throw new RuntimeException("Comment not found with id: " + commentId);
        }
        
        // 댓글 작성자 또는 관리자만 삭제 가능 (여기서는 작성자만)
        if (!comment.getUserId().equals(userId)) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }
        
        commentRepository.deleteComment(commentId);
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