package com.majder.giveaway.utils.aspect.log;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LogService {

    public void info(String message, String component, String username, List<String> methodParams) {
        this.saveLog(message, component, LogType.INFO, username, methodParams);

    }

    public void error( String message, String component, String username, List<String> methodParams) {
        this.saveLog(message, component, LogType.ERROR, username, methodParams);
    }

    public void debug(String message, String component, String username, List<String> methodParams) {
        this.saveLog(message, component, LogType.DEBUG, username, methodParams);

    }

    public void warn(String message, String component, String username, List<String> methodParams) {
        this.saveLog(message, component, LogType.WARN, username, methodParams);
    }

    private void saveLog(String message, String component, LogType type, String username, List<String> methodParams) {
        var logModel = new Log();
        logModel.setLogType(type);
        logModel.setTimestamp(LocalDateTime.now());
        logModel.setMessage(message);
        logModel.setComponent(component);
        logModel.setMethodParams(methodParams);
        logModel.setUsername(username);
        System.out.println(logModel);
        log.info("{}", logModel);
    }
}
