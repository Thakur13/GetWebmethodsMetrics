package dev.cloudhandson.webmethods.model;

public class UniversalMessagingConfiguration {
    private String alias;
    private String host;
    private int port;
    private String timeout;
    private String truststore;
    private String truststorePassword;
    private String umCertificate;
    private String umAlias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getTruststore() {
        return truststore;
    }

    public void setTruststore(String truststore) {
        this.truststore = truststore;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public String getUmCertificate() {
        return umCertificate;
    }

    public void setUmCertificate(String umCertificate) {
        this.umCertificate = umCertificate;
    }

    public String getUmAlias() {
        return umAlias;
    }

    public void setUmAlias(String umAlias) {
        this.umAlias = umAlias;
    }
}