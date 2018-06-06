package com.undertow.server;

import com.undertow.server.endpoints.TesteEndpoint;
import com.undertow.server.endpoints.UndertowJaxrsServerTecoTeco;

import org.jboss.resteasy.spi.ResteasyDeployment;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.servlet.api.DeploymentInfo;

public class UndertowServer {

    private UndertowJaxrsServerTecoTeco server;

    public UndertowServer() {
        this.server = new UndertowJaxrsServerTecoTeco();
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

        // deploymentInfo.addInitialHandlerChainWrapper(new HandlerWrapper() {
        //     @Override
        //     public HttpHandler wrap(HttpHandler handler) {
        //         return new BlockingHandler(new LoggingHandler(handler));
        //     }
        // });

        this.server.deploy(deploymentInfo);
    }

    public static void main(String[] args) {
        new UndertowServer().start();
    }

}



/**


package com.undertow.server;

import javax.servlet.ServletException;

import com.undertow.server.endpoints.TesteEndpoint;

import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

import io.undertow.Undertow;
import io.undertow.Undertow.Builder;
import io.undertow.server.handlers.BlockingHandler;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;

public class UndertowServer {

    public UndertowServer() throws ServletException {
        
        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.getActualResourceClasses().add(TesteEndpoint.class);
        
        ServletContainer servletContainer = Servlets.defaultContainer();
        
        UndertowJaxrsServer server = new UndertowJaxrsServer();
        DeploymentInfo deploymentInfo = 
            server.undertowDeployment(deployment)
            .setClassLoader(ClassLoader.getSystemClassLoader()).setContextPath("/gateway/")
            .setDeploymentName("gateway.war");
        
        DeploymentManager deploymentManager = servletContainer.addDeployment(deploymentInfo);
        deploymentManager.deploy();
        
        Builder builder = Undertow.builder()
            .setHandler(new BlockingHandler(new LoggingHandler(deploymentManager.getDeployment().getHandler())))
            .addHttpListener(8000, "0.0.0.0");
        
        server.start(builder);
        
        server.deploy(deploymentManager.getDeployment().getDeploymentInfo());

    }

    public static void main(String[] args) throws ServletException {
        new UndertowServer();
    }


        // deploymentInfo.addInitialHandlerChainWrapper(new HandlerWrapper() {
        //     @Override
        //     public HttpHandler wrap(HttpHandler handler) {
        //         return new BlockingHandler(new LoggingHandler(handler));
        //     }
        // });

}

 
 */