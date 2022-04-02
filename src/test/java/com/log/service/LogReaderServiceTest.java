package com.log.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;

import com.log.model.Log;
import com.log.model.LogSearch;
import com.log.model.LogType;
import com.log.resourse.LogResponse;
import com.log.respository.LogRepository;
import com.log.utils.DateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogReaderServiceTest {
    
    @InjectMocks
    private LogReaderService logReaderService;
    
    @Mock
    private LogRepository logRepository;
    
    private final String date = "2022-04-02 10:56:41";
    private final LocalDateTime dateTime = DateFormatter.convertToDate(Optional.of(date)).orElse(LocalDateTime.now());
    
    @BeforeEach
    void init() {
        Log logError = Log.builder().type(LogType.ERROR).dateTime(dateTime)
                .message("Completed initialization in 1 ms").threadId("Test worker1").build();
        Log logInfo = Log.builder().type(LogType.INFO).dateTime(dateTime.minusHours(2))
                .message("Started LogReaderControllerTest in 1.733 seconds").threadId("Test worker2").build();
        Log logDebug = Log.builder().type(LogType.DEBUG).dateTime(dateTime.minusHours(4))
                .message("No active profile set, falling back to 1 default").threadId("Test worker3").build();
        Log logWARNING =
                Log.builder().type(LogType.WARNING).dateTime(dateTime.minusHours(7))
                        .message("No active profile set, falling back to 1 default").threadId("Test worker4").build();
        Log logWARNING2 =
                Log.builder().type(LogType.WARNING).dateTime(dateTime.minusHours(9))
                        .message("No active profile set, falling back to 1 default").threadId("Test worker4").build();
        List<Log> logs = Arrays.asList(logDebug, logError, logInfo, logWARNING, logWARNING2);
        doReturn(logs).when(logRepository).findAllLogs();
    }
    
    @Test
    void should_get_all_logs_when_search_type_is_all_and_end_time_is_now() {
        LogSearch logSearch = LogSearch.builder()
                .type(Optional.empty())
                .endTime(LocalDateTime.now())
                .startTime(Optional.empty()).build();
        
        final List<LogResponse> logResponses = logReaderService.searchLog(logSearch);
        
        assertEquals(5, logResponses.size());
        assertEquals(LogType.ERROR, logResponses.get(0).getType());
        assertEquals(date, logResponses.get(0).getDateTime());
        assertEquals("Completed initialization in 1 ms", logResponses.get(0).getMessage());
        assertEquals("Test worker1", logResponses.get(0).getThreadId());
    }
    
    @Test
    void should_get_two_logs_when_search_type_is_all_and_start_end_time_have_value() {
        LogSearch logSearch = LogSearch.builder()
                .type(Optional.empty())
                .startTime(Optional.of(dateTime.minusHours(5)))
                .endTime(dateTime.minusHours(1))
                .build();
        
        final List<LogResponse> logResponses = logReaderService.searchLog(logSearch);
        
        assertEquals(2, logResponses.size());
        assertEquals(LogType.INFO, logResponses.get(0).getType());
        assertEquals("2022-04-02 08:56:41", logResponses.get(0).getDateTime());
        assertEquals("Started LogReaderControllerTest in 1.733 seconds", logResponses.get(0).getMessage());
        assertEquals("Test worker2", logResponses.get(0).getThreadId());
    }
    
    @Test
    void should_get_error_logs_when_search_type_is_error_and_end_time_is_now() {
        LogSearch logSearch = LogSearch.builder()
                .type(Optional.of(LogType.ERROR))
                .endTime(LocalDateTime.now())
                .startTime(Optional.empty()).build();
        
        final List<LogResponse> logResponses = logReaderService.searchLog(logSearch);
        
        assertEquals(1, logResponses.size());
        assertEquals(LogType.ERROR, logResponses.get(0).getType());
        assertEquals(date, logResponses.get(0).getDateTime());
        assertEquals("Completed initialization in 1 ms", logResponses.get(0).getMessage());
        assertEquals("Test worker1", logResponses.get(0).getThreadId());
    }
    
    @Test
    void should_get_one_logs_when_search_type_is_warning_and_start_end_time_have_value() {
        LogSearch logSearch = LogSearch.builder()
                .type(Optional.of(LogType.WARNING))
                .startTime(Optional.of(dateTime.minusHours(8)))
                .endTime(dateTime.minusHours(4))
                .build();
        
        final List<LogResponse> logResponses = logReaderService.searchLog(logSearch);
        
        assertEquals(1, logResponses.size());
        assertEquals(LogType.WARNING, logResponses.get(0).getType());
        assertEquals("2022-04-02 03:56:41", logResponses.get(0).getDateTime());
        assertEquals("No active profile set, falling back to 1 default", logResponses.get(0).getMessage());
        assertEquals("Test worker4", logResponses.get(0).getThreadId());
    }
}