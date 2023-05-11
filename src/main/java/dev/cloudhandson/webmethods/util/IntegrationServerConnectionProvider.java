package dev.cloudhandson.webmethods.util;

import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;
import dev.cloudhandson.webmethods.model.IntegrationServerConfiguration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IntegrationServerConnectionProvider {

    private final static Logger LOGGER = LogManager.getLogger(IntegrationServerConnectionProvider.class);

    public Context getIntegrationServerConnection(IntegrationServerConfiguration integrationServerConfiguration) throws ServiceException {
        LOGGER.info("Retrieving Integration Server [" + integrationServerConfiguration.getAlias() + "] connection properties");

        String server = integrationServerConfiguration.getHost() + ":" + integrationServerConfiguration.getPort();
        String username = integrationServerConfiguration.getUsername();
        String password = integrationServerConfiguration.getPassword();

        LOGGER.info("Server   : " + server);
        LOGGER.info("Username : " + username);
        LOGGER.info("Password : ******");

        Context context = new Context();
        context.setSecure(true);

        LOGGER.info("Connecting to Integration Server [" + integrationServerConfiguration.getAlias() + "]");
        try {
            context.connect(integrationServerConfiguration.getHost(), integrationServerConfiguration.getPort(), username, password);
            LOGGER.info("Connected to Integration Server [" + integrationServerConfiguration.getAlias() + "]");
        } catch (ServiceException e) {
            LOGGER.error("Unable to connect to Integration Server [" + integrationServerConfiguration.getAlias() + "]");
            throw e;
        } catch (Exception e) {
            LOGGER.error("Time out : Unable to connect to Integration Server [" + integrationServerConfiguration.getAlias().toString() + "]");
            throw new ServiceException(e.getMessage());
        }

        return context;
    }
}
