/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_MAX_LOAN_AMOUNT?: string;
  readonly VITE_MAX_LOAN_TERM_IN_MONTHS?: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
