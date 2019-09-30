package com.nordea.openbanking.client.config;

import com.nordea.openbanking.client.session.UserSession;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import static org.springframework.util.StringUtils.isEmpty;

@Slf4j
@Component
@SessionScope
@RequiredArgsConstructor
public class EnvironmentSpecificClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

    private final ObiProxy obiProxy;
    private final UserSession userSession;

    @Override
    public HttpURLConnection openConnection(URL url, @Nullable Proxy proxy) throws IOException {
        URLConnection urlConnection = getConnection(url);
        if (!(urlConnection instanceof HttpURLConnection)) {
            throw new IllegalStateException("HttpURLConnection required for [" + url + "] but got: " + urlConnection);
        } else {
            return (HttpURLConnection) urlConnection;
        }
    }

    private URLConnection getConnection(URL url) throws IOException {
        URL updatedUrl = replaceBasePath(url);
        switch (userSession.getEnvironment()) {
            case "pre-production":
            case "external-portal-sandbox":
            case "production":
                return updatedUrl.openConnection(obiProxy);
            default:
                return updatedUrl.openConnection();
        }
    }

    @SneakyThrows
    private URL replaceBasePath(URL originalUrl) {
        String url = userSession.getBaseUrl() + originalUrl.getPath();
        if (!isEmpty(originalUrl.getQuery())) {
            url += "?" + originalUrl.getQuery();
        }
        return new URL(url);
    }

}
