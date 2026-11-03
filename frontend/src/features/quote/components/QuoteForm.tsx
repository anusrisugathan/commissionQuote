import type { FormEvent } from "react";
import {
  currencyFormatter,
  getLoanLimitError,
  getLoanTermMaximumHint,
  MAX_LOAN_AMOUNT,
  MAX_LOAN_TERM_IN_MONTHS,
} from "../../../config/loanLimits";
import type { QuoteFormValues } from "../types";

interface QuoteFormProps {
  values: QuoteFormValues;
  error: string;
  isLoading: boolean;
  onValuesChange: (values: QuoteFormValues) => void;
  onErrorChange: (error: string) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
}

export function QuoteForm({
  values,
  error,
  isLoading,
  onValuesChange,
  onErrorChange,
  onSubmit,
}: QuoteFormProps) {
  function updateAmount(amount: string) {
    onValuesChange({ ...values, loanAmount: amount });
    onErrorChange(getLoanLimitError(amount, values.loanTermInMonths));
  }

  function updateTerm(term: string) {
    onValuesChange({ ...values, loanTermInMonths: term });
    onErrorChange(getLoanLimitError(values.loanAmount, term));
  }

  return (
    <form className="form-card" onSubmit={onSubmit}>
      <div className="card-heading">
        <div>
          <h2>Loan details</h2>
          <p>All fields are required</p>
        </div>
        <span className="step-label">QUOTE DETAILS</span>
      </div>

      <label htmlFor="loanAmount">Loan amount</label>
      <div className="input-wrap amount-wrap">
        <span aria-hidden="true">$</span>
        <input
          id="loanAmount"
          name="loanAmount"
          type="number"
          min="0.01"
          max={MAX_LOAN_AMOUNT}
          step="0.01"
          placeholder="e.g. 450,000"
          value={values.loanAmount}
          onChange={(event) => updateAmount(event.target.value)}
          required
        />
      </div>
      <p className="field-hint">AUD · Maximum {currencyFormatter.format(MAX_LOAN_AMOUNT)}</p>

      <div className="field-row">
        <div>
          <label htmlFor="loanTermInMonths">Loan term</label>
          <div className="input-wrap term-wrap">
            <input
              id="loanTermInMonths"
              name="loanTermInMonths"
              type="number"
              min="1"
              max={MAX_LOAN_TERM_IN_MONTHS}
              step="1"
              placeholder="e.g. 360"
              value={values.loanTermInMonths}
              onChange={(event) => updateTerm(event.target.value)}
              required
            />
            <span>months</span>
          </div>
          <p className="field-hint limit-hint">{getLoanTermMaximumHint()}</p>
        </div>
        <div>
          <label htmlFor="riskBand">Risk band</label>
          <select
            id="riskBand"
            name="riskBand"
            value={values.riskBand}
            onChange={(event) => onValuesChange({
              ...values,
              riskBand: event.target.value as QuoteFormValues["riskBand"],
            })}
          >
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
          </select>
        </div>
      </div>

      {error && <div className="error-message" role="alert">{error}</div>}

      <button className="submit-button" type="submit" disabled={isLoading}>
        {isLoading ? (
          <><span className="spinner" aria-hidden="true" /> Generating quote...</>
        ) : (
          <>Generate quote <span aria-hidden="true">→</span></>
        )}
      </button>
      <p className="secure-note"><span aria-hidden="true">▣</span> Your loan details are handled securely</p>
    </form>
  );
}
