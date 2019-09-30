package com.nordea.openbanking.client.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler cut;

    @Before
    public void setup() {
        ObjectMapper objectMapper = objectMapper();
        cut = new GlobalExceptionHandler(objectMapper);
    }

    @Test
    public void templateErrorHandler() {
        HttpServletRequest mockRequest = Mockito.mock(HttpServletRequest.class);
        Exception mockException = new IndexOutOfBoundsException("This is just one of those errors");
        ModelAndView modelAndView = cut.templateErrorHandler(mockRequest, mockException);

        assertThat(modelAndView.getModelMap().get("response_status_code"), is("500"));
    }

    @Test
    public void handleClientError_no_JSON_response() {
        WebRequest mockRequest = Mockito.mock(WebRequest.class);
        HttpClientErrorException mockException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "This is just one of those errors");

        ModelAndView modelAndView = cut.handleClientError(mockException, mockRequest);

        assertThat(modelAndView.getModelMap().get("exception"), is(mockException));
    }

    @Test
    public void handleClientError_valid_JSON_response() {
        WebRequest mockRequest = Mockito.mock(WebRequest.class);
        String bodyString = "{ \"group_header\": { \"message_identification\": \"AU9VsU21uKI\", \"creation_date_time\": \"2019-07-04T10:51:38.129Z\", \"http_code\": 401 }," +
                "\"error\": { " +
                "\"request\": { \"url\": \"/v3/payments/domestic\" },\"failures\": [{ \"code\": \"error.token.expired\", \"description\": \"Bearer token is not valid anymore\" }]}}";
        byte[] bodyBytes = bodyString.getBytes(Charset.defaultCharset());
        HttpClientErrorException mockException = HttpClientErrorException.BadRequest.create(HttpStatus.BAD_REQUEST, "statusText", null, bodyBytes, null);

        ModelAndView modelAndView = cut.handleClientError(mockException, mockRequest);

        assertThat(modelAndView.getModelMap().get("exception"), is(mockException));
    }

    //Test Helper

    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

}