package com.log.resourse;

import com.log.model.LogType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LogResponse {
    private LogType type;
    private String message;
    private String dateTime;
    private String threadId;
}