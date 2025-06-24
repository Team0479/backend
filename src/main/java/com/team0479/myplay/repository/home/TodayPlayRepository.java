package com.team0479.myplay.repository.home;

import com.team0479.myplay.dto.home.TodayPlayDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class TodayPlayRepository {

    private final JdbcTemplate jdbcTemplate;

    public TodayPlayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 특정 유저가 가장 많이 등록한 장르를 조회합니다.
     * 
     * @param userId 유저 ID
     * @return 가장 많이 등록한 장르명
     */
    public String findMostRegisteredCategoryByUser(Long userId) {
        String sql = """
            SELECT p.category
            FROM calendar_entry ce
            LEFT JOIN performance p ON ce.performance_id = p.id
            WHERE ce.user_id = ? AND p.category IS NOT NULL
            GROUP BY p.category
            ORDER BY COUNT(*) DESC
            LIMIT 1
            """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId}, String.class);
        } catch (Exception e) {
            // 데이터가 없거나 에러 발생 시 기본값 반환
            return "뮤지컬"; // 기본 추천 장르
        }
    }

    /**
     * 특정 유저가 특정 장르에 등록한 개수를 조회합니다.
     * 
     * @param userId 유저 ID
     * @param category 장르
     * @return 등록 개수
     */
    public Integer getCategoryCountByUser(Long userId, String category) {
        String sql = """
            SELECT COUNT(*) as count
            FROM calendar_entry ce
            LEFT JOIN performance p ON ce.performance_id = p.id
            WHERE ce.user_id = ? AND p.category = ?
            """;

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userId, category}, Integer.class);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 특정 장르의 추천 공연 목록을 조회합니다.
     * ranking이 낮은 순서대로 정렬합니다.
     * 
     * @param category 장르
     * @param limit 조회할 개수
     * @return 추천 공연 목록
     */
    public List<TodayPlayDto> findRecommendedPlaysByCategory(String category, int limit) {
        String sql = """
            SELECT 
                p.id AS performance_id,
                p.title,
                p.category,
                p.venue,
                p.start_date,
                p.end_date,
                p.description,
                p.image_url,
                p.ranking
            FROM performance p
            WHERE p.category = ?
            ORDER BY p.ranking ASC, p.title ASC
            LIMIT ?
            """;

        return jdbcTemplate.query(sql, new Object[]{category, limit}, todayPlayRowMapper());
    }

    private RowMapper<TodayPlayDto> todayPlayRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            return new TodayPlayDto(
                rs.getLong("performance_id"),
                rs.getString("title"),
                rs.getString("category"),
                rs.getString("venue"),
                rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                rs.getString("description"),
                rs.getString("image_url"),
                rs.getInt("ranking"),
                null // recommendReason은 Service에서 설정
            );
        };
    }
} 