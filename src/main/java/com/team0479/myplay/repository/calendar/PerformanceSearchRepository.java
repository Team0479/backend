package com.team0479.myplay.repository.calendar;

import com.team0479.myplay.dto.calendar.PerformanceSearchDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class PerformanceSearchRepository {

    private final JdbcTemplate jdbcTemplate;

    public PerformanceSearchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<PerformanceSearchDto> performanceSearchRowMapper = (rs, rowNum) -> {
        PerformanceSearchDto dto = new PerformanceSearchDto();
        dto.setId(rs.getLong("id"));
        dto.setTitle(rs.getString("title"));
        dto.setCategory(rs.getString("category"));
        dto.setVenue(rs.getString("venue"));
        dto.setStartDate(rs.getObject("start_date", LocalDate.class));
        dto.setEndDate(rs.getObject("end_date", LocalDate.class));
        dto.setDescription(rs.getString("description"));
        dto.setImageUrl(rs.getString("image_url"));
        return dto;
    };

    public List<PerformanceSearchDto> searchPerformancesByTitle(String title) {
        String sql = """
            SELECT id, title, category, venue, start_date, end_date, description, image_url
            FROM performance 
            WHERE title LIKE ?
            ORDER BY 
                CASE WHEN title = ? THEN 1 ELSE 2 END,
                LENGTH(title),
                title
            LIMIT 10
            """;
        String searchPattern = "%" + title + "%";
        return jdbcTemplate.query(sql, performanceSearchRowMapper, searchPattern, title);
    }

    public PerformanceSearchDto findPerformanceById(Long performanceId) {
        String sql = """
            SELECT id, title, category, venue, start_date, end_date, description, image_url
            FROM performance 
            WHERE id = ?
            """;
        List<PerformanceSearchDto> results = jdbcTemplate.query(sql, performanceSearchRowMapper, performanceId);
        return results.isEmpty() ? null : results.get(0);
    }

    public List<PerformanceSearchDto> searchPerformancesByCategory(String category) {
        String sql = """
            SELECT id, title, category, venue, start_date, end_date, description, image_url
            FROM performance 
            WHERE category = ?
            ORDER BY ranking ASC
            LIMIT 20
            """;
        return jdbcTemplate.query(sql, performanceSearchRowMapper, category);
    }

    public List<PerformanceSearchDto> getRecentPerformances() {
        String sql = """
            SELECT id, title, category, venue, start_date, end_date, description, image_url
            FROM performance 
            WHERE start_date >= CURDATE()
            ORDER BY start_date ASC
            LIMIT 20
            """;
        return jdbcTemplate.query(sql, performanceSearchRowMapper);
    }
} 