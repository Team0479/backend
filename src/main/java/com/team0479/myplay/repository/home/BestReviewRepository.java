package com.team0479.myplay.repository.home;

import com.team0479.myplay.dto.home.BestReviewDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BestReviewRepository {

    private final JdbcTemplate jdbcTemplate;

    public BestReviewRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 전체 베스트 리뷰 목록을 조회합니다.
     * 좋아요 수를 기준으로 내림차순 정렬합니다.
     * 
     * @return 베스트 리뷰 목록
     */
    public List<BestReviewDto> findBestReviews() {
        String sql = """
            SELECT 
                r.id AS review_id,
                r.content,
                r.rating,
                r.like_count,
                r.view_count,
                r.created_at,
                r.performance_id,
                p.title AS performance_title,
                p.category AS performance_category,
                r.user_id,
                u.nickname AS user_nickname,
                u.profile_image AS user_profile_image
            FROM review r
            LEFT JOIN performance p ON r.performance_id = p.id
            LEFT JOIN user u ON r.user_id = u.id
            ORDER BY r.like_count DESC, r.created_at DESC
            """;

        return jdbcTemplate.query(sql, bestReviewRowMapper());
    }

    /**
     * 특정 장르의 베스트 리뷰 목록을 조회합니다.
     * 좋아요 수를 기준으로 내림차순 정렬합니다.
     * 
     * @param category 장르
     * @return 해당 장르의 베스트 리뷰 목록
     */
    public List<BestReviewDto> findBestReviewsByCategory(String category) {
        String sql = """
            SELECT 
                r.id AS review_id,
                r.content,
                r.rating,
                r.like_count,
                r.view_count,
                r.created_at,
                r.performance_id,
                p.title AS performance_title,
                p.category AS performance_category,
                r.user_id,
                u.nickname AS user_nickname,
                u.profile_image AS user_profile_image
            FROM review r
            LEFT JOIN performance p ON r.performance_id = p.id
            LEFT JOIN user u ON r.user_id = u.id
            WHERE p.category = ?
            ORDER BY r.like_count DESC, r.created_at DESC
            """;

        return jdbcTemplate.query(sql, new Object[]{category}, bestReviewRowMapper());
    }

    private RowMapper<BestReviewDto> bestReviewRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            return new BestReviewDto(
                rs.getLong("review_id"),
                rs.getString("content"),
                rs.getInt("rating"),
                rs.getInt("like_count"),
                rs.getInt("view_count"),
                rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null,
                rs.getLong("performance_id"),
                rs.getString("performance_title"),
                rs.getString("performance_category"),
                rs.getLong("user_id"),
                rs.getString("user_nickname"),
                rs.getString("user_profile_image")
            );
        };
    }
} 