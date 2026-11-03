package com.bendigo.commissionquote.api.service;

import com.bendigo.commissionquote.api.dto.QuoteRequest;
import com.bendigo.commissionquote.api.dto.QuoteResponse;
import com.bendigo.commissionquote.vendor.client.VendorQuoteClient;
import org.springframework.stereotype.Service;

@Service
public class QuoteService {
    private final VendorQuoteClient vendorQuoteClient;

    public QuoteService(VendorQuoteClient vendorQuoteClient) {
        this.vendorQuoteClient = vendorQuoteClient;
    }

    public QuoteResponse createQuote(QuoteRequest request) {
        return vendorQuoteClient.createQuote(request);
    }
}
