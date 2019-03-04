package com.gtu.rest.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.gtu.rest.client.wms.ServiceMethodRequest;
import com.gtu.rest.client.wms.ServiceMethodResponse;

public class WMSServiceMethodClient extends WebServiceGatewaySupport {

    /** {{ 對方url }} */
    @Value("${WMSService}")
    private String url;

    /**
     * 傳送XML格式的request至XXService，並回傳結果。 {{ XServiceMethodResponse視maven產生的程式為準
     * }}
     * 
     * @param request
     * @return
     */
    public ServiceMethodResponse getResp(ServiceMethodRequest request) {
        return (ServiceMethodResponse) getWebServiceTemplate().marshalSendAndReceive(url, request);
    }
}
