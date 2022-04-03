package com.log.utils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.log.model.LogType;
import org.junit.jupiter.api.Test;

class LogFileParseUtilsTest {
    
    private final String normalLogStr =
            "2022-04-02 18:01:30.253 [Test worker] INFO o.s.t.web.servlet.TestDispatcherServlet  : Completed initialization in 1 ms";
    private final String otherLogStr =
            "org.springframework.dao.DataAccessResourceFailureException: Unable to acquire JDBC Connection; nested exception is org.hibernate.exception.JDBCConnectionException:";
    
    @Test
    void should_can_parse_date_time_given_log_line_contains_date_time() {
        Optional<LocalDateTime> dateTime = LogFileParseUtils.extractDateTime(normalLogStr);
        
        assertEquals(Optional.of(LocalDateTime.of(2022, 4, 2, 18, 1, 30)), dateTime);
    }
    
    @Test
    void should_get_parse_date_time_is_null_given_log_line_do_not_contains_date_time() {
        Optional<LocalDateTime> dateTime = LogFileParseUtils.extractDateTime(otherLogStr);
        
        assertEquals(Optional.empty(), dateTime);
    }
    
    @Test
    void should_can_parse_log_type_given_log_line_contains_type() {
        Optional<LogType> type = LogFileParseUtils.extractLogType(normalLogStr);
        
        assertEquals(Optional.of(LogType.INFO), type);
    }
    
    @Test
    void should_get_parse_log_type_is_null_given_log_line_do_not_contains_type() {
        Optional<LogType> type = LogFileParseUtils.extractLogType(otherLogStr);
        
        assertEquals(Optional.empty(), type);
    }
    
    @Test
    void should_can_parse_thread_id_given_log_line_contains_thread() {
        Optional<String> threadId = LogFileParseUtils.extractThreadId(normalLogStr);
        
        assertEquals(Optional.of("Test worker"), threadId);
    }
    
    @Test
    void should_get_parse_thread_id_is_null_given_log_line_do_not_contains_thread() {
        Optional<String> threadId = LogFileParseUtils.extractThreadId(otherLogStr);
        
        assertEquals(Optional.empty(), threadId);
    }
    
    @Test
    void should_can_parse_content_given_log_line_contains_colon() {
        String message = LogFileParseUtils.extractMessage(normalLogStr);
        
        assertEquals("o.s.t.web.servlet.TestDispatcherServlet  : Completed initialization in 1 ms", message);
    }
    
    @Test
    void should_get_parse_content_given_log_line_do_not_contains_colon() {
        String message = LogFileParseUtils.extractMessage("Unable to acquire JDBC Connection; nested exception is org.hibernate.exception.JDBCConnectionException");
        
        assertEquals("Unable to acquire JDBC Connection; nested exception is org.hibernate.exception.JDBCConnectionException", message);
    }
}