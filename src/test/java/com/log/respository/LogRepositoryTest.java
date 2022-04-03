package com.log.respository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import com.log.model.Log;
import com.log.model.LogType;
import com.log.utils.Config;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LogRepositoryTest {
    
    @InjectMocks
    private LogRepository logRepository;
    
    @Spy
    private Config config;
    
    @BeforeEach
    void init() {
        config.setLocation("classpath:log-test.txt");
    }
    
    @Test
    void should_can_parse_all_log_file_to_log_objects() {
        final List<Log> allLogs = logRepository.findAllLogs();
        
        assertEquals(25, allLogs.size());
        assertEquals(LocalDateTime.of(2022, 4, 1, 18, 1, 28), allLogs.get(0).getDateTime());
        assertEquals("Test worker", allLogs.get(0).getThreadId());
        assertEquals(LogType.WARNING, allLogs.get(0).getType());
        assertNotNull(allLogs.get(0).getMessage());
    }
    
    @Test
    void should_can_parse_multi_error_lin_file_to_log_objects() {
        final Optional<Log>
                logOptional = logRepository.findAllLogs().stream().filter(log -> log.getType() == LogType.ERROR).findFirst();
        
        assertTrue(logOptional.isPresent());
        assertEquals("reactor.core.publisher.Operators         : Operator called default onErrorDropped\n" +
                "org.springframework.dao.DataAccessResourceFailureException: Unable to acquire JDBC Connection; nested exception is org.hibernate.exception.JDBCConnectionException: Unable to acquire JDBC Connection\n" +
                "\tat org.springframework.orm.jpa.vendor.HibernateJpaDialect.convertHibernateAccessException(HibernateJpaDialect.java:277) ~[spring-orm-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]\n" +
                "\tat org.springframework.orm.jpa.vendor.HibernateJpaDialect.translateExceptionIfPossible(HibernateJpaDialect.java:255) ~[spring-orm-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]\n" +
                "\tat org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.translateExceptionIfPossible(AbstractEntityManagerFactoryBean.java:528) ~[spring-orm-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]\n" +
                "\tat org.springframework.dao.support.ChainedPersistenceExceptionTranslator.translateExceptionIfPossible(ChainedPersistenceExceptionTranslator.java:61) ~[spring-tx-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]\n" +
                "\tat org.springframework.dao.support.DataAccessUtils.translateIfNecessary(DataAccessUtils.java:242) ~[spring-tx-5.2.11.RELEASE.jar!/:5.2.11.RELEASE]", logOptional.get().getMessage());
    }
    
    @Test
    void should_throw_file_parse_exception_when_file_do_not_existing() {
        config.setLocation("classpath:logs.txt");
        assertThrows(FileParseException.class, () -> logRepository.findAllLogs());
    }
    
    @Test
    void should_throw_file_parse_exception_when_log_type_do_not_existing() {
        config.setLocation("classpath:log-test-error.txt");
        assertThrows(FileParseException.class, () -> logRepository.findAllLogs());
    }
}