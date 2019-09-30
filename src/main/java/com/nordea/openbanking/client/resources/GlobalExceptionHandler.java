package com.nordea.openbanking.client.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordea.openbanking.client.model.ExternalErrorResponse;
import com.nordea.openbanking.client.model.ExternalErrorResponseV2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Takes care of all errors that could be showing up, both through the Application it self and the Nordea Openbanking API
 *
 * @author Jan Nielsen
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String RESPONSE_LITERAL = "response";
    private static final String EXCEPTION_LITERAL = "exception";

    ObjectMapper objectMapper;

    public GlobalExceptionHandler(@Autowired ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler({RuntimeException.class, HttpServerErrorException.InternalServerError.class})
    public ModelAndView templateErrorHandler(HttpServletRequest request, Exception e) {
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        mav.addObject("response_string", "No response");
        mav.addObject(RESPONSE_LITERAL, null);
        mav.addObject("response_status_code", "500");
        mav.addObject("raw_request", request);
        mav.addObject(EXCEPTION_LITERAL, e);
        log.error("Some exception happened: ", e);
        mav.addObject("url", request.getRequestURI());
        return mav;
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ModelAndView handleClientError(RuntimeException ex, WebRequest request) {
        log.error("Some exception happened: ", ex);
        ModelAndView mav = new ModelAndView(DEFAULT_ERROR_VIEW);
        String responseBodyAsString = ((HttpClientErrorException) ex).getResponseBodyAsString();
        if (responseBodyAsString.isEmpty()) {
            mav.addObject(EXCEPTION_LITERAL, ex);
        } else {
            try {
                if (responseBodyAsString.contains("/v2")) {

                    mav.addObject(RESPONSE_LITERAL, objectMapper.readValue(responseBodyAsString, ExternalErrorResponseV2.class));
                } else {
                    mav.addObject(RESPONSE_LITERAL, objectMapper.readValue(responseBodyAsString, ExternalErrorResponse.class));
                }
            } catch (IOException e) {
                log.warn("Could not map response", e);
                throw HttpServerErrorException.InternalServerError.create(HttpStatus.BAD_REQUEST, "Could not map response", new HttpHeaders(), new byte['a'], null);
            }

            mav.addObject("response_string", responseBodyAsString);
            mav.addObject("response_status_code", ((HttpClientErrorException) ex).getRawStatusCode());
            mav.addObject("raw_request", request);
            mav.addObject(EXCEPTION_LITERAL, ex);
            mav.addObject("url", request.getContextPath());
        }
        return mav;
    }

}
