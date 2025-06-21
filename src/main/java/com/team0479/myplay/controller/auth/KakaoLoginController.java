package com.team0479.myplay.controller.auth;

import com.team0479.myplay.domain.user.User;
import com.team0479.myplay.dto.auth.KakaoUserInfoResponseDto;
import com.team0479.myplay.service.auth.KakaoService;
import com.team0479.myplay.service.user.UserService;
import com.team0479.myplay.config.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final UserService userService;
    private final JwtProvider jwtProvider;

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        // 1. 인가 코드로 액세스 토큰 받기
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 2. 액세스 토큰으로 사용자 정보 요청
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);

        // 3. User 엔티티 생성
        User user = new User();
        user.setEmail("kakao_" + userInfo.getId());
        user.setNickname(userInfo.getNickname());
        user.setProfileImage(userInfo.getProfileImageUrl());
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        // 4. DB에 저장 (중복 검사 포함)
        userService.register(user);

        // 5. JWT 생성
        String jwt = jwtProvider.generateToken(user.getEmail());

        // 6. 프론트로 리디렉션
        String redirectUrl = "myplay://callback?token=" + jwt;
        response.sendRedirect(redirectUrl);
    }

}
