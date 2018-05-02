package com.undertow.server;

import com.undertow.server.endpoints.TesteEndpoint;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.HandlerWrapper;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.servlet.api.DeploymentInfo;

public class UndertowServer {

    private UndertowJaxrsServer server;

    public UndertowServer() {
        this.server = new UndertowJaxrsServer();
    }

    public void start() {
        Builder builder = Undertow.builder().addHttpListener(8000, "0.0.0.0");
        this.server.start(builder);
        this.configureEndpoints();
    }

    public void stop() {
        this.server.stop();
    }

    private void configureEndpoints() {
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.getActualResourceClasses().add(TesteEndpoint.class);

        DeploymentInfo deploymentInfo = this.server.undertowDeployment(deployment) //
                .setClassLoader(ClassLoader.getSystemClassLoader()).setContextPath("/gateway/") //
                .setDeploymentName("gateway.war");

        deploymentInfo.addInitialHandlerChainWrapper(new HandlerWrapper() {
            @Override
            public HttpHandler wrap(HttpHandler handler) {
                return new BlockingHandler(new LoggingHandler(handler));
            }
        });

        this.server.deploy(deploymentInfo);
    }

    public static void main(String[] args) {
        new UndertowServer().start();
    }

}
