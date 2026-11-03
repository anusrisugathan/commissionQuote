import { QuotePage } from "../features/quote/QuotePage";

export default function App() {
  return (
    <main className="page-shell">
      <header className="topbar">
        <a className="brand" href="/" aria-label="Lending home">
          <span className="brand-mark" aria-hidden="true">L</span>
          <span>lending<span className="brand-light"> / commissions</span></span>
        </a>
        <span className="environment-label"><span /> QUOTE WORKSPACE</span>
      </header>

      <section className="content">
        <QuotePage />
        <footer>Commission Quote <span>·</span> Lending Platform</footer>
      </section>
    </main>
  );
}
