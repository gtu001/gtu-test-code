/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package tw.gov.moi.ae.jms;

import gtu.string.StringCompressUtil;
import gtu.xml.xstream.iisi.XmlParserImpl;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

/**
 * 訊息通報資料.
 */
public class JmsMessageNew extends JmsMessage implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -6284044449693738612L;

    /** 編碼過的訊息內容. */
    private String compressedMsg;

    private transient Object msgObj;

    private transient String msgXML;
    
    /**
     * 建構子.
     */
    public JmsMessageNew() {
        super();
    }

    /**
     * 建構子.
     */
    public JmsMessageNew(JmsMessage oldMsg) {
        super();
        this.setCallerId(oldMsg.getCallerId());
        this.setCategory(oldMsg.getCategory());
        this.setCount(oldMsg.getCount());
        this.setDescription(oldMsg.getDescription());
        this.setJmsTaskId(oldMsg.getJmsTaskId());
        this.setJmsTaskIdTemp(oldMsg.getJmsTaskIdTemp());
        this.setMessageId(oldMsg.getMessageId());
        this.setReceiverSiteId(oldMsg.getReceiverSiteId());
        this.setReceiverSystemId(oldMsg.getReceiverSystemId());
        this.setSendDate(oldMsg.getSendDate());
        this.setSenderSiteId(oldMsg.getSenderSiteId());
        this.setSenderSystemId(oldMsg.getSenderSystemId());
        this.setTransationId(oldMsg.getTransationId());

        final Object message = oldMsg.getMessage();
        if (message instanceof String) {
            final String msgString = (String) message;
            if (msgString.startsWith("H4sIAAAA")) { // ziped
                this.setCompressedMsg(msgString);
            } else {
                this.setMessage(message);
            }
        } else {
            this.setMessage(message);
        }

    }

    /**
     * @param message2
     */
    @Override
    public void setMessage(Object message) {
        final XmlParserImpl xmlParser = new XmlParserImpl();
        final String objAsXML = xmlParser.genXmlString(message);
        this.msgObj = message;
        this.msgXML = objAsXML;
        this.compressedMsg = StringCompressUtil.compress(objAsXML);
    }

    /**
     * @return the message
     */
    @Override
    public Object getMessage() {
        prepareObject();
        return this.msgObj;
    }

    private void prepareObject() {
        if (this.msgObj == null && StringUtils.isNotEmpty(this.compressedMsg)) {
            final XmlParserImpl xmlParser = new XmlParserImpl();
            this.msgXML = StringCompressUtil.uncompress(this.compressedMsg);
            try {
                this.msgObj = xmlParser.parseToObj(this.msgXML);
            } catch (Exception e) {
                this.msgObj = this.msgXML;
            }
        }
    }

    /**
     * @return the message
     */
    @Override
    public String getMessageXML() {
        prepareObject();
        return this.msgXML;
    }

    /**
     * @param compressedMsg the compressedMsg to set
     */
    public void setCompressedMsg(String compressedMsg) {
        this.compressedMsg = compressedMsg;
        this.msgObj = null;
        this.msgXML = null;
        //prepareObject();
    }

    /**
     * @return the compressedMsg
     */
    public String getCompressedMsg() {
        return this.compressedMsg;
    }

    /**
     * @see tw.gov.moi.ae.jms.JmsMessage#isOld()
     */
    @Override
    public boolean isOld() {
        return false;
    }

}
