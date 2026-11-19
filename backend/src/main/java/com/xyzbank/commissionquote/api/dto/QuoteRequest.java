package com.xyzbank.commissionquote.api.dto;

import com.xyzbank.commissionquote.vendor.model.RiskBand;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record QuoteRequest(
        @NotNull
        @DecimalMin(value = "0.0", inclusive = false)
        @DecimalMax(value = "1000000000", message = "Loan amount cannot exceed 1,000,000,000.")
        BigDecimal loanAmount,
        @NotNull
        @Min(1)
        @Max(value = 600, message = "Loan term cannot exceed 600 months.")
        Integer loanTermInMonths,
        @NotNull RiskBand riskBand
) {
}
