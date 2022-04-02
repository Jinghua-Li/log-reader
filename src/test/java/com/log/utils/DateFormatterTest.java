package com.log.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DateFormatterTest {
    
    @Test
    void should_covert_to_local_date_time_given_date_string() {
        String dateStr = "2022-04-01 11:53:03";
        
        Optional<LocalDateTime> result = DateFormatter.convertToDate(Optional.of(dateStr));
        
        final Optional<LocalDateTime> except = Optional.of(LocalDateTime.of(2022, 4, 1, 11, 53, 3));
        assertEquals(except, result);
    }
    
    @Test
    void should_throw_exception_given_invalid_date_string() {
        Optional<String> dateStr = Optional.of("2022-04-01211:53:03");
        assertThrows(DateTimeParseException.class, () -> DateFormatter.convertToDate(dateStr));
    }
}