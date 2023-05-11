package dev.cloudhandson.webmethods.scheduling;

import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;
import dev.cloudhandson.webmethods.metrics.generator.WebmethodsMetricsGenerator;
import dev.cloudhandson.webmethods.model.AppProperties;
import dev.cloudhandson.webmethods.model.IntegrationServerConfiguration;
import dev.cloudhandson.webmethods.util.IntegrationServerConnectionProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class ScheduledWebmethodsTasks {

    private static final Logger isLog = LogManager.getLogger(WebmethodsMetricsGenerator.class);

    @Autowired
    private AppProperties appProperties;

    @Scheduled(fixedRateString = "${scheduling.fixed-rate.in.milliseconds}")
    public void generateIntegrationServerMetrics() {
        isLog.debug("Executing generateIntegrationServerMetrics ");
        isLog.info("Thread name = " + Thread.currentThread().getName() + " time = " + LocalDateTime.now());

        isLog.info("Retrieving the list of the Integration Servers");

        // List of Integration Server aliases
        List<IntegrationServerConfiguration> isList = appProperties.getIsList();

        // Loop on all Integration Servers
        if (isList != null) {
            for (IntegrationServerConfiguration isConfig : isList) {
                Context context = null;
                try {
                    isLog.info(isConfig.getAlias());

                    // Get Integration Server connection
                    IntegrationServerConnectionProvider isConnection = new IntegrationServerConnectionProvider();
                    context = isConnection.getIntegrationServerConnection(isConfig);

                    // Start generating Integration Server metrics
                    WebmethodsMetricsGenerator metricsGenerator = new WebmethodsMetricsGenerator(isConfig);
                    metricsGenerator.generateIntegrationServerHistorisation(context);

                } catch (ServiceException e) {
                    isLog.error("Error during processing : " + isConfig.getHost() + " : " + e.getMessage());
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    isLog.error("Error during processing : " + isConfig.getHost() + " : " + e.getMessage());
                } finally {
                  if (context != null) {
                      isLog.info("Closing connection to Integration Server [" + isConfig.getAlias() + "]");
                      context.disconnect();
                      isLog.info("Connection closed");
                  }
                }
            }
        } else {
            isLog.info("There is no Integration Server alias configured");
        }
    }
}
