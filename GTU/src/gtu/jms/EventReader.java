package gtu.jms;

import java.io.*;
import java.util.*;
import java.text.*;

import javax.jms.*;
import javax.jms.Queue;
import javax.naming.*;

import com.fet.csp.*;
import com.fet.csp.common.*;

/**
 * This class provides functions to read status change events from a queue
 * server and write events to status change table.<br>
 * 
 * @author�GKenny<br>
 *
 * @see Thread
 */
public class EventReader extends Thread {
    private static ResourceBundle bundle = null;
    private static String strContextFactory = "weblogic.jndi.WLInitialContextFactory";
    private static String strProviderUrl = "t3://10.64.202.2:16002";
    private static String strQueueConnectionFactory = "JMSconnectionFactory1";
    private static String strQueueReq = "EAI2CSP";
    private static String strQueueResp = "CSP2JMS";
    private static String strHostIP = "127.0.0.1";
    private static boolean bSendAck = false;

    private static QueueConnectionFactory factory;
    private static Queue queueReq;
    private static Queue queueResp;
    private static String flag;

    private QueueConnection connection;
    private QueueSession session;
    private QueueReceiver receiver;
    private QueueSender sender;

    private String name = "Thread-n";
    protected String nickname = "";

    // indicate if Weblogic JMS is ready.
    private boolean isContextAlive = false;

    private boolean isAvailable = true;
    private boolean isDbAvailable = true;
    private ConnectTest ct = new ConnectTest();

    private static int iRetryInterval = 60 * 1000; // try to reconnect when
                                                   // failed to communicate with
                                                   // a queue(milliseconds)
    private static int iDbRetryInterval = 60 * 1000;
    private static int iReadTimeOut = 0 * 000; // wait for a message
                                               // arrives(milliseconds)

    // Prevention module, this should never occur.
    private static int iIndex = 0;
    private static boolean isProcessFail = false;

    protected static boolean bEnableSplitLog;

    private String strJMSDateFormat = "yyyyMMddHHmmssSSS";

    private static String strClassName = "EventReader";

    static {
        bundle = ResourceBundle.getBundle("EventReader");

        strContextFactory = bundle.getString("INITIAL_CONTEXT_FACTORY").trim();
        strProviderUrl = bundle.getString("PROVIDER_URL").trim();
        strQueueConnectionFactory = bundle.getString("QUEUE_CONNECTION_FACTORY").trim();
        strQueueReq = bundle.getString("QUEUE_REQ").trim();
        strQueueResp = bundle.getString("QUEUE_RESP").trim();
        flag = bundle.getString("flag").trim();

        strHostIP = bundle.getString("HostIP").trim();
        bSendAck = new Boolean(bundle.getString("SendAck").trim()).booleanValue();

        iRetryInterval = Integer.parseInt(bundle.getString("RetryInterval").trim());
        iDbRetryInterval = Integer.parseInt(bundle.getString("DbRetryInterval").trim());

        iReadTimeOut = Integer.parseInt(bundle.getString("ReadTimeOut").trim());

        String strBackupLog = bundle.getString("BackupLog").trim();
        String strErrorLog = bundle.getString("ErrorLog").trim();
        BackupLog.setFileName(strBackupLog);
        ErrorLog.setFileName(strErrorLog);

        String strBackupLogTMP = bundle.getString("BackupLogTMP").trim();
        String strErrorLogTMP = bundle.getString("ErrorLogTMP").trim();
        BackupLogTMP.setFileName(strBackupLogTMP);
        ErrorLogTMP.setFileName(strErrorLogTMP);

        Logger.log("INITIAL_CONTEXT_FACTORY: " + strContextFactory);
        Logger.log("PROVIDER_URL: " + strProviderUrl);
        Logger.log("QUEUE_CONNECTION_FACTORY: " + strQueueConnectionFactory);
        Logger.log("QUEUE_REQ: " + strQueueReq);
        Logger.log("QUEUE_RESP: " + strQueueResp);
        Logger.log("RetryInterval: " + iRetryInterval);
        Logger.log("DbRetryInterval: " + iDbRetryInterval);
    }

    /**
     * EventReader Constructor
     */
    public EventReader(String name) {
        this.name = name;
        this.nickname = name + "[" + Logger.formatDate(new Date(), "yyyyMMddHHmmss") + "]";
        // init();
    }

    /**
     * Initialize JMS setting, this method will lookup QueueConnectionFactory
     * and Queue definitions from a naming server.
     *
     * @return boolean
     *
     */
    private void initJMS() throws Exception {
        final String strMethodName = "initJMS";
        Logger.log(strClassName, strMethodName, "start");
        Context initCtx = null;
        try {
            Hashtable hParams = new java.util.Hashtable(2);
            hParams.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, strContextFactory);
            hParams.put(javax.naming.Context.PROVIDER_URL, strProviderUrl);
            initCtx = new InitialContext(hParams);
            factory = (QueueConnectionFactory) initCtx.lookup(strQueueConnectionFactory);
            queueReq = (Queue) initCtx.lookup(strQueueReq);
            if (bSendAck) {
                queueResp = (Queue) initCtx.lookup(strQueueResp);
            }
            Logger.log(strClassName, strMethodName, "success");
        } catch (Exception e) {
            throw (e);
        } finally {
            if (initCtx != null) {
                initCtx.close();
            }
            Logger.log(strClassName, strMethodName, "end");
        }
    }

    /**
     * Setup JMS environment, this will initialize JMS connection and create
     * queue sessions etc.
     *
     * @return void
     * @exception java.lang.Exception
     *
     */
    private void setupJMS() throws Exception {
        final String strMethodName = "setupJMS";
        Logger.log(strClassName, strMethodName, name + ";start");
        try {
            initJMS();

            if (connection != null) {
                connection.close();
                connection = null;
            }
            connection = factory.createQueueConnection();
            session = connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);
            receiver = session.createReceiver(queueReq);
            if (bSendAck) {
                sender = session.createSender(queueResp);
            }
            connection.start();
            isContextAlive = true;
            Logger.log(strClassName, strMethodName, name + ";success");
        } catch (Exception e) {
            Logger.log(strClassName, strMethodName, name + ";Exception=" + e);
            // throw(e);
        } finally {
            Logger.log(strClassName, strMethodName, name + ";end");
        }
    }

    /**
     * This is a main function to handle status change event.
     *
     * @return void
     *
     */
    private void execute() {
        final String strMethodName = "execute";
        Logger.log(strClassName, strMethodName, name + ";start");
        try {
            while (isAvailable) {
                try {
                    setupJMS();
                    read();
                    if (iIndex++ > 30000) {
                        iIndex = 0;
                    }
                    isProcessFail = false;
                } catch (Throwable e) {
                    isProcessFail = true;
                    isContextAlive = false;
                    e.printStackTrace();
                } finally {
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    Logger.log(strClassName, strMethodName, name + ";ExceptionX=" + e);
                }
                connection = null;
            }
            Logger.log(strClassName, strMethodName, name + ";end");
        }
    }

    public void run() {
        execute();
    }

    /**
     * Read JMS message and call ParseStatusEvent to handle status change
     * events.
     *
     * @return void
     * @exception java.lang.Exception
     *
     * @see com.fet.csp.ParseStatusEvent
     */
    private void read() throws Exception {
        Message message = receiver.receive(iReadTimeOut);
        if (message != null) {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String msgid = textMessage.getJMSMessageID();
                String msgidprn = "MSGID:[" + msgid + "]";
                Date msgtime = new Date(textMessage.getJMSTimestamp());
                String timeprn = "TIMESTAMP:[" + Logger.formatDate(msgtime, strJMSDateFormat) + "]";
                String value = textMessage.getText();
                // JMS for F2 Event
                if ("1".equals(flag)) {
                    ParseStatusEvent parser = new ParseStatusEvent();
                    String result = parser.changeUserStatus(value);
                    Logger.log(msgidprn + ";" + timeprn + ";message=" + value + ";result=" + result);
                    if (result.startsWith("00000000") || result.startsWith("10000000")) {
                        BackupLog.writeToLog(msgidprn + ";" + timeprn + ";message:[" + value + "]");
                        textMessage.acknowledge();
                        if (bSendAck) {
                            reply(msgid);
                        }
                    } else if (result.startsWith("10009913")) {
                        ErrorLog.writeToLog(msgidprn + ";" + timeprn + ";message:[" + value + "];result:[db fail]");
                        DbChecker.setIsAvailable(false);
                        session.recover();
                    } else if (result.startsWith("10009914")) {
                        ErrorLog.writeToLog(msgidprn + ";" + timeprn + ";message:[" + value + "];result:[sql fail]");
                        textMessage.acknowledge();
                        if (bSendAck) {
                            reply(msgid);
                        }
                    } else {
                        ErrorLog.writeToLog(msgidprn + ";" + timeprn + ";message:[" + value + "];result:[" + result + "]");
                        textMessage.acknowledge();
                        if (bSendAck) {
                            reply(msgid);
                        }
                    }
                }
            }
        }
    }

    /**
     * After CSP read a JMS message from EAI succefully, CSP will send a
     * confirmation message to the EAI queue.
     *
     * @return void
     * @exception java.lang.Exception
     *
     * @see com.fet.csp.ParseStatusEvent
     */
    private void reply(String correlatiobID) throws Exception {
        TextMessage message = session.createTextMessage();
        message.setJMSCorrelationID(correlatiobID);
        message.setText(strHostIP);
        sender.send(message);
        message.acknowledge();
        String msgidprn = "MSGID:[" + correlatiobID + "]";
        Logger.log(msgidprn + ";replied");
    }

    /**
     * Initialize n threads to handle status change events from queue server,
     * and use a StatusChecker to check if threads work fine, if something wrong
     * with a certain thread, the checker will restart the thread.
     *
     */
    public static void main(String[] args) {
        EventReader.bEnableSplitLog = Boolean.valueOf(bundle.getString("EnableSplitLog").trim()).booleanValue();

        String strLogDir = bundle.getString("LogDir").trim();
        String strLogHeader = bundle.getString("LogHeader").trim();
        String strLogInterval = bundle.getString("LogInterval").trim();

        int iThreadCount = Integer.parseInt(bundle.getString("ThreadCount"));
        Hashtable hProcess = new Hashtable(iThreadCount);

        for (int i = 0; i < iThreadCount; i++) {
            String name = "Thread" + i;
            EventReader t = new EventReader(name);
            t.start();
            hProcess.put(name, t);
            // StatusChecker.vProcess.add(t);
        }
    }
}
