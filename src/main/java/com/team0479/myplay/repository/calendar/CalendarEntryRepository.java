package com.team0479.myplay.repository.calendar;

import com.team0479.myplay.dto.calendar.CalendarEntryDto;
import com.team0479.myplay.dto.calendar.CalendarEntryCreateDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CalendarEntryRepository {

    private final JdbcTemplate jdbcTemplate;

    public CalendarEntryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CalendarEntryDto> calendarEntryRowMapper = (rs, rowNum) -> {
        CalendarEntryDto dto = new CalendarEntryDto();
        dto.setId(rs.getLong("id"));
        dto.setRating(rs.getObject("rating", Integer.class));
        dto.setMemo(rs.getString("memo"));
        dto.setTicketingAt(rs.getObject("ticketing_at", LocalDateTime.class));
        dto.setWatchedAt(rs.getObject("watched_at", LocalDateTime.class));
        dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        dto.setCustomTitle(rs.getString("custom_title"));
        dto.setPerformanceId(rs.getLong("performance_id"));
        dto.setPerformanceTitle(rs.getString("performance_title"));
        dto.setPerformanceCategory(rs.getString("performance_category"));
        dto.setPerformanceVenue(rs.getString("performance_venue"));
        dto.setPerformanceStartDate(rs.getObject("performance_start_date", LocalDateTime.class));
        dto.setPerformanceEndDate(rs.getObject("performance_end_date", LocalDateTime.class));
        dto.setUserId(rs.getLong("user_id"));
        return dto;
    };

    public List<CalendarEntryDto> findByUserIdAndDate(Long userId, LocalDate date) {
        String sql = """
            SELECT ce.id, ce.rating, ce.memo, ce.ticketing_at, ce.watched_at, ce.created_at, 
                   ce.custom_title, ce.performance_id, ce.user_id,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.start_date as performance_start_date, 
                   p.end_date as performance_end_date
            FROM calendar_entry ce
            LEFT JOIN performance p ON ce.performance_id = p.id
            WHERE ce.user_id = ? 
              AND (DATE(ce.watched_at) = ? OR DATE(ce.ticketing_at) = ?)
            ORDER BY ce.watched_at ASC, ce.ticketing_at ASC
            """;
        return jdbcTemplate.query(sql, calendarEntryRowMapper, userId, date, date);
    }

    public List<CalendarEntryDto> findByUserId(Long userId) {
        String sql = """
            SELECT ce.id, ce.rating, ce.memo, ce.ticketing_at, ce.watched_at, ce.created_at, 
                   ce.custom_title, ce.performance_id, ce.user_id,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.start_date as performance_start_date, 
                   p.end_date as performance_end_date
            FROM calendar_entry ce
            LEFT JOIN performance p ON ce.performance_id = p.id
            WHERE ce.user_id = ?
            ORDER BY ce.created_at DESC
            """;
        return jdbcTemplate.query(sql, calendarEntryRowMapper, userId);
    }

    public Long createCalendarEntry(CalendarEntryCreateDto createDto) {
        String sql = """
            INSERT INTO calendar_entry (rating, memo, ticketing_at, watched_at, custom_title, performance_id, user_id, created_at)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, createDto.getRating());
            ps.setString(2, createDto.getMemo());
            ps.setObject(3, createDto.getTicketingAt() != null ? Timestamp.valueOf(createDto.getTicketingAt()) : null);
            ps.setObject(4, createDto.getWatchedAt() != null ? Timestamp.valueOf(createDto.getWatchedAt()) : null);
            ps.setString(5, createDto.getCustomTitle());
            ps.setObject(6, createDto.getPerformanceId());
            ps.setLong(7, createDto.getUserId());
            ps.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public CalendarEntryDto findById(Long id) {
        String sql = """
            SELECT ce.id, ce.rating, ce.memo, ce.ticketing_at, ce.watched_at, ce.created_at, 
                   ce.custom_title, ce.performance_id, ce.user_id,
                   p.title as performance_title, p.category as performance_category, 
                   p.venue as performance_venue, p.start_date as performance_start_date, 
                   p.end_date as performance_end_date
            FROM calendar_entry ce
            LEFT JOIN performance p ON ce.performance_id = p.id
            WHERE ce.id = ?
            """;
        List<CalendarEntryDto> results = jdbcTemplate.query(sql, calendarEntryRowMapper, id);
        return results.isEmpty() ? null : results.get(0);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM calendar_entry WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void updateCalendarEntry(Long id, CalendarEntryCreateDto updateDto) {
        String sql = """
            UPDATE calendar_entry 
            SET rating = ?, memo = ?, ticketing_at = ?, watched_at = ?, 
                custom_title = ?, performance_id = ?
            WHERE id = ?
            """;
        jdbcTemplate.update(sql, 
            updateDto.getRating(),
            updateDto.getMemo(),
            updateDto.getTicketingAt() != null ? Timestamp.valueOf(updateDto.getTicketingAt()) : null,
            updateDto.getWatchedAt() != null ? Timestamp.valueOf(updateDto.getWatchedAt()) : null,
            updateDto.getCustomTitle(),
            updateDto.getPerformanceId(),
            id
        );
    }
} 