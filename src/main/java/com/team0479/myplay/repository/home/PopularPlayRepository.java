package com.team0479.myplay.repository.home;

import com.team0479.myplay.dto.home.PopularPlayDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PopularPlayRepository {

    private final JdbcTemplate jdbcTemplate;

    public PopularPlayRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 특정 장르의 인기 플레이 목록을 조회합니다.
     * performance 테이블의 ranking 필드를 기준으로 정렬합니다.
     * 
     * @param category 장르
     * @return 인기 플레이 목록
     */
    public List<PopularPlayDto> findPopularPlaysByCategory(String category) {
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
            """;

        return jdbcTemplate.query(sql, new Object[]{category}, popularPlayRowMapper());
    }



    private RowMapper<PopularPlayDto> popularPlayRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            return new PopularPlayDto(
                rs.getLong("performance_id"),
                rs.getString("title"),
                rs.getString("category"),
                rs.getString("venue"),
                rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                rs.getString("description"),
                rs.getString("image_url"),
                rs.getInt("ranking")
            );
        };
    }
} 