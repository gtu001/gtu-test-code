package gtu.jms;

import java.io.*;
import java.util.*;

import javax.jms.*;
import javax.jms.Queue;
import javax.naming.*;

import com.fet.csp.*;

public class EventEmulator implements Runnable {
	private ResourceBundle bundle = null;
	private String strContextFactory = "weblogic.jndi.WLInitialContextFactory";
	private String strProviderUrl="t3://10.64.202.2:16002";
	private String strQueueConnectionFactory = "JMSconnectionFactory1";
	private String strQueue = "EAI2CSP";

	private Context initCtx;
	private QueueConnectionFactory factory;
	private QueueConnection connection;
	private QueueSession session;
	private Queue queue;
	private QueueSender sender;
	
	private String strTestFile;
	
	public EventEmulator() {
		init();
	}

	private void init() {
		bundle = ResourceBundle.getBundle("EventReader");
		strContextFactory = bundle.getString("INITIAL_CONTEXT_FACTORY").trim();
		strProviderUrl = bundle.getString("PROVIDER_URL").trim();
		strQueueConnectionFactory = bundle.getString("QUEUE_CONNECTION_FACTORY").trim();
		strQueue = bundle.getString("QUEUE_REQ").trim();
		strTestFile = bundle.getString("TestFile").trim();
	}

	public void run() {
		System.out.println("Running...");
		try {
			Hashtable hParams = new java.util.Hashtable(2);
			hParams.put(javax.naming.Context.INITIAL_CONTEXT_FACTORY, strContextFactory);
			hParams.put(javax.naming.Context.PROVIDER_URL, strProviderUrl);
			initCtx = new InitialContext(hParams);
			//System.out.println("initCtx" + initCtx);
			factory = (QueueConnectionFactory)initCtx.lookup(strQueueConnectionFactory);
			connection = factory.createQueueConnection();
			session = connection.createQueueSession(false, Session.CLIENT_ACKNOWLEDGE);

			queue = (Queue)initCtx.lookup(strQueue);
			sender = session.createSender(queue);

			System.out.println("Read file from " + strTestFile);
			String filename = strTestFile;
			BufferedReader in = new BufferedReader(new FileReader(filename));
			String s = null;
			while((s = in.readLine())!= null) {
				TextMessage message = session.createTextMessage();
				//s = "<XMLString><OP1>PRE_ACTIVATION</OP1><OP2>CERQ</OP2><MSISDN>0999876001</MSISDN><newMSISDN></newMSISDN><STATUS></STATUS><RATEPLAN1>A1</RATEPLAN1><RATEPLAN2></RATEPLAN2><VAS></VAS><timestamp>2003-10-29 00:00:00.0</timestamp></XMLString>";
				System.out.println("Send event: " + s);
				message.setText(s);
				sender.send(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (Exception e) {
				}
			}
		}
		System.out.println("Finished...");
	}
	
	public static void main (String[] args) {
		try{
			EventEmulator emulator = new EventEmulator();
			Thread t = new Thread(emulator);
			t.start();
		}catch(Throwable e){
			e.printStackTrace();
		}
	}
}