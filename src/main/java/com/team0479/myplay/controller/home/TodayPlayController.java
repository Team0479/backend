package com.team0479.myplay.controller.home;

import com.team0479.myplay.dto.home.TodayPlayDto;
import com.team0479.myplay.service.home.TodayPlayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/today-plays")
@CrossOrigin(origins = "*")
public class TodayPlayController {

    private final TodayPlayService todayPlayService;

    public TodayPlayController(TodayPlayService todayPlayService) {
        this.todayPlayService = todayPlayService;
    }

    /**
     * 특정 유저를 위한 오늘의 플레이 추천 API
     * 유저가 calendar에 가장 많이 등록한 장르를 기반으로 추천합니다.
     * 
     * @param userId 유저 ID
     * @param limit 조회할 개수 (선택적, 기본값: 4)
     * @return 오늘의 플레이 추천 목록
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TodayPlayDto>> getTodayPlaysForUser(
            @PathVariable Long userId,
            @RequestParam(value = "limit", defaultValue = "4") int limit) {
        
        List<TodayPlayDto> todayPlays = todayPlayService.getTodayPlaysForUser(userId, limit);
        return ResponseEntity.ok(todayPlays);
    }

    /**
     * 기본 오늘의 플레이 추천 API
     * 유저 정보 없이 기본 추천 목록을 반환합니다.
     * 
     * @param limit 조회할 개수 (선택적, 기본값: 4)
     * @return 기본 추천 목록
     */
    @GetMapping
    public ResponseEntity<List<TodayPlayDto>> getDefaultTodayPlays(
            @RequestParam(value = "limit", defaultValue = "4") int limit) {
        
        List<TodayPlayDto> todayPlays = todayPlayService.getDefaultTodayPlays(limit);
        return ResponseEntity.ok(todayPlays);
    }
} 