package com.assignment.backend;

import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import java.io.IOException;
import java.net.URI;


/**
 * BankApplication Application
 * class.
 */
@Slf4j
public class BankApplication {
    // Base URI the Grizzly HTTP server will listen on
    private static final String BASE_URI = "http://localhost:8080/Bank/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    private static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.rest package
        final ResourceConfig rc = new ResourceConfig().packages("com.assignment.backend");
        rc.register(org.glassfish.jersey.moxy.json.MoxyJsonFeature.class);
        rc.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        rc.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Main method.
     *
     * @param args arguments passed while execution of Main
     * @throws IOException if an I/O error occurs.
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        String format = "Jersey app started with WADL available at "
                + "{}application.wadl\nHit enter to stop it...";
        log.info(format, BASE_URI);
        System.in.read();
        server.shutdownNow();
    }
}

