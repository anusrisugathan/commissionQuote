package com.bendigo.commissionquote.vendor.service;

import com.bendigo.commissionquote.api.dto.QuoteRequest;
import com.bendigo.commissionquote.api.dto.QuoteResponse;
import com.bendigo.commissionquote.vendor.exception.InvalidVendorApiKeyException;
import com.bendigo.commissionquote.vendor.exception.VendorUnavailableException;
import com.bendigo.commissionquote.vendor.model.RiskBand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleSupplier;

@Service
public class MockVendorService {
    private final double failureRate;
    private final DoubleSupplier random;
    private final byte[] apiKey;

    @Autowired
    public MockVendorService(
            @Value("${mock-vendor.failure-rate:0.2}") double failureRate,
            @Value("${vendor.api.key}") String apiKey
    ) {
        this(failureRate, apiKey, () -> ThreadLocalRandom.current().nextDouble());
    }

    MockVendorService(double failureRate, String apiKey, DoubleSupplier random) {
        if (!Double.isFinite(failureRate) || failureRate < 0 || failureRate > 1) {
            throw new IllegalArgumentException("Mock vendor failure rate must be between 0 and 1.");
        }
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Vendor API key must be configured.");
        }
        this.failureRate = failureRate;
        this.apiKey = apiKey.getBytes(StandardCharsets.UTF_8);
        this.random = random;
    }

    public QuoteResponse createQuote(QuoteRequest request, String suppliedApiKey) {
        if (suppliedApiKey == null || !MessageDigest.isEqual(
                apiKey, suppliedApiKey.getBytes(StandardCharsets.UTF_8))) {
            throw new InvalidVendorApiKeyException();
        }
        if (random.getAsDouble() < failureRate) {
            throw new VendorUnavailableException();
        }

        BigDecimal rate = rateFor(request.riskBand());
        BigDecimal totalCommission = request.loanAmount()
                .multiply(rate)
                .setScale(2, RoundingMode.HALF_UP);
        return new QuoteResponse(UUID.randomUUID().toString(), rate, totalCommission);
    }

    private BigDecimal rateFor(RiskBand riskBand) {
        return switch (riskBand) {
            case LOW -> new BigDecimal("0.0100");
            case MEDIUM -> new BigDecimal("0.0150");
            case HIGH -> new BigDecimal("0.0200");
        };
    }
}
