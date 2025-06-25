package com.team0479.myplay.repository.review;

import com.team0479.myplay.dto.review.ReviewDetailDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReviewDetailRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDetailRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReviewDetailDto> reviewDetailRowMapper = (rs, rowNum) -> {
        ReviewDetailDto dto = new ReviewDetailDto();
        dto.setReviewId(rs.getLong("review_id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setRating(rs.getObject("rating", Integer.class));
        dto.setLikeCount(rs.getObject("like_count", Integer.class));
        dto.setViewCount(rs.getObject("view_count", Integer.class));
        dto.setCommentCount(rs.getObject("comment_count", Integer.class));
        dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        dto.setPerformanceId(rs.getLong("performance_id"));
        dto.setPerformanceTitle(rs.getString("performance_title"));
        dto.setPerformanceCategory(rs.getString("performance_category"));
        dto.setPerformanceVenue(rs.getString("performance_venue"));
        dto.setPerformanceImageUrl(rs.getString("performance_image_url"));
        dto.setUserId(rs.getLong("user_id"));
        dto.setUserNickname(rs.getString("user_nickname"));
        dto.setUserProfileImage(rs.getString("user_profile_image"));
        dto.setUserLevel(rs.getObject("user_level", Integer.class));
        return dto;
    };

    /**
     * 리뷰 상세 정보 조회 (기본 정보만, 이미지와 좋아요 여부는 별도 조회)
     */
    public ReviewDetailDto findReviewDetailById(Long reviewId) {
        String sql = """
            SELECT r.id as review_id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   r.performance_id, p.title as performance_title, p.category as performance_category,
                   p.venue as performance_venue, p.image_url as performance_image_url,
                   r.user_id, u.nickname as user_nickname, u.profile_image as user_profile_image, u.level as user_level,
                   COALESCE(cc.comment_count, 0) as comment_count
            FROM review r
            LEFT JOIN performance p ON r.performance_id = p.id
            LEFT JOIN user u ON r.user_id = u.id
            LEFT JOIN (
                SELECT review_id, COUNT(*) as comment_count
                FROM comment
                GROUP BY review_id
            ) cc ON r.id = cc.review_id
            WHERE r.id = ?
            """;
        List<ReviewDetailDto> results = jdbcTemplate.query(sql, reviewDetailRowMapper, reviewId);
        return results.isEmpty() ? null : results.get(0);
    }

    /**
     * 리뷰의 모든 이미지 URL 조회
     */
    public List<String> findReviewImageUrls(Long reviewId) {
        String sql = "SELECT image_url FROM review_image WHERE review_id = ? ORDER BY id ASC";
        return jdbcTemplate.queryForList(sql, String.class, reviewId);
    }

    /**
     * 사용자가 해당 리뷰에 좋아요를 눌렀는지 확인
     */
    public boolean isLikedByUser(Long reviewId, Long userId) {
        String sql = "SELECT COUNT(*) FROM review_like WHERE review_id = ? AND user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, reviewId, userId);
        return count != null && count > 0;
    }

    /**
     * 리뷰 조회수 증가
     */
    public void incrementViewCount(Long reviewId) {
        String sql = "UPDATE review SET view_count = view_count + 1 WHERE id = ?";
        jdbcTemplate.update(sql, reviewId);
    }

    /**
     * 리뷰 좋아요 추가
     */
    public void addLike(Long reviewId, Long userId) {
        // 좋아요 테이블에 추가
        String insertLikeSql = "INSERT INTO review_like (review_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(insertLikeSql, reviewId, userId);
        
        // 리뷰 좋아요 카운트 증가
        String updateCountSql = "UPDATE review SET like_count = like_count + 1 WHERE id = ?";
        jdbcTemplate.update(updateCountSql, reviewId);
    }

    /**
     * 리뷰 좋아요 제거
     */
    public void removeLike(Long reviewId, Long userId) {
        // 좋아요 테이블에서 제거
        String deleteLikeSql = "DELETE FROM review_like WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(deleteLikeSql, reviewId, userId);
        
        // 리뷰 좋아요 카운트 감소
        String updateCountSql = "UPDATE review SET like_count = like_count - 1 WHERE id = ?";
        jdbcTemplate.update(updateCountSql, reviewId);
    }

    /**
     * 사용자 경험치 업데이트
     */
    public void updateUserExperience(Long userId, int expGain) {
        String sql = "UPDATE user SET exp = exp + ? WHERE id = ?";
        jdbcTemplate.update(sql, expGain, userId);
    }

    /**
     * 사용자의 현재 경험치 조회
     */
    public int getUserCurrentExp(Long userId) {
        String sql = "SELECT exp FROM user WHERE id = ?";
        Integer exp = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return exp != null ? exp : 0;
    }

    /**
     * 사용자의 현재 레벨 조회
     */
    public int getUserCurrentLevel(Long userId) {
        String sql = "SELECT level FROM user WHERE id = ?";
        Integer level = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return level != null ? level : 1;
    }

    /**
     * 특정 레벨에 필요한 경험치 조회
     */
    public int getRequiredExpForLevel(int level) {
        String sql = "SELECT required_exp FROM level_requirement WHERE level = ?";
        Integer requiredExp = jdbcTemplate.queryForObject(sql, Integer.class, level);
        return requiredExp != null ? requiredExp : 0;
    }

    /**
     * 사용자 레벨 업데이트
     */
    public void updateUserLevel(Long userId, int newLevel) {
        String sql = "UPDATE user SET level = ? WHERE id = ?";
        jdbcTemplate.update(sql, newLevel, userId);
    }
} 