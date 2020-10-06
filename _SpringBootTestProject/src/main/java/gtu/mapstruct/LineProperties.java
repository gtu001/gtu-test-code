//package gtu.mapstruct;
//
//import lombok.Data;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Configuration;
//
//@Data
//@Configuration
//@ConfigurationProperties(prefix = "spring.boot.admin.notify.line")
//@ConditionalOnProperty(prefix = "spring.boot.admin.notify.line", name = "enabled", matchIfMissing = true)
//public class LineProperties {
//    private boolean enabled = false;
//    private String channelSecret;
//    private String channelToken;
//    private String to;
//}