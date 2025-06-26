package com.team0479.myplay.repository.review;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDateTime;

@Repository
public class ReviewCreateRepository {

    private final JdbcTemplate jdbcTemplate;

    public ReviewCreateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 리뷰 등록
     */
    public Long createReview(String title, String content, Integer rating, Long performanceId, Long userId) {
        String sql = "INSERT INTO review (title, content, rating, performance_id, user_id, like_count, view_count, created_at) VALUES (?, ?, ?, ?, ?, 0, 0, ?)";
        
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, content);
            ps.setInt(3, rating);
            ps.setLong(4, performanceId);
            ps.setLong(5, userId);
            ps.setObject(6, LocalDateTime.now());
            return ps;
        }, keyHolder);
        
        return keyHolder.getKey().longValue();
    }

    /**
     * 리뷰 이미지 등록
     */
    public void createReviewImage(Long reviewId, String imageUrl) {
        String sql = "INSERT INTO review_image (review_id, image_url) VALUES (?, ?)";
        jdbcTemplate.update(sql, reviewId, imageUrl);
    }

    /**
     * 사용자 경험치 업데이트 (리뷰 작성 시 경험치 추가)
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
        try {
            Integer requiredExp = jdbcTemplate.queryForObject(sql, Integer.class, level);
            return requiredExp != null ? requiredExp : 0;
        } catch (Exception e) {
            return 0; // 해당 레벨이 없으면 0 반환
        }
    }

    /**
     * 사용자 레벨 업데이트
     */
    public void updateUserLevel(Long userId, int newLevel) {
        String sql = "UPDATE user SET level = ? WHERE id = ?";
        jdbcTemplate.update(sql, newLevel, userId);
    }
} 