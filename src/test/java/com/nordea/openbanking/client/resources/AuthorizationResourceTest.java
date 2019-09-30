package com.nordea.openbanking.client.resources;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthorizationResourceTest {

    @LocalServerPort
    int serverPort;
    private static String BASEURL;

    @Autowired
    private WebTestClient client;

    @Before
    public void setUp() {
        BASEURL = "http://localhost:" + serverPort;

        client = WebTestClient
                .bindToServer()
                .baseUrl(BASEURL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Test
    public void getStartAuthorization() {
        client.get().uri("/startAuthorization")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("location", BASEURL + "/authorize/login");
    }

    @Test
    public void getLogin() {
        client.get().uri("/login")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("location", BASEURL + "/authorize/login");
    }

    @Test
    public void PostStartAuthorization() {
        client.post().uri("/startAuthorization")
                .exchange()
                .expectStatus().is3xxRedirection()
                .expectHeader().valueEquals("location", BASEURL + "/authorize/login");
    }
}