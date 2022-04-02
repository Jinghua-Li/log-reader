package com.log.resourse;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import com.log.exception.BadRequestException;
import com.log.model.LogSearch;
import com.log.model.LogType;
import com.log.service.LogReaderService;
import com.log.utils.Constants;
import com.log.utils.DateFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogReaderController {
    
    @Autowired
    private LogReaderService logReaderService;
    
    @GetMapping("/search")
    public List<LogResponse> searchLog(@RequestParam(name = "type", defaultValue = "ALL") String type,
                                       @RequestParam(value = "startTime", required = false) String startTime,
                                       @RequestParam(value = "endTime", required = false) String endTime) {
        try {
            LogSearch logSearch = LogSearch.builder()
                    .type(LogType.valueOf(type))
                    .startTime(DateFormatter.convertToDate(startTime))
                    .endTime(DateFormatter.convertToDate(endTime).orElse(LocalDateTime.now()))
                    .build();
            return logReaderService.searchLog(logSearch);
        } catch (DateTimeParseException exception) {
            throw new BadRequestException(Constants.ERROR_DATA_FORMATTER);
        } catch (IllegalArgumentException exception) {
            throw new BadRequestException(Constants.ERROR_LOG_TYPE);
        }
    }
}