package com.team0479.myplay.repository.mission;

import com.team0479.myplay.dto.mission.MissionDto;
import com.team0479.myplay.dto.mission.UserMissionDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MissionRepository {

    private final JdbcTemplate jdbcTemplate;

    public MissionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 사용자의 미션 목록 조회 (진행도 포함)
     */
    public List<UserMissionDto> getUserMissions(Long userId) {
        String sql = """
            SELECT 
                um.id,
                um.user_id,
                um.mission_id,
                m.name as mission_name,
                m.description as mission_description,
                m.type as mission_type,
                m.goal,
                um.progress,
                um.is_completed,
                um.last_updated
            FROM user_mission um
            JOIN mission m ON um.mission_id = m.id
            WHERE um.user_id = ?
            ORDER BY m.id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new UserMissionDto(
                rs.getLong("id"),
                rs.getLong("user_id"),
                rs.getLong("mission_id"),
                rs.getString("mission_name"),
                rs.getString("mission_description"),
                rs.getString("mission_type"),
                rs.getInt("goal"),
                rs.getInt("progress"),
                rs.getBoolean("is_completed"),
                rs.getTimestamp("last_updated").toLocalDateTime()
            );
        }, userId);
    }

    /**
     * 모든 활성 미션 조회
     */
    public List<MissionDto> getAllActiveMissions() {
        String sql = """
            SELECT id, name, description, type, start_date, end_date, goal, target_action, created_at
            FROM mission
            WHERE (end_date IS NULL OR end_date >= CURDATE())
            ORDER BY id
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new MissionDto(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("type"),
                rs.getDate("start_date") != null ? rs.getDate("start_date").toLocalDate() : null,
                rs.getDate("end_date") != null ? rs.getDate("end_date").toLocalDate() : null,
                rs.getInt("goal"),
                rs.getString("target_action"),
                rs.getTimestamp("created_at").toLocalDateTime()
            );
        });
    }

    /**
     * 사용자에게 미션 할당
     */
    public void assignMissionToUser(Long userId, Long missionId) {
        String sql = """
            INSERT INTO user_mission (user_id, mission_id, progress, is_completed, last_updated)
            VALUES (?, ?, 0, false, ?)
        """;
        jdbcTemplate.update(sql, userId, missionId, LocalDateTime.now());
    }

    /**
     * 사용자의 미션 진행도 업데이트
     */
    public void updateMissionProgress(Long userId, String targetAction, int incrementValue) {
        // 해당 액션에 맞는 미션들 찾기
        String findMissionsSql = """
            SELECT um.id, um.progress, m.goal, um.is_completed
            FROM user_mission um
            JOIN mission m ON um.mission_id = m.id
            WHERE um.user_id = ? AND m.target_action = ? AND um.is_completed = false
        """;

        List<UserMissionDto> userMissions = jdbcTemplate.query(findMissionsSql, (rs, rowNum) -> {
            UserMissionDto dto = new UserMissionDto();
            dto.setId(rs.getLong("id"));
            dto.setProgress(rs.getInt("progress"));
            dto.setGoal(rs.getInt("goal"));
            dto.setIsCompleted(rs.getBoolean("is_completed"));
            return dto;
        }, userId, targetAction);

        // 각 미션의 진행도 업데이트
        for (UserMissionDto userMission : userMissions) {
            int newProgress = Math.min(userMission.getProgress() + incrementValue, userMission.getGoal());
            boolean isCompleted = newProgress >= userMission.getGoal();

            String updateSql = """
                UPDATE user_mission 
                SET progress = ?, is_completed = ?, last_updated = ?
                WHERE id = ?
            """;
            jdbcTemplate.update(updateSql, newProgress, isCompleted, LocalDateTime.now(), userMission.getId());
        }
    }

    /**
     * 사용자가 이미 할당받은 미션인지 확인
     */
    public boolean isUserMissionExists(Long userId, Long missionId) {
        String sql = "SELECT COUNT(*) FROM user_mission WHERE user_id = ? AND mission_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, missionId);
        return count != null && count > 0;
    }

    /**
     * 월별 미션 진행도 초기화
     */
    public void resetMonthlyMissions() {
        String sql = """
            UPDATE user_mission um
            JOIN mission m ON um.mission_id = m.id
            SET um.progress = 0, um.is_completed = false, um.last_updated = ?
            WHERE m.type = 'MONTHLY'
        """;
        jdbcTemplate.update(sql, LocalDateTime.now());
    }
} 