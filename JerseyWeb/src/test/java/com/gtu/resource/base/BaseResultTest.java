package com.gtu.resource.base;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.gtu.resource.TestResourceTest;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;

public class BaseResultTest {
    private static final Logger logger = Logger.getLogger(BaseResultTest.class);

    protected static String REST_PATH = "/test";
    protected static String WS_SECRET = "w96j0bp6g.4";
    protected static String WS_HOST_NAME = "http://localhost:8090/JerseyWeb/rest";

    protected URI getBaseURI() {
        return UriBuilder.fromUri(WS_HOST_NAME).build();
    }

    protected WebResource getWebResource() {
        OAuthParameters params = new OAuthParameters().signature("HAMC-SHA1").consumerKey("key");
        OAuthSecrets secrets = new OAuthSecrets().consumerSecret(WS_SECRET);
        Client client = Client.create();
        OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
        WebResource resource = client.resource(getBaseURI());
        resource.addFilter(filter);
        return resource;
    }

    protected void errorHandler(ClientResponse response) {
        if (200 != response.getStatus()) {
            if (400 == response.getStatus()) {
                logger.error("ERROR " + response.getStatus() + ":非法請求");
            } else if (401 == response.getStatus()) {
                logger.error("ERROR " + response.getStatus() + ":沒有權限存取");
            } else if (403 == response.getStatus()) {
                logger.error("ERROR " + response.getStatus() + ":禁止存取");
            } else if (404 == response.getStatus()) {
                logger.error("ERROR " + response.getStatus() + ":網頁不存在");
            } else if (500 == response.getStatus()) {
                logger.error("ERROR " + response.getStatus() + ":伺服器內部錯誤");
            } else if (503 == response.getStatus()) {
                logger.error("ERROR " + response.getStatus() + ":服務不可用");
            } else {
                logger.error("ERROR " + response.getStatus() + " Other Error:" + response.getStatusInfo().getReasonPhrase());
            }
        } else {
            logger.info(response.getStatus() + " : 正常");
        }
    }
}
