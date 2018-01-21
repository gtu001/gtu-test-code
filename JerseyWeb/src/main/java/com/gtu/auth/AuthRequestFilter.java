package com.gtu.auth;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;

import com.sun.jersey.oauth.server.OAuthServerRequest;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.sun.jersey.oauth.signature.OAuthSignature;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;

public class AuthRequestFilter implements ContainerRequestFilter {
    @Context
    HttpServletRequest webRequest;

    public ContainerRequest filter(ContainerRequest arg0) {
        webRequest.getSession().getId();
        OAuthServerRequest oauthRequest = new OAuthServerRequest(arg0);
        OAuthParameters params = new OAuthParameters();
        params.readRequest(oauthRequest);
        OAuthSecrets secrets = new OAuthSecrets().consumerSecret("w96j0bp6g.4");
        try {
            if (!OAuthSignature.verify(oauthRequest, params, secrets)) {
                throw new WebApplicationException(401);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new WebApplicationException(401);
        }
        System.out.println("ContainerRequest=" + arg0.toString());
        return arg0;
    }
}
