package com.log.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.log.resourse.LogResponse;
import com.log.utils.Constants;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    private LogType type;
    private String message;
    private LocalDateTime dateTime;
    private String threadId;
    
    public LogResponse toLogResponse() {
        return LogResponse.builder().type(this.type).message(this.message).dateTime(this.dateTime.format(
                DateTimeFormatter.ofPattern(Constants.DATE_FORMATTER))).threadId(this.threadId).build();
    }
}