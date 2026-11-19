package com.xyzbank.commissionquote.vendor.client;

import com.xyzbank.commissionquote.api.dto.QuoteRequest;
import com.xyzbank.commissionquote.api.dto.QuoteResponse;
import com.xyzbank.commissionquote.vendor.controller.MockVendorController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;
import java.net.http.HttpClient;

@Component
public class VendorQuoteClient {
    private final RestClient restClient;
    private final String apiKey;

    public VendorQuoteClient(
            RestClient.Builder builder,
            @Value("${vendor.api.base-url}") String baseUrl,
            @Value("${vendor.api.key}") String apiKey,
            @Value("${vendor.api.connect-timeout:2s}") Duration connectTimeout,
            @Value("${vendor.api.read-timeout:5s}") Duration readTimeout
    ) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);
        this.restClient = builder
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
        this.apiKey = apiKey;
    }

    public QuoteResponse createQuote(QuoteRequest request) {
        return restClient.post()
                .uri("/mock-vendor/quotes")
                .header(MockVendorController.API_KEY_HEADER, apiKey)
                .body(request)
                .retrieve()
                .body(QuoteResponse.class);
    }
}
