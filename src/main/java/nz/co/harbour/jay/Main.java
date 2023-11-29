package nz.co.harbour.jay;

import nz.co.harbour.jay.transaction.TransactionDao;
import org.eclipse.jetty.server.Server;
import org.glassfish.jersey.jetty.JettyHttpContainerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.sql.DataSource;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import static nz.co.harbour.jay.jdbc.DataSourceFactory.getAppDataSource;

public class Main {
    public static final String BASE_URI = "http://localhost:8080/";
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private static ResourceConfig buildResourceConfig() {
        DataSource appDataSource = getAppDataSource();

        TransactionDao transactionDao = new TransactionDao(appDataSource);
        ServiceBinder serviceBinder = new ServiceBinder(transactionDao);
        final ResourceConfig config = new ResourceConfig().packages("nz.co.harbour.jay");
        config.register(serviceBinder);
        return config;
    }

    public static void main(String[] args) {
        try {
            final ResourceConfig config = buildResourceConfig();
            final Server server = JettyHttpContainerFactory.createServer(URI.create(BASE_URI), config);

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("Shutting down the application...");
                    server.stop();
                    System.out.println("Done, exit.");
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, null, e);
                }
            }));
            System.out.println(String.format("Application started.%nStop the application using CTRL+C"));
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }
}