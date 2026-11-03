package com.bendigo.commissionquote.api.dto;

import com.bendigo.commissionquote.vendor.model.RiskBand;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class QuoteRequestValidationTest {
    @Test
    void acceptsMaximumLoanAmountAndTerm() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            QuoteRequest request = new QuoteRequest(
                    new BigDecimal("1000000000"),
                    600,
                    RiskBand.LOW
            );

            assertTrue(validator.validate(request).isEmpty());
        }
    }

    @Test
    void rejectsLoanAmountAboveMaximum() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            QuoteRequest request = new QuoteRequest(
                    new BigDecimal("1000000000.01"),
                    600,
                    RiskBand.LOW
            );

            assertTrue(validator.validate(request).stream()
                    .anyMatch(violation -> violation.getPropertyPath().toString().equals("loanAmount")));
        }
    }

    @Test
    void rejectsLoanTermAboveMaximum() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = factory.getValidator();
            QuoteRequest request = new QuoteRequest(
                    new BigDecimal("1000000000"),
                    601,
                    RiskBand.LOW
            );

            assertTrue(validator.validate(request).stream()
                    .anyMatch(violation -> violation.getPropertyPath().toString().equals("loanTermInMonths")));
        }
    }
}
