package dev.cloudhandson.webmethods.scheduling;

import dev.cloudhandson.webmethods.model.AppProperties;
import dev.cloudhandson.webmethods.model.IntegrationServerConfiguration;
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

    private static final Logger log = LogManager.getLogger(ScheduledWebmethodsTasks.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Autowired
    private AppProperties appProperties;

    @Scheduled(fixedRateString = "${scheduling.fixed-rate.in.milliseconds}")
    public void generateIntegrationServerMetrics() {
        log.info("Executing generateIntegrationServerMetrics ");
        log.info("Thread name = " + Thread.currentThread().getName() + " time = " + LocalDateTime.now());

        log.debug("Retrieving the list of the Integration Servers");
        // List of Integration Server aliases
        List<IntegrationServerConfiguration> isList = appProperties.getIsList();

        // Loop on all Integration Servers
        if (isList != null) {
            for (IntegrationServerConfiguration isConfig : isList) {
                log.info(isConfig.getAlias());
                String isMetricFile = "IS_" + isConfig.getAlias() + "_" + isConfig.getPort() + ".txt";
            }
        } else {
            log.debug("There is no IS alias configured");
        }
    }
}
