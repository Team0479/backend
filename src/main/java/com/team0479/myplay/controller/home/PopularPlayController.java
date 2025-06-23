package com.team0479.myplay.controller.home;

import com.team0479.myplay.dto.home.PopularPlayDto;
import com.team0479.myplay.service.home.PopularPlayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/popular-plays")
@CrossOrigin(origins = "*")
public class PopularPlayController {

    private final PopularPlayService popularPlayService;

    public PopularPlayController(PopularPlayService popularPlayService) {
        this.popularPlayService = popularPlayService;
    }

    /**
     * 특정 장르의 인기 플레이 조회 API
     * ranking 필드를 기준으로 정렬하여 반환합니다.
     * 
     * @param category 장르 (뮤지컬, 콘서트, 스포츠, 전시/행사)
     * @return 해당 장르의 인기 플레이 목록
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<PopularPlayDto>> getPopularPlaysByCategory(
            @PathVariable String category) {
        
        List<PopularPlayDto> popularPlays = popularPlayService.getPopularPlaysByCategory(category);
        return ResponseEntity.ok(popularPlays);
    }

    /**
     * 뮤지컬 인기 플레이 조회 API
     * 
     * @return 뮤지컬 인기 플레이 목록
     */
    @GetMapping("/musicals")
    public ResponseEntity<List<PopularPlayDto>> getPopularMusicals() {
        List<PopularPlayDto> musicals = popularPlayService.getPopularMusicals();
        return ResponseEntity.ok(musicals);
    }

    /**
     * 콘서트 인기 플레이 조회 API
     * 
     * @return 콘서트 인기 플레이 목록
     */
    @GetMapping("/concerts")
    public ResponseEntity<List<PopularPlayDto>> getPopularConcerts() {
        List<PopularPlayDto> concerts = popularPlayService.getPopularConcerts();
        return ResponseEntity.ok(concerts);
    }

    /**
     * 스포츠 인기 플레이 조회 API
     * 
     * @return 스포츠 인기 플레이 목록
     */
    @GetMapping("/sports")
    public ResponseEntity<List<PopularPlayDto>> getPopularSports() {
        List<PopularPlayDto> sports = popularPlayService.getPopularSports();
        return ResponseEntity.ok(sports);
    }

    /**
     * 전시/행사 인기 플레이 조회 API
     * 
     * @return 전시/행사 인기 플레이 목록
     */
    @GetMapping("/exhibitions")
    public ResponseEntity<List<PopularPlayDto>> getPopularExhibitions() {
        List<PopularPlayDto> exhibitions = popularPlayService.getPopularExhibitions();
        return ResponseEntity.ok(exhibitions);
    }
} 