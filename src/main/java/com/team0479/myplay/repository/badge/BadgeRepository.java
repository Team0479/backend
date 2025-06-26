package com.team0479.myplay.repository.badge;

import com.team0479.myplay.dto.badge.UserBadgeDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BadgeRepository {

    private final JdbcTemplate jdbcTemplate;

    public BadgeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 사용자의 모든 칭호 목록 조회 (획득 여부 포함)
     */
    public List<UserBadgeDto> getUserBadges(Long userId) {
        String sql = """
            SELECT 
                b.id as badge_id,
                b.name as badge_name,
                b.title,
                b.description,
                CASE WHEN ub.user_id IS NOT NULL THEN true ELSE false END as is_acquired,
                ub.acquired_at
            FROM badge b
            LEFT JOIN user_badge ub ON b.id = ub.badge_id AND ub.user_id = ?
            ORDER BY b.id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new UserBadgeDto(
                rs.getLong("badge_id"),
                rs.getString("badge_name"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBoolean("is_acquired"),
                rs.getTimestamp("acquired_at") != null ? 
                    rs.getTimestamp("acquired_at").toLocalDateTime() : null
            );
        }, userId);
    }

    /**
     * 사용자가 획득한 칭호만 조회
     */
    public List<UserBadgeDto> getUserAcquiredBadges(Long userId) {
        String sql = """
            SELECT 
                b.id as badge_id,
                b.name as badge_name,
                b.title,
                b.description,
                true as is_acquired,
                ub.acquired_at
            FROM badge b
            JOIN user_badge ub ON b.id = ub.badge_id
            WHERE ub.user_id = ?
            ORDER BY ub.acquired_at DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new UserBadgeDto(
                rs.getLong("badge_id"),
                rs.getString("badge_name"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBoolean("is_acquired"),
                rs.getTimestamp("acquired_at").toLocalDateTime()
            );
        }, userId);
    }

    /**
     * 사용자에게 칭호 부여
     */
    public void awardBadgeToUser(Long userId, Long badgeId) {
        // 이미 획득한 칭호인지 확인
        if (isUserBadgeExists(userId, badgeId)) {
            return; // 이미 획득한 칭호는 중복 부여하지 않음
        }

        String sql = """
            INSERT INTO user_badge (user_id, badge_id, acquired_at)
            VALUES (?, ?, ?)
        """;
        jdbcTemplate.update(sql, userId, badgeId, LocalDateTime.now());
    }

    /**
     * 사용자가 이미 해당 칭호를 획득했는지 확인
     */
    public boolean isUserBadgeExists(Long userId, Long badgeId) {
        String sql = "SELECT COUNT(*) FROM user_badge WHERE user_id = ? AND badge_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, badgeId);
        return count != null && count > 0;
    }

    /**
     * 사용자가 획득한 칭호 개수 조회
     */
    public int getUserBadgeCount(Long userId) {
        String sql = "SELECT COUNT(*) FROM user_badge WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * 전체 칭호 개수 조회
     */
    public int getTotalBadgeCount() {
        String sql = "SELECT COUNT(*) FROM badge";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    /**
     * 사용자의 캘린더 일정 등록 수 조회
     */
    public int getUserCalendarCount(Long userId) {
        String sql = "SELECT COUNT(*) FROM calendar_entry WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * 사용자의 리뷰 작성 수 조회
     */
    public int getUserReviewCount(Long userId) {
        String sql = "SELECT COUNT(*) FROM review WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * 사용자의 댓글 작성 수 조회
     */
    public int getUserCommentCount(Long userId) {
        String sql = "SELECT COUNT(*) FROM comment WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * 현재 베스트 플레이어 1위 조회
     */
    public Long getCurrentBestPlayer() {
        String sql = """
            SELECT u.id
            FROM user u 
            LEFT JOIN calendar_entry ce ON ce.user_id = u.id 
            LEFT JOIN review r ON r.user_id = u.id
            GROUP BY u.id, u.nickname 
            ORDER BY COUNT(DISTINCT ce.id) + COUNT(DISTINCT r.id) DESC
            LIMIT 1
        """;
        
        try {
            return jdbcTemplate.queryForObject(sql, Long.class);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 베스트 리뷰 상위 10위 사용자 조회
     */
    public List<Long> getTop10ReviewUsers() {
        String sql = """
            SELECT r.user_id
            FROM review r
            JOIN performance p ON r.performance_id = p.id
            JOIN user u ON r.user_id = u.id
            ORDER BY r.like_count DESC, r.created_at DESC
            LIMIT 10
        """;
        
        return jdbcTemplate.queryForList(sql, Long.class);
    }
} 