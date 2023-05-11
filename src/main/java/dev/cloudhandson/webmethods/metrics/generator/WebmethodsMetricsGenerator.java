package dev.cloudhandson.webmethods.metrics.generator;

import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import dev.cloudhandson.webmethods.model.IntegrationServerConfiguration;
import dev.cloudhandson.webmethods.util.IDataToolsUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class WebmethodsMetricsGenerator {

    private static final String SEPARATOR = " ";
    private static final String WM_SERVER_QUERY = "wm.server.query";
    private static final String WM_SERVER_MESSAGING = "wm.server.messaging";

    private static final Logger isLog = LogManager.getLogger(WebmethodsMetricsGenerator.class);
    private static final Logger isCloudMonitoringHistorisationLog = LogManager.getLogger("isCloudMonitoringHistorisationLog");

    private IntegrationServerConfiguration integrationServerConfiguration;

    public WebmethodsMetricsGenerator(IntegrationServerConfiguration integrationServerConfiguration) {
        this.integrationServerConfiguration = integrationServerConfiguration;
    }

    public void generateIntegrationServerHistorisation(Context context) throws IOException {
        try {
            isLog.debug("Executing generateIntegrationServerHistorisation");

            // Get data from Jvm Stats
            IData jvmStatsIData = context.invoke(WM_SERVER_QUERY, "getStats", null);
            IDataCursor jvmStatsIDataCursor = jvmStatsIData.getCursor();

            String freeMemory = IDataUtil.getString(jvmStatsIDataCursor, "freeMem");
            String usedMemory = IDataUtil.getString(jvmStatsIDataCursor, "usedMem");
            String totalMemory = IDataUtil.getString(jvmStatsIDataCursor, "totalMem");

            jvmStatsIDataCursor.destroy();


            // Get data from Available Threads
            IData resourceSettingsIData = context.invoke(WM_SERVER_QUERY, "getResourceSettings", null);
            String availableThreads = IDataToolsUtil.getLeafNode(resourceSettingsIData, "Resources.ServerThreadPool.fields.AvailableThreads.value");

            // Get UM details on IS : enabled, connected, state
            String enabled = "";
            String connected = "";
            String state = "";

            IData connAliasInput = IDataFactory.create();
            IDataCursor inputIDataCursor = connAliasInput.getCursor();
            inputIDataCursor.insertAfter("type", "UM");
            inputIDataCursor.destroy();

            IData umIData = context.invoke(WM_SERVER_MESSAGING, "getConnectionAliases", connAliasInput);
            IDataCursor umIDataCursor = umIData.getCursor();
            String[] umList = IDataUtil.getStringArray(umIDataCursor, "connAliases");
            umIDataCursor.destroy();

            StringBuilder umStates = new StringBuilder();
            for (String um : umList) {
                IData connAliasReportInput = IDataFactory.create();
                IDataCursor umIDataCursorInput = connAliasReportInput.getCursor();
                umIDataCursorInput.insertAfter("aliasName", um);
                umIDataCursorInput.destroy();
                IData connAliasReportOutput = context.invoke(WM_SERVER_MESSAGING, "getConnectionAliasReport", connAliasReportInput);
                IDataCursor umIDataCursorOutput = connAliasReportOutput.getCursor();
                enabled = IDataUtil.getString(umIDataCursorOutput, "enabled");
                connected = IDataUtil.getString(umIDataCursorOutput, "connected");
                state = IDataUtil.getString(umIDataCursorOutput, "_state");
                umStates.append(enabled).append(SEPARATOR);
                umStates.append(connected).append(SEPARATOR);
                umStates.append(state).append(SEPARATOR);
                umIDataCursorOutput.destroy();
            }
            // End of get UM details on IS

            String percentageThreadsAvailable = availableThreads.substring(0, availableThreads.indexOf(" %"));
            String numThreads = availableThreads.substring(availableThreads.indexOf("(") + 1, availableThreads.indexOf(" Threads"));

            isCloudMonitoringHistorisationLog.info(integrationServerConfiguration.getHost() + ":" + integrationServerConfiguration.getPort() + SEPARATOR + totalMemory + SEPARATOR + freeMemory + SEPARATOR + usedMemory + SEPARATOR + percentageThreadsAvailable + SEPARATOR + numThreads + SEPARATOR + umStates.toString());
        } catch (ServiceException e) {
            isLog.error("An error occurred while generating Integration Server Metrics : " + e.getMessage());
        }
    }
}
