package com.team0479.myplay.repository.user;

import com.team0479.myplay.dto.plaza.ReviewListDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MyReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public MyReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReviewListDto> reviewListRowMapper = (rs, rowNum) -> {
        ReviewListDto dto = new ReviewListDto();
        dto.setId(rs.getLong("id"));
        dto.setTitle(rs.getString("title"));
        dto.setContent(rs.getString("content"));
        dto.setRating(rs.getObject("rating", Integer.class));
        dto.setLikeCount(rs.getObject("like_count", Integer.class));
        dto.setViewCount(rs.getObject("view_count", Integer.class));
        dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        dto.setPerformanceTitle(rs.getString("performance_title"));
        dto.setPerformanceCategory(rs.getString("performance_category"));
        dto.setPerformanceVenue(rs.getString("performance_venue"));
        dto.setPerformanceImageUrl(rs.getString("performance_image_url"));
        dto.setPerformanceId(rs.getLong("performance_id"));
        dto.setUserNickname(rs.getString("user_nickname"));
        dto.setUserProfileImage(rs.getString("user_profile_image"));
        dto.setUserId(rs.getLong("user_id"));
        dto.setReviewImageUrl(rs.getString("review_image_url"));
        return dto;
    };

    /**
     * 특정 사용자가 작성한 리뷰 목록 조회 (최신순)
     */
    public List<ReviewListDto> findMyReviews(Long userId) {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, p.id as performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, u.id as user_id,
                   (SELECT ri.image_url FROM review_image ri WHERE ri.review_id = r.id LIMIT 1) as review_image_url
            FROM review r
            LEFT JOIN performance p ON r.performance_id = p.id
            LEFT JOIN user u ON r.user_id = u.id
            WHERE r.user_id = ?
            ORDER BY r.created_at DESC
            """;
        return jdbcTemplate.query(sql, reviewListRowMapper, userId);
    }

    /**
     * 특정 사용자가 작성한 리뷰 개수 조회
     */
    public int countMyReviews(Long userId) {
        String sql = "SELECT COUNT(*) FROM review WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * 특정 사용자가 작성한 평점별 리뷰 목록 조회
     */
    public List<ReviewListDto> findMyReviewsByRating(Long userId, Integer rating) {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, p.id as performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, u.id as user_id,
                   (SELECT ri.image_url FROM review_image ri WHERE ri.review_id = r.id LIMIT 1) as review_image_url
            FROM review r
            LEFT JOIN performance p ON r.performance_id = p.id
            LEFT JOIN user u ON r.user_id = u.id
            WHERE r.user_id = ? AND r.rating = ?
            ORDER BY r.created_at DESC
            """;
        return jdbcTemplate.query(sql, reviewListRowMapper, userId, rating);
    }

    /**
     * 특정 사용자가 작성한 카테고리별 리뷰 목록 조회
     */
    public List<ReviewListDto> findMyReviewsByCategory(Long userId, String category) {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, p.id as performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, u.id as user_id,
                   (SELECT ri.image_url FROM review_image ri WHERE ri.review_id = r.id LIMIT 1) as review_image_url
            FROM review r
            LEFT JOIN performance p ON r.performance_id = p.id
            LEFT JOIN user u ON r.user_id = u.id
            WHERE r.user_id = ? AND p.category = ?
            ORDER BY r.created_at DESC
            """;
        return jdbcTemplate.query(sql, reviewListRowMapper, userId, category);
    }
} 