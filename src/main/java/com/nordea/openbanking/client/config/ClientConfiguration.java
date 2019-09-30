package com.nordea.openbanking.client.config;

import com.nordea.openbanking.client.client.ObiHttpRequestInterceptor;
import com.nordea.openbanking.v3.pis.sepa.api.PaymentsV3Api;
import com.nordea.openbanking.v4.pis.sepa.api.PaymentsV4Api;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.SessionScope;

import static java.util.Collections.singletonList;

@Configuration
public class ClientConfiguration {

    @Bean
    @SessionScope
    public RestTemplate restResourceTemplate(ObiHttpRequestInterceptor obiSecurityHeadersInterceptor,
                                             EnvironmentSpecificClientHttpRequestFactory factory) {
        RestTemplate template = new RestTemplate(factory);
        template.setInterceptors(singletonList(obiSecurityHeadersInterceptor));
        return template;
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v2.auth.api.AuthorizationApi authV2Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v2.auth.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v2.auth.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v2.auth.api.AuthorizationApi(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v1.business.auth.api.AuthorizeV1Api authV1CorporateClient(RestTemplate restTemplate) {
        com.nordea.openbanking.v1.business.auth.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v1.business.auth.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v1.business.auth.api.AuthorizeV1Api(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v2.auth.api.DecoupledAuthorizationApi authDecoupledV2Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v2.auth.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v2.auth.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v2.auth.api.DecoupledAuthorizationApi(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v3.auth.api.AuthorizationApi authV3Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v3.auth.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v3.auth.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v3.auth.api.AuthorizationApi(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v3.auth.api.DecoupledAuthorizationApi authDecoupledV3Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v3.auth.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v3.auth.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v3.auth.api.DecoupledAuthorizationApi(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v4.auth.api.AuthorizationApi authV4Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v4.auth.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v4.auth.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v4.auth.api.AuthorizationApi(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v4.auth.api.DecoupledAuthorizationApi authDecoupledV4Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v4.auth.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v4.auth.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v4.auth.api.DecoupledAuthorizationApi(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v1.business.ais.api.AccountsV1Api accountsV1CorporateClient(RestTemplate restTemplate) {
        com.nordea.openbanking.v1.business.ais.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v1.business.ais.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v1.business.ais.api.AccountsV1Api(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v3.ais.api.CustomerAccountsApi accountsV3Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v3.ais.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v3.ais.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v3.ais.api.CustomerAccountsApi(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v4.ais.api.CustomerAccountsApi accountsV4Client(RestTemplate restTemplate) {
        com.nordea.openbanking.v4.ais.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v4.ais.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v4.ais.api.CustomerAccountsApi(apiClient);
    }

    @Bean
    @SessionScope
    public PaymentsV3Api paymentsV3SepaClient(RestTemplate restTemplate) {
        com.nordea.openbanking.v3.pis.sepa.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v3.pis.sepa.invoker.ApiClient(restTemplate);
        return new PaymentsV3Api(apiClient);
    }

    @Bean
    @SessionScope
    public PaymentsV4Api paymentsV4SepaClient(RestTemplate restTemplate) {
        com.nordea.openbanking.v4.pis.sepa.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v4.pis.sepa.invoker.ApiClient(restTemplate);
        return new PaymentsV4Api(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v3.pis.domestic.api.PaymentsV3Api paymentsV3DomesticClient(RestTemplate restTemplate) {
        com.nordea.openbanking.v3.pis.domestic.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v3.pis.domestic.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v3.pis.domestic.api.PaymentsV3Api(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.v4.pis.domestic.api.PaymentsV4Api paymentsV4DomesticClient(RestTemplate restTemplate) {
        com.nordea.openbanking.v4.pis.domestic.invoker.ApiClient apiClient =
                new com.nordea.openbanking.v4.pis.domestic.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.v4.pis.domestic.api.PaymentsV4Api(apiClient);
    }

    @Bean
    @SessionScope
    public com.nordea.openbanking.dynamic.sbx.api.DynamicSandboxApiApi dynamicSBXClient(RestTemplate restTemplate) {
        com.nordea.openbanking.dynamic.sbx.invoker.ApiClient apiClient =
                new com.nordea.openbanking.dynamic.sbx.invoker.ApiClient(restTemplate);
        return new com.nordea.openbanking.dynamic.sbx.api.DynamicSandboxApiApi(apiClient);
    }

}


