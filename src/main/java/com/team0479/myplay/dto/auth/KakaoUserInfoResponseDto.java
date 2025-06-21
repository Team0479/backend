package com.team0479.myplay.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfoResponseDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    // nickname 추출
    public String getNickname() {
        return kakaoAccount != null && kakaoAccount.profile != null
                ? kakaoAccount.profile.nickname
                : null;
    }

    // profileImage 추출
    public String getProfileImageUrl() {
        return kakaoAccount != null && kakaoAccount.profile != null
                ? kakaoAccount.profile.profileImageUrl
                : null;
    }

    // email 추출 (nullable)
    public String getEmail() {
        return kakaoAccount != null ? kakaoAccount.email : null;
    }

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount {

        @JsonProperty("profile")
        private Profile profile;

        @JsonProperty("email")
        private String email;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Profile {
            @JsonProperty("nickname")
            private String nickname;

            @JsonProperty("profile_image_url")
            private String profileImageUrl;
        }
    }
}
