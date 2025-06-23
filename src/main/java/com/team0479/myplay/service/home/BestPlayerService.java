package com.team0479.myplay.service.home;

import com.team0479.myplay.dto.home.BestPlayerDto;
import com.team0479.myplay.repository.home.BestPlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestPlayerService {

    private final BestPlayerRepository bestPlayerRepository;

    public BestPlayerService(BestPlayerRepository bestPlayerRepository) {
        this.bestPlayerRepository = bestPlayerRepository;
    }

    /**
     * 베스트 플레이어 목록을 조회합니다.
     * 리뷰 작성 수와 공연 관람 수(calendar_entry)를 합한 총 활동량을 기준으로 정렬합니다.
     * 
     * @param limit 조회할 플레이어 수 (기본값: 3명)
     * @return 베스트 플레이어 목록
     */
    public List<BestPlayerDto> getBestPlayers(int limit) {
        if (limit <= 0) {
            limit = 3; // 기본값 설정
        }
        return bestPlayerRepository.findBestPlayers(limit);
    }

    /**
     * 상위 3명의 베스트 플레이어를 조회합니다.
     * 
     * @return 상위 3명의 베스트 플레이어 목록
     */
    public List<BestPlayerDto> getTop3BestPlayers() {
        return getBestPlayers(3);
    }
} 