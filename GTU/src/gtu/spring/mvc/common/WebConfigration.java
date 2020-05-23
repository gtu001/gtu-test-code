package gtu.spring.mvc.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.log.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebConfigration {

    private static final Logger logger = null;

    @Autowired
    private Environment env;

    @Bean
    public RestTemplate restTemplate() {
        int timeoutTime = Integer.parseInt(env.getProperty("custom.timeout"));
        logger.debug("!" + "custom.timeout = " + timeoutTime);

        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        clientHttpRequestFactory.setConnectTimeout(timeoutTime);
        clientHttpRequestFactory.setReadTimeout(timeoutTime);

        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(clientHttpRequestFactory));// new
                                                                                                                      // HttpComponentsClientHttpRequestFactory
                                                                                                                      // ()
        restTemplate.getInterceptors().add(new RestTemplateLoggingInterceptor());
        return restTemplate;
    }

    public static class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            traceRequest(request, body);
            ClientHttpResponse response = null;
            try {
                response = execution.execute(request, body);
            } catch (Throwable t) {
                throw t;
            } finally {
                traceResponse(response);
            }
            return response;
        }

        private String getBodyAsJson(String bodyString) {
            if (bodyString == null || bodyString.length() == 0) {
                return null;
            } else {
                if (isValidJSON(bodyString)) {
                    return bodyString;
                } else {
                    bodyString.replaceAll("\"", "\\\"");
                    return "\"" + bodyString + "\"";
                }
            }
        }

        private HttpHeaders getRequestHeaders(HttpRequest request) {
            if (request != null) {
                return request.getHeaders();
            } else {
                return null;
            }
        }

        private String getRequestType(HttpRequest request) {
            if (request != null && request.getMethod() != null) {
                return request.getMethod().toString();
            } else {
                return null;
            }
        }

        private String getRequestUrl(HttpRequest request) {
            if (request != null && request.getURI() != null) {
                return request.getURI().toString();
            } else {
                return null;
            }
        }

        private String getRequestBody(byte[] body) throws UnsupportedEncodingException {
            if (body != null && body.length > 0) {
                return getBodyAsJson(new String(body, "UTF-8"));
            } else {
                return null;
            }
        }

        private void traceRequest(HttpRequest request, byte[] body) throws IOException {
            logger.debug("request URI : " + request.getURI());
            logger.debug("request header : " + ((MultiValueMap<String, String>) request.getHeaders()));
            logger.debug("request method : " + request.getMethod());
            logger.debug("request body : " + getRequestBody(body));
        }

        private String getStatusCode(ClientHttpResponse response) throws IOException {
            if (response != null && response.getStatusCode() != null) {
                return response.getStatusCode().toString();
            } else {
                return null;
            }
        }

        private void traceResponse(ClientHttpResponse response) throws IOException {
            String body = getBodyString(response);
            logger.debug("response status code: " + response.getStatusCode());
            logger.debug("response status text: " + response.getStatusText());
            logger.debug("response body : " + body);
        }

        private String getBodyString(ClientHttpResponse response) {
            try {
                if (response != null && response.getBody() != null && isReadableResponse(response)) {
                    StringBuilder inputStringBuilder = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
                    String line = bufferedReader.readLine();
                    while (line != null) {
                        inputStringBuilder.append(line);
                        inputStringBuilder.append('\n');
                        line = bufferedReader.readLine();
                    }
                    String respBody = inputStringBuilder.toString();
                    logger.debug("[getBodyString] response body : " + respBody);
                    return respBody;
                } else {
                    logger.debug("[getBodyString] response body : " + "NULL");
                    return null;
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        private boolean isReadableResponse(ClientHttpResponse response) {
            for (String contentType : response.getHeaders().get("Content-Type")) {
                if (isReadableContentType(contentType)) {
                    return true;
                }
            }
            return false;
        }

        private boolean isReadableContentType(String contentType) {
            return contentType.startsWith("application/json") || contentType.startsWith("text");
        }

        public boolean isValidJSON(final String json) {
            boolean valid = false;
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.readTree(json);
            } catch (IOException e) {
                valid = false;
            }
            return valid;
        }
    }
}
