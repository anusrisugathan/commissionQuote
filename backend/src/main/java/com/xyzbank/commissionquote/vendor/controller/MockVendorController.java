package com.xyzbank.commissionquote.vendor.controller;

import com.xyzbank.commissionquote.api.dto.QuoteRequest;
import com.xyzbank.commissionquote.api.dto.QuoteResponse;
import com.xyzbank.commissionquote.vendor.service.MockVendorService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mock-vendor/quotes")
public class MockVendorController {
    public static final String API_KEY_HEADER = "api-key";

    private final MockVendorService mockVendorService;

    public MockVendorController(MockVendorService mockVendorService) {
        this.mockVendorService = mockVendorService;
    }

    @PostMapping
    public QuoteResponse createQuote(
            @Valid @RequestBody QuoteRequest request,
            @RequestHeader(value = API_KEY_HEADER, required = false) String apiKey) {
        return mockVendorService.createQuote(request, apiKey);
    }
}
