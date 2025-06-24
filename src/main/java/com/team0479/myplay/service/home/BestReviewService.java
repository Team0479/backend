package com.team0479.myplay.service.home;

import com.team0479.myplay.dto.home.BestReviewDto;
import com.team0479.myplay.repository.home.BestReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BestReviewService {

    private final BestReviewRepository bestReviewRepository;

    public BestReviewService(BestReviewRepository bestReviewRepository) {
        this.bestReviewRepository = bestReviewRepository;
    }

    /**
     * 전체 베스트 리뷰 목록을 조회합니다.
     * 좋아요 수를 기준으로 내림차순 정렬합니다.
     * 
     * @return 베스트 리뷰 목록
     */
    public List<BestReviewDto> getBestReviews() {
        return bestReviewRepository.findBestReviews();
    }

    /**
     * 특정 장르의 베스트 리뷰 목록을 조회합니다.
     * 좋아요 수를 기준으로 내림차순 정렬합니다.
     * 
     * @param category 장르
     * @return 해당 장르의 베스트 리뷰 목록
     */
    public List<BestReviewDto> getBestReviewsByCategory(String category) {
        return bestReviewRepository.findBestReviewsByCategory(category);
    }

    /**
     * 뮤지컬 장르의 베스트 리뷰를 조회합니다.
     * 
     * @return 뮤지컬 베스트 리뷰 목록
     */
    public List<BestReviewDto> getBestMusicalReviews() {
        return getBestReviewsByCategory("뮤지컬");
    }

    /**
     * 콘서트 장르의 베스트 리뷰를 조회합니다.
     * 
     * @return 콘서트 베스트 리뷰 목록
     */
    public List<BestReviewDto> getBestConcertReviews() {
        return getBestReviewsByCategory("콘서트");
    }

    /**
     * 스포츠 장르의 베스트 리뷰를 조회합니다.
     * 
     * @return 스포츠 베스트 리뷰 목록
     */
    public List<BestReviewDto> getBestSportsReviews() {
        return getBestReviewsByCategory("스포츠");
    }

    /**
     * 전시/행사 장르의 베스트 리뷰를 조회합니다.
     * 
     * @return 전시/행사 베스트 리뷰 목록
     */
    public List<BestReviewDto> getBestExhibitionReviews() {
        return getBestReviewsByCategory("전시/행사");
    }
} 