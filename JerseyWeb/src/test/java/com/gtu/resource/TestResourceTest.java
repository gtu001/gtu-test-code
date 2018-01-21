package com.gtu.resource;

import java.io.File;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import com.gtu.entity.TestBean;
import com.gtu.entity.TestResult;
import com.gtu.resource.base.BaseResultTest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.MultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

public class TestResourceTest extends BaseResultTest{
    private static final Logger logger = Logger.getLogger(BaseResultTest.class);

    @Test
    public void testGetTest0() {
        logger.info("#. testGetTest0 .s");
        WebResource resource = getWebResource();
        ClientResponse response = null;
        try {
            response = resource.path(REST_PATH)//
                    .path("getTest0")//
                    .type(MediaType.APPLICATION_XML)//
                    .post(ClientResponse.class);//
            errorHandler(response);
            boolean resultOK = false;
            if (200 == response.getStatus() || 204 == response.getStatus()) {
                resultOK = true;
                ObjectMapper mapper = new ObjectMapper();
                TestResult result = mapper.readValue(response.getEntity(String.class), TestResult.class);
                logger.info("result = " + ReflectionToStringBuilder.toString(result));
            }
            Assert.assertTrue(resultOK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            Assert.assertTrue(false);
        }
    }

    @Test
    public void testGetTest1() {
        logger.info("#. testGetTest1 .s");
        WebResource resource = getWebResource();
        ClientResponse response = null;
        try {
            MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
            queryParams.add("testStr", "哈哈哈");
            response = resource.path(REST_PATH)//
                    .path("getTest1")//
                    .queryParams(queryParams)//
                    .type(MediaType.APPLICATION_XML)//
                    .post(ClientResponse.class);//
            errorHandler(response);
            boolean resultOK = false;
            if (200 == response.getStatus() || 204 == response.getStatus()) {
                resultOK = true;
                ObjectMapper mapper = new ObjectMapper();
                TestResult result = mapper.readValue(response.getEntity(String.class), TestResult.class);
                logger.info("result = " + ReflectionToStringBuilder.toString(result));
            }
            Assert.assertTrue(resultOK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            Assert.assertTrue(false);
        }
    }
    
    @Test
    public void testGetTest2() {
        logger.info("#. testGetTest2 .s");
        WebResource resource = getWebResource();
        ClientResponse response = null;
        try {
            response = resource.path(REST_PATH)//
                    .path("getTest2")//
                    .type(MediaType.APPLICATION_XML)//
                    .post(ClientResponse.class, "測試2");//
            errorHandler(response);
            boolean resultOK = false;
            if (200 == response.getStatus() || 204 == response.getStatus()) {
                resultOK = true;
                ObjectMapper mapper = new ObjectMapper();
                TestResult result = mapper.readValue(response.getEntity(String.class), TestResult.class);
                logger.info("result = " + ReflectionToStringBuilder.toString(result));
            }
            Assert.assertTrue(resultOK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            Assert.assertTrue(false);
        }
    }
    
    @Test
    public void testGetTest3() {
        logger.info("#. testGetTest3 .s");
        WebResource resource = getWebResource();
        ClientResponse response = null;
        try {
            TestBean bean = new TestBean();
            bean.setUserId("getu001");
            bean.setPassword("12345");
            response = resource.path(REST_PATH)//
                    .path("getTest3")//
                    .type(MediaType.APPLICATION_XML)//
                    .post(ClientResponse.class, bean);//
            errorHandler(response);
            boolean resultOK = false;
            if (200 == response.getStatus() || 204 == response.getStatus()) {
                resultOK = true;
                ObjectMapper mapper = new ObjectMapper();
                TestResult result = mapper.readValue(response.getEntity(String.class), TestResult.class);
                logger.info("result = " + ReflectionToStringBuilder.toString(result));
            }
            Assert.assertTrue(resultOK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            Assert.assertTrue(false);
        }
    }
    
    @Test
    public void testGetTest4() {
        logger.info("#. testGetTest4 .s");
        WebResource resource = getWebResource();
        ClientResponse response = null;
        try {
            MultivaluedMap<String, String> queryParams = new MultivaluedMapImpl();
            queryParams.add("testStr", "哈哈哈");
            TestBean bean = new TestBean();
            bean.setUserId("getu001");
            bean.setPassword("12345");
            response = resource.path(REST_PATH)//
                    .path("getTest4")//
                    .queryParams(queryParams)//
                    .type(MediaType.APPLICATION_XML)//
                    .post(ClientResponse.class, bean);//
            errorHandler(response);
            boolean resultOK = false;
            if (200 == response.getStatus() || 204 == response.getStatus()) {
                resultOK = true;
                ObjectMapper mapper = new ObjectMapper();
                TestResult result = mapper.readValue(response.getEntity(String.class), TestResult.class);
                logger.info("result = " + ReflectionToStringBuilder.toString(result));
            }
            Assert.assertTrue(resultOK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            Assert.assertTrue(false);
        }
    }
    
    @Test
    public void testGetTest_MultipartformData() {
        logger.info("#. testGetTest_MultipartformData .s");
        WebResource resource = getWebResource();
        ClientResponse response = null;
        try {
            File pdfFile = new File("D:/5363.jpg");
            
            TestBean bean = new TestBean();
            bean.setUserId("getu001");
            bean.setPassword("12345");
            
            FileDataBodyPart filePart = new FileDataBodyPart("file", pdfFile);
            
            MultiPart multipartEntity = new FormDataMultiPart()//
                    .field("testStr", "testStrABCDE", MediaType.APPLICATION_JSON_TYPE)//
                    .field("dataBean", bean, MediaType.APPLICATION_JSON_TYPE)//
                    .bodyPart(filePart);//
            
            response = resource.path(REST_PATH)//
                    .path("getTest_MultipartformData")//
                    .type(MediaType.MULTIPART_FORM_DATA_TYPE)//
                    .post(ClientResponse.class, multipartEntity);//
            
            errorHandler(response);
            
            boolean resultOK = false;
            if (200 == response.getStatus() || 204 == response.getStatus()) {
                resultOK = true;
                ObjectMapper mapper = new ObjectMapper();
                TestResult result = mapper.readValue(response.getEntity(String.class), TestResult.class);
                logger.info("result = " + ReflectionToStringBuilder.toString(result));
            }
            Assert.assertTrue(resultOK);
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            Assert.assertTrue(false);
        }
    }
}
