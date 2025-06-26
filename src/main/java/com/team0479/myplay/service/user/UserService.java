package com.team0479.myplay.service.user;

import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.user.UserProfileDto;
import com.team0479.myplay.repository.user.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return; // 이미 존재하면 저장 안 함
        }
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    /**
     * 사용자 프로필 조회 (마이페이지용)
     */
    public UserProfileDto getUserProfile(Long userId) {
        try {
            return userRepository.getUserProfile(userId);
        } catch (Exception e) {
            throw new RuntimeException("사용자 프로필을 조회할 수 없습니다: " + e.getMessage());
        }
    }

    /**
     * 이메일로 사용자 조회
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
