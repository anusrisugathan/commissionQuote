package com.xyzbank.commissionquote.vendor.service;

import com.xyzbank.commissionquote.api.dto.QuoteRequest;
import com.xyzbank.commissionquote.api.dto.QuoteResponse;
import com.xyzbank.commissionquote.vendor.exception.InvalidVendorApiKeyException;
import com.xyzbank.commissionquote.vendor.exception.VendorUnavailableException;
import com.xyzbank.commissionquote.vendor.model.RiskBand;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MockVendorServiceTest {
    private static final String API_KEY = "test-key";

    @Test
    void createsQuoteForAuthenticatedRequest() {
        MockVendorService service = new MockVendorService(0.1, API_KEY, () -> 0.5);
        QuoteResponse quote = service.createQuote(
                new QuoteRequest(new BigDecimal("250000"), 360, RiskBand.MEDIUM),
                API_KEY
        );

        assertEquals(new BigDecimal("0.0150"), quote.commissionRate());
        assertEquals(new BigDecimal("3750.00"), quote.totalCommission());
    }

    @Test
    void rejectsMissingOrIncorrectApiKey() {
        MockVendorService service = new MockVendorService(0, API_KEY, () -> 0.5);
        QuoteRequest request = new QuoteRequest(new BigDecimal("1000"), 12, RiskBand.LOW);

        assertThrows(InvalidVendorApiKeyException.class, () -> service.createQuote(request, null));
        assertThrows(InvalidVendorApiKeyException.class, () -> service.createQuote(request, "wrong-key"));
    }

    @Test
    void canDeterministicallySimulateVendorFailure() {
        MockVendorService service = new MockVendorService(0.1, API_KEY, () -> 0.05);
        QuoteRequest request = new QuoteRequest(new BigDecimal("1000"), 12, RiskBand.LOW);

        assertThrows(VendorUnavailableException.class, () -> service.createQuote(request, API_KEY));
    }
}
