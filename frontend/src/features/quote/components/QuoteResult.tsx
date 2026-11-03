import { currencyFormatter } from "../../../config/loanLimits";
import type { QuoteResponse } from "../types";

interface QuoteResultProps {
  quote: QuoteResponse | null;
}

export function QuoteResult({ quote }: QuoteResultProps) {
  return (
    <aside className={`result-card${quote ? " has-quote" : ""}`} aria-live="polite">
      {quote ? (
        <>
          <div className="result-icon success-icon" aria-hidden="true">✓</div>
          <div className="result-kicker">QUOTE READY</div>
          <h2>Your commission</h2>
          <p className="result-amount">{currencyFormatter.format(quote.totalCommission)}</p>
          <div className="result-details">
            <div><span>Commission rate</span><strong>{(quote.commissionRate * 100).toFixed(2)}%</strong></div>
            <div><span>Quote reference</span><strong className="quote-reference">{quote.quoteId}</strong></div>
          </div>
          <p className="result-footnote">Estimate based on the loan details provided.</p>
        </>
      ) : (
        <>
          <div className="result-icon" aria-hidden="true">↗</div>
          <div className="result-kicker">YOUR QUOTE</div>
          <h2>Ready when you are</h2>
          <p className="empty-copy">
            Complete the loan details and generate a quote to see the commission estimate here.
          </p>
          <div className="result-divider" />
          <div className="result-footnote">Quotes are estimates and may be subject to review.</div>
        </>
      )}
    </aside>
  );
}
