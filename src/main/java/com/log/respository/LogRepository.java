package com.log.respository;

import java.util.List;

import com.log.model.Log;
import com.log.utils.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogRepository {
    
    @Autowired
    private Config config;
    
    public List<Log> findAllLogs() {
        return null;
    }
}