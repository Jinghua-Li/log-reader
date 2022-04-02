package com.log.model;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class LogSearch {
    private Optional<LogType> type;
    private Optional<LocalDateTime> startTime;
    private LocalDateTime endTime;
}