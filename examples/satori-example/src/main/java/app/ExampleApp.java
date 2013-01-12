package app;

import java.util.concurrent.TimeUnit;

import com.sun.jersey.api.core.PackagesResourceConfig;
import com.yammer.metrics.reporting.ConsoleReporter;

public class ExampleApp extends PackagesResourceConfig {
    
    public ExampleApp() {
        
        super("controllers", "com.yammer.metrics.jersey");
        
        ConsoleReporter.enable(10, TimeUnit.MINUTES);
        
    }
    
}
