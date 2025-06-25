package com.team0479.myplay.service.calendar;

import com.team0479.myplay.dto.calendar.PerformanceSearchDto;
import com.team0479.myplay.repository.calendar.PerformanceSearchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PerformanceSearchService {

    private final PerformanceSearchRepository performanceSearchRepository;

    public PerformanceSearchService(PerformanceSearchRepository performanceSearchRepository) {
        this.performanceSearchRepository = performanceSearchRepository;
    }

    /**
     * 공연명으로 공연 검색 (자동완성용)
     */
    public List<PerformanceSearchDto> searchPerformancesByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }
        return performanceSearchRepository.searchPerformancesByTitle(title.trim());
    }

    /**
     * 공연 ID로 공연 상세 정보 조회
     */
    public PerformanceSearchDto getPerformanceById(Long performanceId) {
        PerformanceSearchDto performance = performanceSearchRepository.findPerformanceById(performanceId);
        if (performance == null) {
            throw new RuntimeException("Performance not found with id: " + performanceId);
        }
        return performance;
    }

    /**
     * 카테고리별 공연 목록 조회
     */
    public List<PerformanceSearchDto> getPerformancesByCategory(String category) {
        return performanceSearchRepository.searchPerformancesByCategory(category);
    }

    /**
     * 최신 공연 목록 조회 (시작일 기준)
     */
    public List<PerformanceSearchDto> getRecentPerformances() {
        return performanceSearchRepository.getRecentPerformances();
    }
} 