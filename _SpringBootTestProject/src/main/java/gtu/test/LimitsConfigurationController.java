package gtu.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsConfigurationController {

    @Autowired
    private LimitConfiguration configuration;

    @GetMapping("/limits")
    public LimitConfiguration retriveLimitsFromConfigurations() {
        System.out.println("#...  retriveLimitsFromConfigurations");
        // getting values from the properties file
        LimitConfiguration limit = new LimitConfiguration();
        limit.setMaximum(configuration.getMaximum());
        limit.setMinimum(configuration.getMinimum());
        return limit;
    }
}