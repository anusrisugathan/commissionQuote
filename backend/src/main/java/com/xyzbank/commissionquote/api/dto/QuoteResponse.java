package com.xyzbank.commissionquote.api.dto;

import java.math.BigDecimal;

public record QuoteResponse(
        String quoteId,
        BigDecimal commissionRate,
        BigDecimal totalCommission
) {
}
