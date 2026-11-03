package com.bendigo.commissionquote.api.controller;

import com.bendigo.commissionquote.api.dto.QuoteRequest;
import com.bendigo.commissionquote.api.dto.QuoteResponse;
import com.bendigo.commissionquote.api.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/quotes")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public QuoteResponse createQuote(@Valid @RequestBody QuoteRequest request) {
        return quoteService.createQuote(request);
    }
}
