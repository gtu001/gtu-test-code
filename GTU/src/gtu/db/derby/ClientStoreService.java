package gtu.db.derby;

import java.awt.*;
import java.applet.*;
import java.io.*;
import java.util.*;

import javax.swing.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

import org.apache.derby.jdbc.EmbeddedDataSource;

/*
 * Embedded Apache Derby Demo - DerbyTax Application
 *
 * This demo showcases Derby running embedded in Mozilla Firefox as a local
 * database storage which can be accessed from Javascript.
 *
 * A ClientStoreService object is instantiated as a lightweight and hidden
 * applet - The ClientStoreService interface is accessible from javascript
 * via LiveConnect technology (provided as part of Sun's Java Plug-in). The
 * store service returns data in XML format.
 * 
 * NOTE: The motivation for defining such a public interface was to encapsulate
 *       as much as database operations within the client store service. Of
 *       course another approach would have been to allow JDBC calls to be
 *       performed directly from javascript (via liveconnect) or even sending
 *       SQL statements directly to a particular public interface as part of the
 *       ClientStoreService. The first approach was chosen to encapsulate as
 *       much pure database SQL operations as well as <b>not</b> exposing the
 *       database schema. The selected approach can be enhanced by decoupling
 *       the particular database operations and schema(s) by moving them to a
 *       separate DatabaseSource object which then can be instantiated via the
 *       ClientStoreService object, having this last one acting as a pure
 *       service object and proxy to actual DatabaseSource objects (1 per each
 *       database contractually and locally accessible by the webapp).
 *
 *       This demo application handles events such as the ones raised as part
 *       of an applet lifecycle. The client store service here will instantiate
 *       a JDBC DataSource object for creating connections against the required
 *       database. Access to the database source can be authenticated using
 *       one of Derby's authentication provider (i.e. BUILT-IN).
 *
 * @author francois.orsini@sun.com
 * @version 1.0
 * 
 */
public class ClientStoreService extends Applet implements Runnable {
    // Derby is accessed in embedded mode, this means that Derby is embedded
    // in the same process as the JVM - Hence, Derby will run embedded
    // within the web browser process context
    static final String framework = "embedded";
    static final String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    static final String protocol = "jdbc:derby:";

    // The datasource for the database to access - as mentioned in the class
    // comments, this object could be wrapped in some DatabaseSource object,
    // containing pure DB application logic that is currently part of the
    // ClientStoreService class for demo simplicity purposes.
    static volatile EmbeddedDataSource ds = null;

    // Debug (inlined) flag
    static final boolean DEBUG = true;

    // Store service thread for performing certain DB operations
    // asynchronously. Although this is not absolutely required, some
    // application might want to be able to issue simutaneous requests
    // against the store service.
    Thread storeServiceThread = null;

    // Store service OPeration types
    static final int OP_INSERT = 1;
    static final int OP_SELECT = 2;
    static final int OP_UPDATE = 3;
    static final int OP_DELETE = 4;

    public void init() {
        log("ClientStoreService: init() was called");
        setBackground(Color.white);

        // Check Store set-up - database schema with some prepopulated data
        // for the demo will be initialized in storeSetup(), if the database
        // or table are not there (i.e. the first time the demo is run)
        try {
            storeSetup();
        } catch (Exception e) {
            log(
                    e,
                    "ClientStoreService: init() - ERROR: Could not set-up DerbyTaxDB - Store Exception raised:");
        }
    }

    // Authenticate the user against the data (database) source
    public String login(String userName, String password, String dbName)
            throws Exception {
        // This will contain the generated XML for the data returned from
        // a resultset (i.e. SELECT query)
        String xml = null;

        log("ClientStoreService: login() was called");

        // Create a connection with the data source
        try {
            // No authentication required for the demo - built-in
            // authentication can be turned on by setting the
            // following property to 'true' at the system or database
            // level - derby.connection.requireAuthentication (See manuals)
            //
            // Connection c = ds.getConnection(userName, password);
            Connection c = ds.getConnection();
            Statement s = c.createStatement();

            // We just query all the rows as part of this demo - right now,
            // there is only 1 row which represents a Tax return with some
            // basic data
            //
            // If you want to filter the return based on the user name, you
            // would then uncomment the query below for a restrictive search.
            // ResultSet rs =
            // s.executeQuery("SELECT * FROM DerbyTax WHERE username='" +
            // userName + "'");
            ResultSet rs = s.executeQuery("SELECT * FROM DerbyTax");

            // Generate the XML for the ResultSet (data)
            // XML is what gets returned to the client.
            xml = Util.toXML(rs);

            rs.close();
            s.close();
            c.close();
        } catch (Exception e) {
            log("login() raised an exception:", e);
            throw (e); // Javascript can catch Java Exceptions via Liveconnect
        }

        log("XML = " + xml);

        return xml;
    }

    // Called as part of applet lifecycle
    public void start() {
        log("ClientStoreService: start() was called");

        // There is not much we need to do in this method called as part
        // of the applet lifecycle - we could of course move some of the logic
        // in init() right here but this is not necessary (at least for this
        // demo).
    }

    // Called as part of applet lifecycle
    public void run() {
        log("ClientStoreService: run() was called");

        // There is not much we need to do in this method called as part
        // of the applet lifecycle - if the client service were to support
        // Ajax (XMLHTTPRequest) requests, it would then launch the
        // embedded web server in the run method to serve such requests.
    }

    // Called as part of applet lifecycle
    public void stop() {
        log("ClientStoreService: stop() was called");

        // Stop/Terminate the store service thread if it is running
        if (storeServiceThread != null) {
            try {
                log("ClientStoreService: Terminating store service thread");
                storeServiceThread.interrupt();
                storeServiceThread = null;
            } catch (Throwable t) {
                log("ClientStoreService.stop() raised an exception", t);
            }
        }
    }

    // Called as part of applet lifecycle - as part of this demo application,
    // we shutdown the database and the Derby engine.
    public void destroy() {
        log("ClientStoreService: destroy() was called");

        // Shutdown store
        shutdown();
    }

    // Shutdown the client store service - more specifically, the Derby engine
    // instance and the database will be shutdown as well.
    public void shutdown() {
        Connection c = null;

        log("ClientStoreService: shutdown() was called");
        log("ClientStoreService: Shutting down Store service");

        // Shutdown store
        try {
            log("ClientStoreService: Shutting down Derby instance");
            DriverManager.getConnection("jdbc:derby:;shutdown=true");
            /*
             * ds.setDatabaseName("DerbyTaxDB");
             * ds.setShutdownDatabase("shutdown");
             * 
             * log("ClientStoreService: Shutting down data source"); c =
             * ds.getConnection();
             */
        } catch (Exception e) {
            log("ClientStoreService: has been shutdown");
            // log("Shutdown: ", e);
        } finally {
            c = null;
            ds = null;
        }
        // This forces some of the no longer needed Derby classes to be
        // unloaded as there could be some issue if Derby were to be
        // restarted right after shutting down - This is specified in
        // the Derby Developer's guide (section "Shutting down the system").
        System.gc();
    }

    // Application specific code - this could be moved in a separate
    // class but the demo, we will leave it here.
    private void storeSetup() throws Exception {
        Connection c = null;
        Statement s = null;

        log("ClientStoreService: setting-up store (or not)");

        ds = new EmbeddedDataSource();

        // We attempt to create a database named "DerbyTaxDB"
        ds.setCreateDatabase("create");
        ds.setDatabaseName("DerbyTaxDB");

        try {
            c = ds.getConnection();
        } catch (SQLWarning e) {
            // Database already exists -- we keep going
            log("ClientStoreService: DerbyTaxDB database already exists...");
            ds.setCreateDatabase(null);
        } catch (Exception e) {
            // Something wrong happened
            log(e,
                    "ERROR: Cannot connect to DerbyTaxDB database - Exception raised:");
            c = null;
            throw e;
        }

        // create the procedure definition
        try {
            s = c.createStatement();
            s
                    .execute("CREATE TABLE DerbyTax(userName varchar(20), returnName varchar(20), FilingStatus varchar(5), TPFirstName varchar(20), SPFirstName varchar(20), TPMI char(1), SPMI char(1), TPLastName varchar(20), SPLastName varchar(20),TPTitle varchar(3), SPTitle varchar(3), TPBirthDate varchar(20), SPBirthDate varchar(20), TP_SSN_1 char(3), TP_SSN_2 char(2), TP_SSN_3 char(4), SP_SSN_1 char(3), SP_SSN_2 char(2), SP_SSN_3 char(4), TPAddress varchar(20), TPAptNum varchar(20), TPCity varchar(20), TPZip varchar(10), TPHomePhone varchar(12), TPSTATE varchar(2), TPOccup varchar(20),SPOccup varchar(20), TPWorkPhone varchar(12), SPWorkPhone varchar(12), TPWorkPhoneExt varchar(6), SPWorkPhoneExt varchar(6), W2_b varchar(20), W2EmpName varchar(20), W2EmpAddress varchar(3), W2EmpCity varchar(20), W2EmpState varchar(2), W2EmpZip varchar(10), W2_1 varchar(20), W2_2 varchar(20), W2_3 varchar(20), W2_4 varchar(20), W2_5 varchar(20), W2_6 varchar(20), W2_7 varchar(20), W2_8 varchar(20), W2_9 varchar(20), W2_10 varchar(20), W2_11 varchar(20))");

        } catch (SQLException e) {
            // Table already exists so no need to recreate it
            log("ClientStoreService: DerbyTax table already exists...");
            try {
                s.close();
                c.close();
                return;
            } catch (Exception ex) {
                throw e;
            }
        }

        try {
            // Set-up some data to play with
            s
                    .execute("INSERT INTO DerbyTax (userName, returnName, TPFirstName, SPFirstName, TPMI, SPMI, TPLastName, SPLastName) VALUES ('Rebecca', 'Rebecca Return', 'Rebecca', 'Frank', 'C', 'E', 'Smith', 'Martin')");
        } catch (SQLException e) {
            // Something wrong happened - log it
            log(e,
                    "ClientStoreService: Error while inserting data in DerbyTax table");
        }

        try {
            s.close();
            c.close();
        } catch (Exception ex) {
        }

        return;
    }

    public String accessStore(int action, String returnName, String fieldName,
            String fieldValue) {
        String xmlData;

        log("ClientStoreService: accessStore() was called");

        storeServiceThread = new StoreServiceThread(action, returnName,
                fieldName, fieldValue);

        log("storeServiceThread: Starting Store Service thread");

        storeServiceThread.start();

        if (action == OP_SELECT) // SELECT only
        {
            // Wait for the thread to complete in order to retrieve the data
            try {
                storeServiceThread.join();
            } catch (java.lang.InterruptedException ie) {
                log("InterruptedException", ie);
            }
            // Retrieve data (ResultSet) in XML format
            xmlData = ((StoreServiceThread) storeServiceThread).getXML();
        } else {
            xmlData = null;
        }
        storeServiceThread = null;

        // return data in XML
        return xmlData;
    }

    // Store Service thread for individual operations
    class StoreServiceThread extends Thread {
        int action;
        String returnName;
        String fieldName;
        String fieldValue;
        String xml;

        public StoreServiceThread(int action, String returnName,
                String fieldName, String fieldValue) {
            this.action = action;
            this.returnName = returnName;
            this.fieldName = fieldName;
            this.fieldValue = fieldValue;
        }

        public String getXML() {
            return this.xml;
        }

        // This code will get moved to an application class in the nex revision
        public void run() {

            Connection c = null;
            Statement s = null;
            ResultSet rs = null;

            ds.setDatabaseName("DerbyTaxDB");

            try {
                c = ds.getConnection();
            } catch (Exception e) {
                log(e, "Failed connecting to DerbyTaxDB database...");
                return;
            }

            // Perform the actual DML requests - FIXME: to be moved out
            try {
                s = c.createStatement();
                if (action == OP_INSERT) // Insert operation
                {
                    log("Creating Tax Return w/ returnName=[" + returnName
                            + "], userName=[" + fieldValue + "]");
                    // s.execute("INSERT INTO DerbyTax (" + fieldName +
                    // ") VALUES ('" + fieldValue + "'");
                    s.execute("DELETE FROM DerbyTax");
                    s
                            .execute("INSERT INTO DerbyTax (userName, returnName) VALUES ('"
                                    + fieldValue + "', '" + returnName + "')");
                }

                if (action == OP_SELECT) // Select operation
                {
                    log("Selecting Tax Return w/ fieldName=[" + fieldName
                            + "], fieldValue=[" + fieldValue + "]");
                    rs = s.executeQuery("SELECT * FROM DerbyTax WHERE "
                            + fieldName + " = '" + fieldValue + "'");

                    xml = Util.toXML(rs);
                }

                if (action == OP_UPDATE) // Update operation
                {
                    log("Updating Tax Return w/ fieldName=[" + fieldName
                            + "], fieldValue=[" + fieldValue + "]");
                    s.execute("UPDATE DerbyTax SET " + fieldName + " = '"
                            + fieldValue + "'");
                }

            } catch (SQLException e) {
                // Table already exists so no need to recreate it
                log(e, "StoreServiceThread() raised an exception:");

                try {
                    rs.close();
                    s.close();
                    c.close();
                } catch (Exception ex) {
                }

                return;
            }
            try {
                rs.close();
                s.close();
                c.close();
            } catch (Exception exc) {
            }
        }
    }

    // Logging methods to trace to stdout (i.e. Java Console)
    private void log(String message) {
        if (DEBUG) {
            Date date = new Date(System.currentTimeMillis());
            System.out.println("[" + date.toString() + "] " + message);
        }
    }

    private void log(String message, Throwable throwable) {
        if (DEBUG) {
            StringWriter sw = new StringWriter();
            throwable.printStackTrace(new PrintWriter(sw));
            log(message + '\n' + sw);
        }
    }

    private void log(Exception exception, String message) {
        if (DEBUG) {
            log(message, exception);
        }
    }

    public String toString() {
        return "ClientStoreService";
    }
}
