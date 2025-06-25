package com.team0479.myplay.service.calendar;

import com.team0479.myplay.dto.calendar.CalendarEntryDto;
import com.team0479.myplay.dto.calendar.CalendarEntryCreateDto;
import com.team0479.myplay.dto.calendar.UserExpDto;
import com.team0479.myplay.repository.calendar.CalendarEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CalendarEntryService {

    private final CalendarEntryRepository calendarEntryRepository;

    public CalendarEntryService(CalendarEntryRepository calendarEntryRepository) {
        this.calendarEntryRepository = calendarEntryRepository;
    }

    /**
     * 특정 사용자의 특정 날짜 일정 조회
     */
    @Transactional(readOnly = true)
    public List<CalendarEntryDto> getCalendarEntriesByUserAndDate(Long userId, LocalDate date) {
        return calendarEntryRepository.findByUserIdAndDate(userId, date);
    }

    /**
     * 특정 사용자의 모든 일정 조회
     */
    @Transactional(readOnly = true)
    public List<CalendarEntryDto> getCalendarEntriesByUser(Long userId) {
        return calendarEntryRepository.findByUserId(userId);
    }

    /**
     * 새로운 일정 등록
     */
    public CalendarEntryDto createCalendarEntry(CalendarEntryCreateDto createDto) {
        // 일정 등록
        Long entryId = calendarEntryRepository.createCalendarEntry(createDto);
        
        // 경험치 증가 및 레벨 체크 (일정 등록 시 10 경험치 획득)
        updateUserExperienceAndLevel(createDto.getUserId(), 10);
        
        // 등록된 일정 정보 반환
        return calendarEntryRepository.findById(entryId);
    }

    /**
     * 일정 수정
     */
    public CalendarEntryDto updateCalendarEntry(Long entryId, CalendarEntryCreateDto updateDto) {
        // 일정이 존재하는지 확인
        CalendarEntryDto existingEntry = calendarEntryRepository.findById(entryId);
        if (existingEntry == null) {
            throw new RuntimeException("Calendar entry not found with id: " + entryId);
        }

        // 일정 수정
        calendarEntryRepository.updateCalendarEntry(entryId, updateDto);
        
        // 수정된 일정 정보 반환
        return calendarEntryRepository.findById(entryId);
    }

    /**
     * 일정 삭제
     */
    public void deleteCalendarEntry(Long entryId) {
        // 일정이 존재하는지 확인
        CalendarEntryDto existingEntry = calendarEntryRepository.findById(entryId);
        if (existingEntry == null) {
            throw new RuntimeException("Calendar entry not found with id: " + entryId);
        }

        // 일정 삭제
        calendarEntryRepository.deleteById(entryId);
    }

    /**
     * 특정 일정 조회
     */
    @Transactional(readOnly = true)
    public CalendarEntryDto getCalendarEntry(Long entryId) {
        CalendarEntryDto entry = calendarEntryRepository.findById(entryId);
        if (entry == null) {
            throw new RuntimeException("Calendar entry not found with id: " + entryId);
        }
        return entry;
    }

    /**
     * 사용자 경험치 및 레벨 업데이트
     */
    private void updateUserExperienceAndLevel(Long userId, int expGain) {
        // 현재 경험치와 레벨 조회
        int currentExp = calendarEntryRepository.getUserCurrentExp(userId);
        int currentLevel = calendarEntryRepository.getUserCurrentLevel(userId);
        
        // 경험치 증가
        calendarEntryRepository.updateUserExperience(userId, expGain);
        int newExp = currentExp + expGain;
        
        // 레벨 업 체크
        checkAndUpdateLevel(userId, newExp, currentLevel);
    }

    /**
     * 레벨 업 체크 및 업데이트
     */
    private void checkAndUpdateLevel(Long userId, int currentExp, int currentLevel) {
        try {
            // 다음 레벨에 필요한 경험치 조회
            int nextLevelRequiredExp = calendarEntryRepository.getRequiredExpForLevel(currentLevel + 1);
            
            // 레벨 업 조건 체크
            if (nextLevelRequiredExp > 0 && currentExp >= nextLevelRequiredExp) {
                // 레벨 업!
                calendarEntryRepository.updateUserLevel(userId, currentLevel + 1);
                
                // 추가 레벨 업이 가능한지 재귀 체크
                checkAndUpdateLevel(userId, currentExp, currentLevel + 1);
            }
        } catch (Exception e) {
            // level_requirement 테이블에 다음 레벨 데이터가 없는 경우 (최고 레벨 도달)
            // 레벨 업 중단
        }
    }

    /**
     * 사용자 경험치 정보 조회
     */
    @Transactional(readOnly = true)
    public UserExpDto getUserExpInfo(Long userId) {
        int currentExp = calendarEntryRepository.getUserCurrentExp(userId);
        int currentLevel = calendarEntryRepository.getUserCurrentLevel(userId);
        
        int expToNextLevel = 0;
        try {
            int nextLevelRequiredExp = calendarEntryRepository.getRequiredExpForLevel(currentLevel + 1);
            expToNextLevel = Math.max(0, nextLevelRequiredExp - currentExp);
        } catch (Exception e) {
            // 최고 레벨 도달
            expToNextLevel = 0;
        }
        
        return new UserExpDto(userId, currentExp, currentLevel, expToNextLevel, 0);
    }
} 