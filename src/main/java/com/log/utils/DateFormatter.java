package com.log.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

public class DateFormatter {
    
    public static Optional<LocalDateTime> convertToDate(String dateStr) throws
            DateTimeParseException {
        return Optional.ofNullable(dateStr)
                .map(time -> LocalDateTime.parse(time, DateTimeFormatter.ofPattern(Constants.DATE_FORMATTER)));
    }
}