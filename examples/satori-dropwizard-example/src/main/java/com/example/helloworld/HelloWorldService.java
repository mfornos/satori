package com.example.helloworld;

import satori.view.HandlebarsRenderer;
import satori.view.HandlebarsConfiguration;

import com.example.helloworld.health.TemplateHealthCheck;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Environment;

public class HelloWorldService extends Service<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldService().run(args);
    }

    private HelloWorldService() {
        super("hello-world");
    }

    @Override
    protected void initialize(HelloWorldConfiguration configuration, Environment environment) {
        final String template = configuration.getTemplate();
        final String defaultName = configuration.getDefaultName();

        environment.setJerseyProperty(HandlebarsConfiguration.EXPOSE_PSEUDO_VARS_PARAM,
                configuration.getExposePseudoVars());
        environment.addProvider(HandlebarsRenderer.class);

        environment.addResource(new HelloWorldResource(template, defaultName));
        environment.addHealthCheck(new TemplateHealthCheck(template));
    }

}
