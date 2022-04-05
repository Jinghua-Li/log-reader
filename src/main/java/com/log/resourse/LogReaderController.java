package com.log.resourse;

import java.util.List;
import java.util.Optional;

import com.log.service.LogReaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogReaderController {
    
    @Autowired
    private LogReaderService logReaderService;
    
    @GetMapping("/search")
    public List<LogResponse> searchLog(@RequestParam(name = "type", required = false) Optional<String> type,
                                       @RequestParam(value = "startTime", required = false) Optional<String> startTime,
                                       @RequestParam(value = "endTime", required = false) Optional<String> endTime) {
        return null;
    }
}