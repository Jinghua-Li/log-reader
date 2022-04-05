package com.log.service;

import java.util.List;

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
        return null;
    }
}