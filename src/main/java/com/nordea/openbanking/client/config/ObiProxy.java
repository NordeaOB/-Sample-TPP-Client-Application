package com.nordea.openbanking.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.net.Proxy;

import static java.net.Proxy.Type.HTTP;

@Component
public class ObiProxy extends Proxy {

    public ObiProxy(@Value("${obi.proxy.host}") String proxyHost,
                    @Value("${obi.proxy.port}") int proxyPort) {
        super(HTTP, new InetSocketAddress(proxyHost, proxyPort));
    }
}
