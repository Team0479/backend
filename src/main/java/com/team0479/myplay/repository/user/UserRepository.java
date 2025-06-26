package com.team0479.myplay.repository.user;

import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.user.UserProfileDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(User user) {
        String sql = "INSERT INTO user (email, password, nickname, profile_image, role, created_at) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                user.getEmail(),
                user.getPassword(),
                user.getNickname(),
                user.getProfileImage(),
                user.getRole(),
                user.getCreatedAt()
        );
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM user WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";
        return jdbcTemplate.query(sql, new Object[]{email}, userRowMapper())
                .stream().findFirst();
    }

    /**
     * 이메일로 사용자 ID 조회
     */
    public Long getUserIdByEmail(String email) {
        String sql = "SELECT id FROM user WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, email);
        } catch (Exception e) {
            System.out.println("사용자 ID 조회 실패 - 이메일: " + email + ", 오류: " + e.getMessage());
            return null;
        }
    }

    /**
     * 사용자 프로필 정보 조회 (마이페이지용)
     */
    public UserProfileDto getUserProfile(Long userId) {
        String sql = """
            SELECT 
                u.id,
                u.nickname,
                u.profile_image,
                u.level,
                u.exp,
                COALESCE(lr.required_exp, 0) as required_exp_for_next_level
            FROM user u
            LEFT JOIN level_requirement lr ON lr.level = u.level + 1
            WHERE u.id = ?
        """;
        
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            return new UserProfileDto(
                rs.getLong("id"),
                rs.getString("nickname"),
                rs.getString("profile_image"),
                rs.getInt("level"),
                rs.getInt("exp"),
                rs.getInt("required_exp_for_next_level")
            );
        }, userId);
    }

    private RowMapper<User> userRowMapper() {
        return (ResultSet rs, int rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setNickname(rs.getString("nickname"));
            user.setProfileImage(rs.getString("profile_image"));
            user.setRole(rs.getString("role"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return user;
        };
    }
}
