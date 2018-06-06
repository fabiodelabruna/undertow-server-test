package com.undertow.server.endpoints;

import static io.undertow.servlet.Servlets.servlet;

import javax.servlet.ServletException;

import com.undertow.server.LoggingHandler;

import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;
import org.jboss.resteasy.spi.ResteasyDeployment;

import io.undertow.Undertow;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.servlet.api.ServletInfo;

public class UndertowJaxrsServerTecoTeco {

    final PathHandler root = new PathHandler();
    final ServletContainer container = ServletContainer.Factory.newInstance();
    protected Undertow server;

    public UndertowJaxrsServerTecoTeco start(Undertow.Builder builder) {
        server = builder.setHandler(new BlockingHandler(new LoggingHandler(root))).build();
        server.start();
        return this;
    }

    public DeploymentInfo undertowDeployment(ResteasyDeployment deployment, String mapping) {
        
        if (mapping == null)
            mapping = "/";
        if (!mapping.startsWith("/"))
            mapping = "/" + mapping;
        if (!mapping.endsWith("/"))
            mapping += "/";
        mapping = mapping + "*";
       
        String prefix = null;
        if (!mapping.equals("/*"))
            prefix = mapping.substring(0, mapping.length() - 2);

        ServletInfo resteasyServlet = servlet("ResteasyServlet", HttpServlet30Dispatcher.class)
            .setAsyncSupported(true)
            .setLoadOnStartup(1).addMapping(mapping);
        
        if (prefix != null)
            resteasyServlet.addInitParam("resteasy.servlet.mapping.prefix", prefix);

        return new DeploymentInfo().addServletContextAttribute(ResteasyDeployment.class.getName(), deployment)
                .addServlet(resteasyServlet);
    }

    public DeploymentInfo undertowDeployment(ResteasyDeployment deployment) {
        return undertowDeployment(deployment, "/");
    }

    public UndertowJaxrsServerTecoTeco deploy(DeploymentInfo builder) {
        DeploymentManager manager = container.addDeployment(builder);
        manager.deploy();
        try {
            root.addPrefixPath(builder.getContextPath(), manager.start());
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public void stop() {
        server.stop();
    }

}
