package gtu.digester;

public class DBProvider {
    private String url;
    private String db;
    private String ip;
    private String port;
    private String usr;
    private String pwd;
    private String driver;
    private String server;
    private String connMode;
    private String jndiName;

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getConnMode() {
        return connMode;
    }

    public void setConnMode(String connMode) {
        this.connMode = connMode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String string) {
        url = string;
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String string) {
        jndiName = string;
    }

}
