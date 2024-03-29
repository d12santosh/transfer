package com.assignment.backend;

import com.assignment.backend.infra.repo.SavingsAccountRepository;
import com.assignment.backend.domain.repo.AccountRepository;
import com.assignment.backend.domain.service.AccountService;
import com.assignment.backend.domain.service.impl.SavingsAccountServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.inject.Singleton;
import java.io.IOException;
import java.net.URI;


/**
 * BankApplication Application
 * class.
 */
@Slf4j
public class BankApplication {
    // Base URI the Grizzly HTTP server will listen on
    public static final String BASE_URI = "http://localhost:8080/Bank/";

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        // create a resource config that scans for JAX-RS resources and providers
        // in com.example.rest package
        final ResourceConfig rc = new ResourceConfig().packages("com.assignment.backend");
        rc.register(org.glassfish.jersey.moxy.json.MoxyJsonFeature.class);
        rc.register(getAbstractBinder());
        rc.property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        rc.property(ServerProperties.BV_DISABLE_VALIDATE_ON_EXECUTABLE_OVERRIDE_CHECK, true);

        // create and start a new instance of grizzly http server
        // exposing the Jersey application at BASE_URI
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    private static AbstractBinder getAbstractBinder() {
        return new AbstractBinder() {
            @Override
            protected void configure() {
                bind(SavingsAccountRepository.class).to(AccountRepository.class).in(Singleton.class);
                bind(SavingsAccountServiceImpl.class).to(AccountService.class).in(Singleton.class);
            }
        };

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

