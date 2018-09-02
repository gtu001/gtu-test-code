package gtu.log;

import java.lang.management.ManagementFactory;

import org.apache.log4j.MDC;

public class LoggerShowCustomInfo {

    public static void main(String[] args) {
        MDC.put("PID", ManagementFactory.getRuntimeMXBean().getName().replaceAll("@.*", ""));
        System.out.println(ManagementFactory.getRuntimeMXBean().getName());

        // 取得 Process ID 供 Log4j 使用
        System.out.println(MDC.get("PID"));
    }

}

/*
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">

    <appender name="CONSOLE" class="org.apache.log4j.ConsoleAppender">
        <param name="Encoding" value="utf-8" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern"
                value="%d{yyyy-MM-dd HH:mm:ss}[%-5p][%X{PID}][%c{1}:%L] %m%n" />  <-------%X{PID} 可特別顯示
        </layout>
    </appender>

    <appender name="FILE" class="org.apache.log4j.DailyRollingFileAppender">
        <param name="File" value="logs/dcshp.log" />
        <param name="Append" value="true" />
        <param name="DatePattern" value="'.'yyyy-MM-dd" />
        <param name="Encoding" value="utf-8" />
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern"
                value="%d{yyyy-MM-dd HH:mm:ss}[%-5p][%X{PID}][%c{1}:%L] %m%n" />
        </layout>
    </appender>
    <!-- <logger name="org.springframework.jdbc" level="DEBUG" /> <logger 
        name="java.sql.Connection" level="DEBUG" /> <logger name="java.sql.Statement" 
        level="DEBUG" /> -->
    <root>
        <level value="DEBUG" />
        <appender-ref ref="FILE" />
        <appender-ref ref="CONSOLE" />
    </root>

</log4j:configuration>

*/