import type { QuoteRequest, QuoteResponse } from "../types";

function isQuoteResponse(value: unknown): value is QuoteResponse {
  if (typeof value !== "object" || value === null) {
    return false;
  }
  return "quoteId" in value
    && typeof value.quoteId === "string"
    && "commissionRate" in value
    && typeof value.commissionRate === "number"
    && Number.isFinite(value.commissionRate)
    && "totalCommission" in value
    && typeof value.totalCommission === "number"
    && Number.isFinite(value.totalCommission);
}

function getApiErrorMessage(value: unknown): string | undefined {
  return typeof value === "object"
    && value !== null
    && "message" in value
    && typeof value.message === "string"
    ? value.message
    : undefined;
}

export async function generateQuote(request: QuoteRequest): Promise<QuoteResponse> {
  const response = await fetch("/api/quotes", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(request),
    signal: AbortSignal.timeout(15_000),
  });

  let responseBody: unknown;
  try {
    responseBody = await response.json();
  } catch {
    throw new Error("The quote service returned an invalid response.");
  }

  if (!response.ok) {
    throw new Error(
      getApiErrorMessage(responseBody)
        || "We couldn't generate a quote. Please try again.",
    );
  }

  if (!isQuoteResponse(responseBody)) {
    throw new Error("The quote service returned an invalid response.");
  }

  return responseBody;
}

export function getQuoteErrorMessage(error: unknown): string {
  if (error instanceof Error && (error.name === "TimeoutError" || error.name === "AbortError")) {
    return "The request timed out. Please try again.";
  }
  if (error instanceof Error) {
    return error.message || "Unable to reach the quote service. Please try again.";
  }
  return "Unable to reach the quote service. Please try again.";
}
