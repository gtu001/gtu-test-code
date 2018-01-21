package gtu.digester;

import java.util.HashMap;
import java.util.Map;

public class DBConfig {
    private Map<String, DBProvider> dbProviders = new HashMap<String, DBProvider>();
    private Map<String, JNDIProvider> jndiProviders = new HashMap<String, JNDIProvider>();

    public Map<String, DBProvider> getDbProviders() {
        return dbProviders;
    }

    public void setDbProviders(Map<String, DBProvider> dbProviders) {
        this.dbProviders = dbProviders;
    }

    public void addDBProvider(DBProvider db) {
        dbProviders.put(db.getServer(), db);
    }

    public void addJNDIProvider(JNDIProvider jndi) {
        jndiProviders.put(jndi.getName(), jndi);
    }

    public DBProvider getDBProvider(String name) {
        DBProvider db = null;
        if (dbProviders.containsKey(name)) {
            db = (DBProvider) dbProviders.get(name);
        }
        return db;
    }

    public JNDIProvider getJNDIProvider(String name) {
        JNDIProvider jndi = null;
        if (dbProviders.containsKey(name)) {
            jndi = (JNDIProvider) jndiProviders.get(name);
        }
        return jndi;
    }

    public Map<String, JNDIProvider> getJndiProviders() {
        return jndiProviders;
    }

    public void setJndiProviders(Map<String, JNDIProvider> jndiProviders) {
        this.jndiProviders = jndiProviders;
    }

    public void addSubDBConfig(DBConfig subDBConfig) {
        dbProviders.putAll(subDBConfig.getDbProviders());
        jndiProviders.putAll(subDBConfig.getJndiProviders());
    }
}
