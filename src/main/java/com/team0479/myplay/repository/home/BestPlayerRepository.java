package com.team0479.myplay.repository.home;

import com.team0479.myplay.dto.home.BestPlayerDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BestPlayerRepository {

    private final JdbcTemplate jdbcTemplate;

    public BestPlayerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 베스트 플레이어 목록을 조회합니다.
     * 리뷰 작성 수와 공연 관람 수를 합한 총 활동량을 기준으로 정렬합니다.
     * 
     * @param limit 조회할 플레이어 수
     * @return 베스트 플레이어 목록
     */
    public List<BestPlayerDto> findBestPlayers(int limit) {
        String sql = """
            SELECT 
                u.id AS user_id,
                u.nickname,
                u.profile_image,
                COUNT(DISTINCT ce.id) AS calendar_count,
                COUNT(DISTINCT r.id) AS review_count,
                COUNT(DISTINCT ce.id) + COUNT(DISTINCT r.id) AS total_activity
            FROM user u
            LEFT JOIN calendar_entry ce ON ce.user_id = u.id
            LEFT JOIN review r ON r.user_id = u.id
            GROUP BY u.id, u.nickname, u.profile_image
            ORDER BY total_activity DESC
            LIMIT ?
            """;
        
        return jdbcTemplate.query(sql, new Object[]{limit}, bestPlayerRowMapper());
    }

    private RowMapper<BestPlayerDto> bestPlayerRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            return new BestPlayerDto(
                rs.getLong("user_id"),
                rs.getString("nickname"),
                rs.getString("profile_image"),
                rs.getInt("calendar_count"),
                rs.getInt("review_count"),
                rs.getInt("total_activity")
            );
        };
    }
} 