export type RiskBand = "LOW" | "MEDIUM" | "HIGH";

export interface QuoteRequest {
  loanAmount: number;
  loanTermInMonths: number;
  riskBand: RiskBand;
}

export interface QuoteResponse {
  quoteId: string;
  commissionRate: number;
  totalCommission: number;
}

export interface QuoteFormValues {
  loanAmount: string;
  loanTermInMonths: string;
  riskBand: RiskBand;
}
