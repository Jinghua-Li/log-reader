package com.log.resourse;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.log.ControllerBaseTest;
import com.log.model.LogSearch;
import com.log.model.LogType;
import com.log.service.LogReaderService;
import com.log.utils.Constants;
import com.log.utils.DateFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(LogReaderController.class)
class LogReaderControllerTest extends ControllerBaseTest {
    
    @MockBean
    private LogReaderService logReaderService;
    
    private final String startDateStr = "2022-04-01 11:53:03";
    private final String endDateStr = "2022-04-01 15:53:03";
    
    private LogResponse logResponse;
    
    @BeforeEach
    void init() {
        logResponse = LogResponse.builder()
                .type(LogType.ERROR)
                .message("user login failed")
                .dateTime(startDateStr)
                .threadId("thread-1").build();
    }
    
    @Test
    void should_get_log_given_type_is_error_and_start_time_and_end_time_is_null() throws Exception {
        ArgumentCaptor<LogSearch> logSearchArgumentCaptor = ArgumentCaptor.forClass(LogSearch.class);
        doReturn(Collections.singletonList(logResponse)).when(logReaderService)
                .searchLog(logSearchArgumentCaptor.capture());
        
        this.mockMvc.perform(MockMvcRequestBuilders.get("/search?type=ERROR")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(Collections.singletonList(logResponse))));
        
        LogSearch logSearch = logSearchArgumentCaptor.getValue();
        assertEquals(Optional.of(LogType.ERROR), logSearch.getType());
        assertFalse(logSearch.getStartTime().isPresent());
        assertTrue(LocalDateTime.now().isAfter(logSearch.getEndTime()));
    }
    
    @Test
    void should_get_log_given_type_is_null_and_start_time_and_end_time_is_have_value() throws Exception {
        ArgumentCaptor<LogSearch> logSearchArgumentCaptor = ArgumentCaptor.forClass(LogSearch.class);
        doReturn(Collections.singletonList(logResponse)).when(logReaderService)
                .searchLog(logSearchArgumentCaptor.capture());
        
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/search?startTime=" + startDateStr + "&endTime=" + endDateStr)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(Collections.singletonList(logResponse))));
        
        LogSearch logSearch = logSearchArgumentCaptor.getValue();
        assertEquals(Optional.empty(), logSearch.getType());
        assertEquals(DateFormatter.convertToDate(Optional.of(startDateStr)), logSearch.getStartTime());
        assertEquals(DateFormatter.convertToDate(Optional.of(endDateStr)), Optional.of(logSearch.getEndTime()));
    }
    
    @Test
    void should_get_log_given_type_is_info_and_start_time_and_end_time_is_have_value() throws Exception {
        ArgumentCaptor<LogSearch> logSearchArgumentCaptor = ArgumentCaptor.forClass(LogSearch.class);
        doReturn(Collections.singletonList(logResponse)).when(logReaderService)
                .searchLog(logSearchArgumentCaptor.capture());
        
        this.mockMvc.perform(MockMvcRequestBuilders.get(
                                "/search?type=INFO&startTime=" + startDateStr + "&endTime=" + endDateStr)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(toJson(Collections.singletonList(logResponse))));
        
        LogSearch logSearch = logSearchArgumentCaptor.getValue();
        assertEquals(Optional.of(LogType.INFO), logSearch.getType());
        assertEquals(DateFormatter.convertToDate(Optional.of(startDateStr)), logSearch.getStartTime());
        assertEquals(DateFormatter.convertToDate(Optional.of(endDateStr)), Optional.of(logSearch.getEndTime()));
    }
    
    @Test
    void should_return_400_when_type_input_invalid() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/search?type=test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constants.ERROR_LOG_TYPE));
    }
    
    @Test
    void should_return_400_when_start_time_and_end_time_input_invalid() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/search?startTime=2022-04-0111:53:03&endTime=2022-04-01")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(Constants.ERROR_DATA_FORMATTER));
    }
}