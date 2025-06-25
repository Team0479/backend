package com.team0479.myplay.service.calendar;

import com.team0479.myplay.dto.calendar.CalendarEntryDto;
import com.team0479.myplay.dto.calendar.CalendarEntryCreateDto;
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
} 