package com.log.utils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Matcher;

import com.log.model.LogType;

public class LogFileParseUtils {
    public static Optional<LocalDateTime> extractDateTime(String logStr) {
        final Matcher matcher = Constants.DATE_PATTERN.matcher(logStr);
        if (matcher.find()) {
            return DateFormatter.convertToDate(Optional.of(matcher.group(0)));
        }
        return Optional.empty();
    }
    
    public static Optional<LogType> extractLogType(String logStr) {
        final Matcher matcher = Constants.TYPE_PATTERN.matcher(logStr);
        if (matcher.find()) {
            return Optional.of(LogType.valueOf(matcher.group(0)));
        }
        return Optional.empty();
    }
    
    public static Optional<String> extractThreadId(String logStr) {
        final Matcher matcher = Constants.THREAD_PATTERN.matcher(logStr);
        if (matcher.find()) {
            return Optional.of(matcher.group(1).trim());
        }
        return Optional.empty();
    }
    
    public static String extractMessage(String logStr) {
        final Matcher matcher = Constants.TYPE_PATTERN.matcher(logStr);
        if (matcher.find()) {
            return logStr.substring(matcher.end()).trim();
        }
        return logStr.trim();
    }
}