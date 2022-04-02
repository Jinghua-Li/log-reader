package com.log.utils;

import java.util.regex.Pattern;

public class Constants {
    public static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    public static final String ERROR_DATA_FORMATTER = "日期格式错误，请输入正确的日期时间，例如：2022-04-01 11:53:03";
    public static final String ERROR_LOG_TYPE = "日志类型输入错误，请输入正确的日志类型：ERROR, INFO, WARNING, DEBUG, ALL";
    public static final Pattern DATE_PATTERN = Pattern.compile("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}");
    public static final Pattern TYPE_PATTERN = Pattern.compile("ERROR|INFO|WARNING|DEBUG");
    public static final Pattern THREAD_PATTERN = Pattern.compile("\\[(.*?)]");
}