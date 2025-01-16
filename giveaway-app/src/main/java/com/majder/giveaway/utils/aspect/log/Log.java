package com.majder.giveaway.utils.aspect.log;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class Log {

    private String id;

    private String message;

    private LocalDateTime timestamp;

    private LogType logType;

    private String component;

    private List<String> methodParams;

    private String username;


    public Log() {
        this.methodParams = new ArrayList<>();
    }

    public void addMethodParam(String param) {
        this.methodParams.add(param);
    }
}