package com.team0479.myplay.service.user;

import com.team0479.myplay.domain.user.User;
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
}
