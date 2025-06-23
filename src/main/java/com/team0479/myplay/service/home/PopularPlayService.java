package com.team0479.myplay.service.home;

import com.team0479.myplay.dto.home.PopularPlayDto;
import com.team0479.myplay.repository.home.PopularPlayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PopularPlayService {

    private final PopularPlayRepository popularPlayRepository;

    public PopularPlayService(PopularPlayRepository popularPlayRepository) {
        this.popularPlayRepository = popularPlayRepository;
    }

    /**
     * 특정 장르의 인기 플레이 목록을 조회합니다.
     * performance 테이블의 ranking 필드를 기준으로 정렬합니다.
     * 
     * @param category 장르
     * @return 인기 플레이 목록
     */
    public List<PopularPlayDto> getPopularPlaysByCategory(String category) {
        return popularPlayRepository.findPopularPlaysByCategory(category);
    }

    /**
     * 뮤지컬 장르의 인기 플레이를 조회합니다.
     * 
     * @return 뮤지컬 인기 플레이 목록
     */
    public List<PopularPlayDto> getPopularMusicals() {
        return getPopularPlaysByCategory("뮤지컬");
    }

    /**
     * 콘서트 장르의 인기 플레이를 조회합니다.
     * 
     * @return 콘서트 인기 플레이 목록
     */
    public List<PopularPlayDto> getPopularConcerts() {
        return getPopularPlaysByCategory("콘서트");
    }

    /**
     * 스포츠 장르의 인기 플레이를 조회합니다.
     * 
     * @return 스포츠 인기 플레이 목록
     */
    public List<PopularPlayDto> getPopularSports() {
        return getPopularPlaysByCategory("스포츠");
    }

    /**
     * 전시/행사 장르의 인기 플레이를 조회합니다.
     * 
     * @return 전시/행사 인기 플레이 목록
     */
    public List<PopularPlayDto> getPopularExhibitions() {
        return getPopularPlaysByCategory("전시/행사");
    }
} 