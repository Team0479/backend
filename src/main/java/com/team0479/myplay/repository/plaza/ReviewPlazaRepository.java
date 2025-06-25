package com.team0479.myplay.repository.plaza;

import com.team0479.myplay.dto.plaza.ReviewListDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ReviewPlazaRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewPlazaRepository(JdbcTemplate jdbcTemplate) {
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
     * 특정 공연에 대한 리뷰 목록 조회 (좋아요 순)
     */
    public List<ReviewListDto> findReviewsByPerformanceTitle(String performanceTitle) {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, r.performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, r.user_id,
                   ri.image_url as review_image_url
            FROM review r
            INNER JOIN performance p ON r.performance_id = p.id
            INNER JOIN user u ON r.user_id = u.id
            LEFT JOIN (
                SELECT review_id, image_url, 
                       ROW_NUMBER() OVER (PARTITION BY review_id ORDER BY id ASC) as rn
                FROM review_image
            ) ri ON r.id = ri.review_id AND ri.rn = 1
            WHERE p.title LIKE ?
            ORDER BY r.like_count DESC, r.created_at DESC
            """;
        String searchPattern = "%" + performanceTitle + "%";
        return jdbcTemplate.query(sql, reviewListRowMapper, searchPattern);
    }

    /**
     * 모든 리뷰 목록 조회 (조회수 순)
     */
    public List<ReviewListDto> findAllReviewsByViewCount() {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, r.performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, r.user_id,
                   ri.image_url as review_image_url
            FROM review r
            INNER JOIN performance p ON r.performance_id = p.id
            INNER JOIN user u ON r.user_id = u.id
            LEFT JOIN (
                SELECT review_id, image_url, 
                       ROW_NUMBER() OVER (PARTITION BY review_id ORDER BY id ASC) as rn
                FROM review_image
            ) ri ON r.id = ri.review_id AND ri.rn = 1
            ORDER BY r.view_count DESC, r.created_at DESC
            """;
        return jdbcTemplate.query(sql, reviewListRowMapper);
    }

    /**
     * 카테고리별 리뷰 목록 조회 (조회수 순)
     */
    public List<ReviewListDto> findReviewsByCategory(String category) {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, r.performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, r.user_id,
                   ri.image_url as review_image_url
            FROM review r
            INNER JOIN performance p ON r.performance_id = p.id
            INNER JOIN user u ON r.user_id = u.id
            LEFT JOIN (
                SELECT review_id, image_url, 
                       ROW_NUMBER() OVER (PARTITION BY review_id ORDER BY id ASC) as rn
                FROM review_image
            ) ri ON r.id = ri.review_id AND ri.rn = 1
            WHERE p.category = ?
            ORDER BY r.view_count DESC, r.created_at DESC
            """;
        return jdbcTemplate.query(sql, reviewListRowMapper, category);
    }

    /**
     * 리뷰 수가 많은 공연들의 리뷰 목록 조회
     */
    public List<ReviewListDto> findPopularPerformanceReviews() {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, r.performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, r.user_id,
                   ri.image_url as review_image_url
            FROM review r
            INNER JOIN performance p ON r.performance_id = p.id
            INNER JOIN user u ON r.user_id = u.id
            LEFT JOIN (
                SELECT review_id, image_url, 
                       ROW_NUMBER() OVER (PARTITION BY review_id ORDER BY id ASC) as rn
                FROM review_image
            ) ri ON r.id = ri.review_id AND ri.rn = 1
            INNER JOIN (
                SELECT performance_id, COUNT(*) as review_count
                FROM review
                GROUP BY performance_id
                ORDER BY review_count DESC
                LIMIT 10
            ) popular_p ON r.performance_id = popular_p.performance_id
            ORDER BY r.view_count DESC, r.created_at DESC
            """;
        return jdbcTemplate.query(sql, reviewListRowMapper);
    }

    /**
     * 최신 리뷰 목록 조회
     */
    public List<ReviewListDto> findRecentReviews() {
        String sql = """
            SELECT r.id, r.title, r.content, r.rating, r.like_count, r.view_count, r.created_at,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.image_url as performance_image_url, r.performance_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, r.user_id,
                   ri.image_url as review_image_url
            FROM review r
            INNER JOIN performance p ON r.performance_id = p.id
            INNER JOIN user u ON r.user_id = u.id
            LEFT JOIN (
                SELECT review_id, image_url, 
                       ROW_NUMBER() OVER (PARTITION BY review_id ORDER BY id ASC) as rn
                FROM review_image
            ) ri ON r.id = ri.review_id AND ri.rn = 1
            ORDER BY r.created_at DESC
            LIMIT 50
            """;
        return jdbcTemplate.query(sql, reviewListRowMapper);
    }
} 