package com.team0479.myplay.repository.review;

import com.team0479.myplay.dto.review.CommentDto;
import com.team0479.myplay.dto.review.CommentCreateDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public CommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<CommentDto> commentRowMapper = (rs, rowNum) -> {
        CommentDto dto = new CommentDto();
        dto.setCommentId(rs.getLong("comment_id"));
        dto.setContent(rs.getString("content"));
        dto.setCreatedAt(rs.getObject("created_at", LocalDateTime.class));
        dto.setReviewId(rs.getLong("review_id"));
        dto.setUserId(rs.getLong("user_id"));
        dto.setUserNickname(rs.getString("user_nickname"));
        dto.setUserProfileImage(rs.getString("user_profile_image"));
        dto.setUserLevel(rs.getObject("user_level", Integer.class));
        return dto;
    };

    /**
     * 특정 리뷰의 댓글 목록 조회
     */
    public List<CommentDto> findCommentsByReviewId(Long reviewId) {
        String sql = """
            SELECT c.id as comment_id, c.content, c.created_at, c.review_id, c.user_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, u.level as user_level
            FROM comment c
            LEFT JOIN user u ON c.user_id = u.id
            WHERE c.review_id = ?
            ORDER BY c.created_at ASC
            """;
        return jdbcTemplate.query(sql, commentRowMapper, reviewId);
    }

    /**
     * 댓글 작성
     */
    public Long createComment(CommentCreateDto createDto) {
        String sql = """
            INSERT INTO comment (content, review_id, user_id, created_at)
            VALUES (?, ?, ?, ?)
            """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, createDto.getContent());
            ps.setLong(2, createDto.getReviewId());
            ps.setLong(3, createDto.getUserId());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    /**
     * 댓글 삭제
     */
    public void deleteComment(Long commentId) {
        String sql = "DELETE FROM comment WHERE id = ?";
        jdbcTemplate.update(sql, commentId);
    }

    /**
     * 댓글 ID로 댓글 조회
     */
    public CommentDto findCommentById(Long commentId) {
        String sql = """
            SELECT c.id as comment_id, c.content, c.created_at, c.review_id, c.user_id,
                   u.nickname as user_nickname, u.profile_image as user_profile_image, u.level as user_level
            FROM comment c
            LEFT JOIN user u ON c.user_id = u.id
            WHERE c.id = ?
            """;
        List<CommentDto> results = jdbcTemplate.query(sql, commentRowMapper, commentId);
        return results.isEmpty() ? null : results.get(0);
    }
} 