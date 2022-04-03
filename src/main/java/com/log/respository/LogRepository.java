package com.log.respository;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.log.model.Log;
import com.log.model.LogType;
import com.log.utils.Config;
import com.log.utils.LogFileParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class LogRepository {
    
    @Autowired
    private Config config;
    
    public List<Log> findAllLogs() {
        final File file = getLogFile();
        try (final Stream<String> lines = Files.lines(Paths.get(file.getPath()))) {
            List<Log> logs = new ArrayList<>();
            lines.forEach(line -> generateLogs(logs, line));
            return logs;
        } catch (IOException e) {
            throw new FileParseException(e.getMessage());
        }
    }
    
    private static void generateLogs(List<Log> logs, String line) {
        if (LogFileParseUtils.extractDateTime(line).isPresent()) {
            Log log = Log.builder().build();
            log.setDateTime(LogFileParseUtils.extractDateTime(line).get());
            log.setMessage(LogFileParseUtils.extractMessage(line));
            log.setType(LogFileParseUtils.extractLogType(line)
                    .orElseThrow(() -> new FileParseException("Log type do not exist")));
            log.setThreadId(LogFileParseUtils.extractThreadId(line)
                    .orElseThrow(() -> new FileParseException("Thread id do not exist")));
            logs.add(log);
            return;
        }
        Log lastLog = logs.get(logs.size() - 1);
        if (lastLog.getType() == LogType.ERROR) {
            lastLog.setMessage(lastLog.getMessage() + "\n" + line);
        }
    }
    
    private File getLogFile() {
        try {
            return ResourceUtils.getFile(config.getLocation());
        } catch (FileNotFoundException e) {
            throw new FileParseException(e.getMessage());
        }
    }
}