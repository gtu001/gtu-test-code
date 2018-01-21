package com.gtu.resource;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.gtu.dao.TransactionDao;
import com.gtu.entity.TestBean;
import com.gtu.entity.TestResult;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Component("testResource")
@Path("/test")
public class TestResource {

    private static final Logger logger = Logger.getLogger(TestResource.class);

    @Autowired(required = true)
    @Qualifier("transactionDao")
    private TransactionDao transactionDao;

    @Path("/getTest0")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest0() {
        TestResult result = new TestResult();
        result.setResult(true);
        transactionDao.save();
        logger.info("#. getTest0 OK !");
        return Response.status(Status.OK).entity(result).build();
    }

    @Path("/getTest1")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest1(@QueryParam("testStr") String testStr) {
        TestResult result = new TestResult();
        result.setResult(true);
        logger.info("#. getTest1 OK! - testStr : " + testStr);
        return Response.status(Status.OK).entity(result).build();
    }

    @Path("/getTest2")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest2(String testData) {
        TestResult response = new TestResult();
        response.setResult(true);
        logger.info("#. getTest2 OK! - testStr : " + testData);
        return Response.status(Status.OK).entity(response).build();
    }

    @Path("/getTest3")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest3(TestBean dataBean) {
        TestResult response = new TestResult();
        response.setResult(true);
        logger.info("#. getTest3 OK! - bean - " + ReflectionToStringBuilder.toString(dataBean));
        return Response.status(Status.OK).entity(response).build();
    }

    @Path("/getTest4")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_JSON)
    public TestResult getTest4(@QueryParam("testStr") String testStr, TestBean dataBean) {
        TestResult response = new TestResult();
        response.setResult(true);
        logger.info("#. getTest4 OK! - testStr - " + testStr);
        logger.info("#. getTest4 OK! - dataBean - " + ReflectionToStringBuilder.toString(dataBean));
        return response;
    }

    @Path("/getTest_MultipartformData")
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public TestResult getTest_MultipartformData(@FormDataParam("testStr") String testStr, @FormDataParam("dataBean") TestBean dataBean, @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition cdh) {
        TestResult response = new TestResult();
        response.setResult(true);
        logger.info("getTest_formData testStr - " + testStr);
        logger.info("getTest_formData dataBean - " + ReflectionToStringBuilder.toString(dataBean));
        logger.info("getTest_formData file - " + fileInputStream);
        logger.info("getTest_formData file - " + cdh);
        return response;
    }
}
