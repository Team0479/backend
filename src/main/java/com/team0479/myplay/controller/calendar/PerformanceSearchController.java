package com.team0479.myplay.controller.calendar;

import com.team0479.myplay.dto.calendar.PerformanceSearchDto;
import com.team0479.myplay.service.calendar.PerformanceSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/calendar/performances")
@CrossOrigin(origins = "*")
public class PerformanceSearchController {

    private final PerformanceSearchService performanceSearchService;

    public PerformanceSearchController(PerformanceSearchService performanceSearchService) {
        this.performanceSearchService = performanceSearchService;
    }

    /**
     * 공연명으로 공연 검색 (자동완성용)
     * GET /api/calendar/performances/search?title={title}
     */
    @GetMapping("/search")
    public ResponseEntity<List<PerformanceSearchDto>> searchPerformancesByTitle(@RequestParam String title) {
        try {
            List<PerformanceSearchDto> performances = performanceSearchService.searchPerformancesByTitle(title);
            return ResponseEntity.ok(performances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 공연 ID로 공연 상세 정보 조회
     * GET /api/calendar/performances/{performanceId}
     */
    @GetMapping("/{performanceId}")
    public ResponseEntity<PerformanceSearchDto> getPerformanceById(@PathVariable Long performanceId) {
        try {
            PerformanceSearchDto performance = performanceSearchService.getPerformanceById(performanceId);
            return ResponseEntity.ok(performance);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 카테고리별 공연 목록 조회
     * GET /api/calendar/performances/category/{category}
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<PerformanceSearchDto>> getPerformancesByCategory(@PathVariable String category) {
        try {
            List<PerformanceSearchDto> performances = performanceSearchService.getPerformancesByCategory(category);
            return ResponseEntity.ok(performances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 최신 공연 목록 조회 (시작일 기준)
     * GET /api/calendar/performances/recent
     */
    @GetMapping("/recent")
    public ResponseEntity<List<PerformanceSearchDto>> getRecentPerformances() {
        try {
            List<PerformanceSearchDto> performances = performanceSearchService.getRecentPerformances();
            return ResponseEntity.ok(performances);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 