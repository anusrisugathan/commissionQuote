import { useState } from "react";
import type { FormEvent } from "react";
import { getLoanLimitError } from "../../config/loanLimits";
import { generateQuote, getQuoteErrorMessage } from "./api/quoteApi";
import { QuoteForm } from "./components/QuoteForm";
import { QuoteResult } from "./components/QuoteResult";
import type { QuoteFormValues, QuoteResponse } from "./types";

const INITIAL_VALUES: QuoteFormValues = {
  loanAmount: "",
  loanTermInMonths: "",
  riskBand: "LOW",
};

export function QuotePage() {
  const [values, setValues] = useState<QuoteFormValues>(INITIAL_VALUES);
  const [quote, setQuote] = useState<QuoteResponse | null>(null);
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const limitError = getLoanLimitError(values.loanAmount, values.loanTermInMonths);
    if (limitError) {
      setError(limitError);
      return;
    }

    setError("");
    setQuote(null);
    setIsLoading(true);
    try {
      const generatedQuote = await generateQuote({
        loanAmount: Number(values.loanAmount),
        loanTermInMonths: Number(values.loanTermInMonths),
        riskBand: values.riskBand,
      });
      setQuote(generatedQuote);
    } catch (requestError: unknown) {
      setError(getQuoteErrorMessage(requestError));
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <>
      <div className="eyebrow"><span /> LOAN TOOLS</div>
      <h1>Commission quote</h1>
      <p className="intro">Enter the loan details to generate an estimated commission.</p>
      <div className="workspace">
        <QuoteForm
          values={values}
          error={error}
          isLoading={isLoading}
          onValuesChange={setValues}
          onErrorChange={setError}
          onSubmit={handleSubmit}
        />
        <QuoteResult quote={quote} />
      </div>
    </>
  );
}
