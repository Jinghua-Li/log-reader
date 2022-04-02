package com.log.service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.log.model.Log;
import com.log.model.LogSearch;
import com.log.resourse.LogResponse;
import com.log.respository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogReaderService {
    
    @Autowired
    private LogRepository logRepository;
    
    public List<LogResponse> searchLog(LogSearch search) {
        return logRepository.findAllLogs().stream()
                .filter(log -> log.getDateTime().isBefore(search.getEndTime()))
                .filter(log -> search.getStartTime().map(startTime -> log.getDateTime().isAfter(startTime)).orElse(true))
                .filter(log -> search.getType().map(type -> type == log.getType()).orElse(true))
                .sorted(Comparator.comparing(Log::getDateTime).reversed())
                .map(Log::toLogResponse).collect(Collectors.toList());
    }
}