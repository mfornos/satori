package com.example.helloworld;

import com.yammer.dropwizard.config.Bootstrap;
import satori.view.HandlebarsRenderer;
import satori.view.HandlebarsConfiguration;

import com.example.helloworld.health.TemplateHealthCheck;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Environment;

public class HelloWorldService extends Service<HelloWorldConfiguration> {
    public static void main(String[] args) throws Exception {
        new HelloWorldService().run(args);
    }

    @Override
    public void initialize(Bootstrap<HelloWorldConfiguration> bootstrap) {
        bootstrap.setName("hello-world");
    }

    @Override
    public void run(HelloWorldConfiguration configuration, Environment environment) throws Exception {
        final String template = configuration.getTemplate();
        final String defaultName = configuration.getDefaultName();

        environment.setJerseyProperty(HandlebarsConfiguration.EXPOSE_PSEUDO_VARS_PARAM,
                configuration.getExposePseudoVars());
        environment.addProvider(HandlebarsRenderer.class);

        environment.addResource(new HelloWorldResource(template, defaultName));
        environment.addHealthCheck(new TemplateHealthCheck(template));
    }
}
