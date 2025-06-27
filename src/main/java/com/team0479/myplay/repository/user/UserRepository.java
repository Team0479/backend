package com.team0479.myplay.repository.user;

import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.user.UserProfileDto;
import com.team0479.myplay.dto.user.UserExistenceDto;
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
     * 사용자 ID로 존재 여부 및 프로필 완성도 확인
     */
    public UserExistenceDto checkUserExistence(Long userId) {
        String sql = "SELECT id, nickname, profile_image, COALESCE(profile_completed, false) as profile_completed FROM user WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Long id = rs.getLong("id");
                String nickname = rs.getString("nickname");
                String profileImage = rs.getString("profile_image");
                boolean profileCompleted = rs.getBoolean("profile_completed");
                
                return new UserExistenceDto(id, true, profileCompleted, nickname, profileImage);
            }, userId);
        } catch (Exception e) {
            System.out.println("사용자 존재 확인 실패 - ID: " + userId + ", 오류: " + e.getMessage());
            return new UserExistenceDto(null, false, false, null, null);
        }
    }

    /**
     * 이메일로 사용자 존재 여부 및 프로필 완성도 확인
     */
    public UserExistenceDto checkUserExistenceByEmail(String email) {
        String sql = "SELECT id, nickname, profile_image, COALESCE(profile_completed, false) as profile_completed FROM user WHERE email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
                Long id = rs.getLong("id");
                String nickname = rs.getString("nickname");
                String profileImage = rs.getString("profile_image");
                boolean profileCompleted = rs.getBoolean("profile_completed");
                
                return new UserExistenceDto(id, true, profileCompleted, nickname, profileImage);
            }, email);
        } catch (Exception e) {
            System.out.println("사용자 존재 확인 실패 - 이메일: " + email + ", 오류: " + e.getMessage());
            return new UserExistenceDto(null, false, false, null, null);
        }
    }

    /**
     * 사용자 프로필 업데이트 (닉네임, 프로필 이미지, 프로필 완성 상태)
     */
    public boolean updateUserProfile(Long userId, String nickname, String profileImage) {
        String sql = "UPDATE user SET nickname = ?, profile_image = ?, profile_completed = true WHERE id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, nickname, profileImage, userId);
            System.out.println("프로필 업데이트 완료 - ID: " + userId + ", 닉네임: " + nickname + ", profile_completed: true");
            return rowsAffected > 0;
        } catch (Exception e) {
            System.out.println("프로필 업데이트 실패 - ID: " + userId + ", 오류: " + e.getMessage());
            return false;
        }
    }

    /**
     * 닉네임 중복 체크
     */
    public boolean isNicknameExists(String nickname) {
        String sql = "SELECT COUNT(*) FROM user WHERE nickname = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nickname);
            return count != null && count > 0;
        } catch (Exception e) {
            System.out.println("닉네임 중복 체크 실패 - 닉네임: " + nickname + ", 오류: " + e.getMessage());
            return true; // 오류 시 중복으로 처리 (안전)
        }
    }

    /**
     * 닉네임 중복 체크 (자신 제외)
     */
    public boolean isNicknameExistsExcludeUser(String nickname, Long userId) {
        String sql = "SELECT COUNT(*) FROM user WHERE nickname = ? AND id != ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, nickname, userId);
            return count != null && count > 0;
        } catch (Exception e) {
            System.out.println("닉네임 중복 체크 실패 (자신 제외) - 닉네임: " + nickname + ", 사용자ID: " + userId + ", 오류: " + e.getMessage());
            return true; // 오류 시 중복으로 처리 (안전)
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
            user.setProfileCompleted(rs.getBoolean("profile_completed"));
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            return user;
        };
    }
}
