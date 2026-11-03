function positiveNumberOrDefault(value: string | undefined, fallback: number): number {
  const parsed = Number(value);
  return Number.isFinite(parsed) && parsed > 0 ? parsed : fallback;
}

function positiveIntegerOrDefault(value: string | undefined, fallback: number): number {
  const parsed = Number(value);
  return Number.isSafeInteger(parsed) && parsed > 0 ? parsed : fallback;
}

export const MAX_LOAN_AMOUNT = positiveNumberOrDefault(
  import.meta.env.VITE_MAX_LOAN_AMOUNT,
  1_000_000_000,
);

export const MAX_LOAN_TERM_IN_MONTHS = positiveIntegerOrDefault(
  import.meta.env.VITE_MAX_LOAN_TERM_IN_MONTHS,
  600,
);

export const currencyFormatter = new Intl.NumberFormat("en-AU", {
  style: "currency",
  currency: "AUD",
});

export function getLoanLimitError(amount: string, term: string): string {
  if (amount && Number(amount) > MAX_LOAN_AMOUNT) {
    return `Loan amount cannot exceed ${currencyFormatter.format(MAX_LOAN_AMOUNT)}.`;
  }

  if (term && Number(term) > MAX_LOAN_TERM_IN_MONTHS) {
    const years = MAX_LOAN_TERM_IN_MONTHS % 12 === 0
      ? ` (${MAX_LOAN_TERM_IN_MONTHS / 12} years)`
      : "";
    return `Loan term cannot exceed ${MAX_LOAN_TERM_IN_MONTHS} months${years}.`;
  }

  return "";
}

export function getLoanTermMaximumHint(): string {
  const years = MAX_LOAN_TERM_IN_MONTHS % 12 === 0
    ? ` (${MAX_LOAN_TERM_IN_MONTHS / 12} years)`
    : "";
  return `Maximum ${MAX_LOAN_TERM_IN_MONTHS} months${years}`;
}
