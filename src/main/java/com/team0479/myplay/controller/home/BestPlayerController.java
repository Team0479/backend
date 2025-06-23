package com.team0479.myplay.controller.home;

import com.team0479.myplay.dto.home.BestPlayerDto;
import com.team0479.myplay.service.home.BestPlayerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/best-players")
@CrossOrigin(origins = "*")
public class BestPlayerController {

    private final BestPlayerService bestPlayerService;

    public BestPlayerController(BestPlayerService bestPlayerService) {
        this.bestPlayerService = bestPlayerService;
    }

    /**
     * 베스트 플레이어 차트 조회 API
     * 리뷰 작성 수와 공연 관람 수를 합한 총 활동량을 기준으로 상위 플레이어들을 반환합니다.
     * 
     * @param limit 조회할 플레이어 수 (선택적, 기본값: 3)
     * @return 베스트 플레이어 목록
     */
    @GetMapping
    public ResponseEntity<List<BestPlayerDto>> getBestPlayers(
            @RequestParam(value = "limit", defaultValue = "3") int limit) {
        
        List<BestPlayerDto> bestPlayers = bestPlayerService.getBestPlayers(limit);
        return ResponseEntity.ok(bestPlayers);
    }

    /**
     * 상위 3명의 베스트 플레이어 조회 API
     * 프론트엔드 차트에서 사용할 상위 3명의 플레이어 정보를 반환합니다.
     * 
     * @return 상위 3명의 베스트 플레이어 목록
     */
    @GetMapping("/top3")
    public ResponseEntity<List<BestPlayerDto>> getTop3BestPlayers() {
        List<BestPlayerDto> top3Players = bestPlayerService.getTop3BestPlayers();
        return ResponseEntity.ok(top3Players);
    }
} 