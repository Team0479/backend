package com.team0479.myplay.controller.calendar;

import com.team0479.myplay.dto.calendar.CalendarEntryDto;
import com.team0479.myplay.dto.calendar.CalendarEntryCreateDto;
import com.team0479.myplay.service.calendar.CalendarEntryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/calendar")
@CrossOrigin(origins = "*")
public class CalendarEntryController {

    private final CalendarEntryService calendarEntryService;

    public CalendarEntryController(CalendarEntryService calendarEntryService) {
        this.calendarEntryService = calendarEntryService;
    }

    /**
     * 특정 사용자의 특정 날짜 일정 조회
     * GET /api/calendar/entries/user/{userId}/date/{date}
     */
    @GetMapping("/entries/user/{userId}/date/{date}")
    public ResponseEntity<List<CalendarEntryDto>> getCalendarEntriesByUserAndDate(
            @PathVariable Long userId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<CalendarEntryDto> entries = calendarEntryService.getCalendarEntriesByUserAndDate(userId, date);
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 특정 사용자의 모든 일정 조회
     * GET /api/calendar/entries/user/{userId}
     */
    @GetMapping("/entries/user/{userId}")
    public ResponseEntity<List<CalendarEntryDto>> getCalendarEntriesByUser(@PathVariable Long userId) {
        try {
            List<CalendarEntryDto> entries = calendarEntryService.getCalendarEntriesByUser(userId);
            return ResponseEntity.ok(entries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 새로운 일정 등록
     * POST /api/calendar/entries
     */
    @PostMapping("/entries")
    public ResponseEntity<CalendarEntryDto> createCalendarEntry(@RequestBody CalendarEntryCreateDto createDto) {
        try {
            CalendarEntryDto createdEntry = calendarEntryService.createCalendarEntry(createDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEntry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * 일정 수정
     * PUT /api/calendar/entries/{entryId}
     */
    @PutMapping("/entries/{entryId}")
    public ResponseEntity<CalendarEntryDto> updateCalendarEntry(
            @PathVariable Long entryId,
            @RequestBody CalendarEntryCreateDto updateDto) {
        try {
            CalendarEntryDto updatedEntry = calendarEntryService.updateCalendarEntry(entryId, updateDto);
            return ResponseEntity.ok(updatedEntry);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * 일정 삭제
     * DELETE /api/calendar/entries/{entryId}
     */
    @DeleteMapping("/entries/{entryId}")
    public ResponseEntity<Map<String, String>> deleteCalendarEntry(@PathVariable Long entryId) {
        try {
            calendarEntryService.deleteCalendarEntry(entryId);
            return ResponseEntity.ok(Map.of("message", "Calendar entry deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Calendar entry not found"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete calendar entry"));
        }
    }

    /**
     * 특정 일정 상세 조회
     * GET /api/calendar/entries/{entryId}
     */
    @GetMapping("/entries/{entryId}")
    public ResponseEntity<CalendarEntryDto> getCalendarEntry(@PathVariable Long entryId) {
        try {
            CalendarEntryDto entry = calendarEntryService.getCalendarEntry(entryId);
            return ResponseEntity.ok(entry);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
} 