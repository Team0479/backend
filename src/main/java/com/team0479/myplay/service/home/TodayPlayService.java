package com.team0479.myplay.service.home;

import com.team0479.myplay.dto.home.TodayPlayDto;
import com.team0479.myplay.repository.home.TodayPlayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodayPlayService {

    private final TodayPlayRepository todayPlayRepository;

    public TodayPlayService(TodayPlayRepository todayPlayRepository) {
        this.todayPlayRepository = todayPlayRepository;
    }

    /**
     * 특정 유저를 위한 오늘의 플레이 추천 목록을 조회합니다.
     * 유저가 calendar에 가장 많이 등록한 장르를 기반으로 추천합니다.
     * 
     * @param userId 유저 ID
     * @param limit 조회할 개수 (기본값: 4개)
     * @return 오늘의 플레이 추천 목록
     */
    public List<TodayPlayDto> getTodayPlaysForUser(Long userId, int limit) {
        if (limit <= 0) {
            limit = 4; // 기본값 4개
        }

        // 1. 유저가 가장 많이 등록한 장르 조회
        String mostPreferredCategory = todayPlayRepository.findMostRegisteredCategoryByUser(userId);
        
        // 2. 해당 장르에 등록한 개수 조회
        Integer categoryCount = todayPlayRepository.getCategoryCountByUser(userId, mostPreferredCategory);
        
        // 3. 해당 장르의 추천 공연 조회
        List<TodayPlayDto> recommendedPlays = todayPlayRepository.findRecommendedPlaysByCategory(mostPreferredCategory, limit);
        
        // 4. 추천 이유 설정
        String recommendReason = generateRecommendReason(mostPreferredCategory, categoryCount);
        for (TodayPlayDto play : recommendedPlays) {
            play.setRecommendReason(recommendReason);
        }
        
        return recommendedPlays;
    }

    /**
     * 기본 오늘의 플레이 추천 목록을 조회합니다. (유저 ID 없이)
     * 
     * @param limit 조회할 개수
     * @return 기본 추천 목록 (뮤지컬 장르 기준)
     */
    public List<TodayPlayDto> getDefaultTodayPlays(int limit) {
        if (limit <= 0) {
            limit = 4;
        }
        
        List<TodayPlayDto> defaultPlays = todayPlayRepository.findRecommendedPlaysByCategory("뮤지컬", limit);
        
        // 기본 추천 이유 설정
        for (TodayPlayDto play : defaultPlays) {
            play.setRecommendReason("인기 있는 뮤지컬을 추천드려요!");
        }
        
        return defaultPlays;
    }

    /**
     * 추천 이유 메시지를 생성합니다.
     * 
     * @param category 장르
     * @param count 등록 개수
     * @return 추천 이유 메시지
     */
    private String generateRecommendReason(String category, Integer count) {
        if (count == null || count == 0) {
            return category + " 공연을 추천드려요!";
        }
        
        return String.format("%s을 %d번 관람하셨어요! 더 많은 %s을 추천드려요!", 
                            category, count, category);
    }
} 