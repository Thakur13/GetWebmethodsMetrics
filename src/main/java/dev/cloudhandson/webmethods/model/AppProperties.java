package dev.cloudhandson.webmethods.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private List<IntegrationServerConfiguration> isList;
    private List<UniversalMessagingConfiguration> umList;

    public List<IntegrationServerConfiguration> getIsList() {
        return isList;
    }

    public void setIsList(List<IntegrationServerConfiguration> isList) {
        this.isList = isList;
    }

    public List<UniversalMessagingConfiguration> getUmList() {
        return umList;
    }

    public void setUmList(List<UniversalMessagingConfiguration> umList) {
        this.umList = umList;
    }
}