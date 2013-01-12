package com.example.helloworld;

import org.apache.bval.constraints.NotEmpty;
import org.codehaus.jackson.annotate.JsonProperty;

import com.yammer.dropwizard.config.Configuration;

public class HelloWorldConfiguration extends Configuration {
    @NotEmpty @JsonProperty private String template;
    
    @NotEmpty @JsonProperty private Boolean exposePseudoVars;

    @NotEmpty @JsonProperty private String defaultName = "Stranger";

    public String getTemplate() {
        return template;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public Boolean getExposePseudoVars() {
        return exposePseudoVars;
    }
}
