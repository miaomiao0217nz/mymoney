package nz.co.harbour.jay;

import nz.co.harbour.jay.transaction.TransactionDao;
import nz.co.harbour.jay.transaction.TransactionResource;
import org.eclipse.jetty.security.*;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import javax.sql.DataSource;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Logger;

import static nz.co.harbour.jay.jdbc.DataSourceFactory.getAppDataSource;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    static ServletContextHandler buildUsingResourceConfig(String pass) throws URISyntaxException, MalformedURLException {
        // Figure out what path to serve content from
        ClassLoader cl = Main.class.getClassLoader();
        // We look for a file, as ClassLoader.getResource() is not
        // designed to look for directories (we resolve the directory later)
        URL f = cl.getResource("html/");
        System.err.println("WebRoot is " + f.toURI());
        ResourceHandler staticResource = new ResourceHandler();
        staticResource.setWelcomeFiles(new String[] { "index.html" });
        staticResource.setBaseResource(Resource.newResource(f.toURI()));

        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        handler.setContextPath("/");
        handler.setHandler(staticResource);
        
        DataSource appDataSource = getAppDataSource();
        TransactionDao transactionDao = new TransactionDao(appDataSource);
        ServiceBinder serviceBinder = new ServiceBinder(transactionDao);
        final ResourceConfig config = new ResourceConfig();
        config.register(serviceBinder);
        config.register(ObjectMapperProvider.class);
        config.register(TransactionResource.class);
        handler.addServlet(new ServletHolder(new ServletContainer(config)), "/*");
        handler.setSecurityHandler(basicAuth("sophie", pass, "Private!"));
        return handler;
    }

    public static void main(String[] args) throws Exception {
        String pass = System.getenv("PASS");
        if (StringUtil.isBlank(pass))
            pass = "mymoney!";

        ServletContextHandler handler = buildUsingResourceConfig(pass);
        Server server = new Server(80);
        server.setHandler(handler);
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }

    private static final SecurityHandler basicAuth(String username, String password, String realm) {

        UserStore userStore = new UserStore();
        userStore.addUser(username, Credential.getCredential(password), new String[]{"user"});

        HashLoginService l = new HashLoginService();
        l.setUserStore(userStore);
        l.setName(realm);

        Constraint constraint = new Constraint();
        constraint.setName(Constraint.__BASIC_AUTH);
        constraint.setRoles(new String[]{"user"});
        constraint.setAuthenticate(true);

        ConstraintMapping cm = new ConstraintMapping();
        cm.setConstraint(constraint);
        cm.setPathSpec("/*");

        ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
        csh.setAuthenticator(new BasicAuthenticator());
        csh.setRealmName("myrealm");
        csh.addConstraintMapping(cm);
        csh.setLoginService(l);

        return csh;

    }
}