# Commission Quote

This is a web application that allows a user to input loan details and displays the generated commission quote, along with a mock version of
a third-party vendor Commission Quote API. 

A React based user interface and Spring Boot APIs are used for generating commission quotes. The Spring application exposes the application endpoint and a separately protected mock vendor endpoint. The application API calls the mock vendor over HTTP using the configured API key.

## Requirements

- Java 17+
- Node.js 20+

## Run locally

1. Build the Spring Boot backend from the project root. Its source code is under `backend/src`:

   ```sh
   ./gradlew build
   ```

   On Windows, use `gradlew.bat build`.

2. Build the React frontend:

   ```sh
   cd frontend
   npm install
   npm run build
   ```

3. Start the Spring Boot API from the project root:

   ```sh
   ./gradlew bootRun
   ```

   On Windows, use `gradlew.bat bootRun`.

4. In another terminal, start the React UI:

   ```sh
   cd frontend
   npm run dev
   ```

5. Open the Vite URL shown in the terminal (normally `http://localhost:5173`).

The UI sends requests to `POST /api/quotes`. The mock vendor is available at `POST /mock-vendor/quotes` and requires an `api-key` header. The local development key is `local-dev-vendor-key`; the browser never receives this key.

## Backend structure

Backend Java code is grouped by responsibility under `backend/src/main/java/com/bendigo/commissionquote`:

- `api/controller`: application quote endpoint
- `api/service`: application quote orchestration
- `api/dto`: API request, response, and error types
- `api/exception`: application-wide REST exception handling
- `vendor/controller`: mock vendor HTTP endpoint
- `vendor/service`: mock quote generation and API-key verification
- `vendor/client`: HTTP client used by the application API to call the vendor
- `vendor/model`: vendor data enums
- `vendor/exception`: vendor-specific exceptions

## Configuration

| Variable | Default | Description |
| --- | --- | --- |
| `SERVER_PORT` | `8080` | Spring Boot HTTP port |
| `VENDOR_API_BASE_URL` | `http://localhost:8080` | Base URL used by the backend vendor client |
| `VENDOR_API_KEY` | `local-dev-vendor-key` | API key sent by the backend and required by the mock |
| `VENDOR_API_CONNECT_TIMEOUT` | `2s` | Backend vendor connection timeout |
| `VENDOR_API_READ_TIMEOUT` | `5s` | Backend vendor response timeout |
| `MOCK_VENDOR_FAILURE_RATE` | `0.1` | Probability (0–1) that an authenticated mock request returns HTTP 503 |
| `VITE_MAX_LOAN_AMOUNT` | `1000000000` | Frontend maximum loan amount in AUD |
| `VITE_MAX_LOAN_TERM_IN_MONTHS` | `600` | Frontend maximum loan term in months |

Set `MOCK_VENDOR_FAILURE_RATE=0` for stable local demos. Do not use the development API key in a deployed environment.

The frontend limits are set in `frontend/.env` and read by Vite when it starts/builds. These `VITE_` values are included in the frontend bundle, so they must not contain secrets. The backend independently enforces a maximum loan amount of AUD 1,000,000,000 and a maximum term of 600 months (50 years), regardless of frontend settings.

## Frontend structure

The React + TypeScript frontend lives in `frontend/src`:

- `app`: top-level application shell
- `features/quote`: quote page, form/result components, API client, and quote types
- `config`: frontend loan limits and formatting
- `shared/styles`: global responsive styles

`npm run build` runs the TypeScript type check before building the Vite bundle.

## API contract

Request to `POST /api/quotes` (the mock endpoint accepts the same body):

```json
{
  "loanAmount": 250000,
  "loanTermInMonths": 360,
  "riskBand": "MEDIUM"
}
```

Successful response:

```json
{
  "quoteId": "a-unique-quote-reference",
  "commissionRate": 0.015,
  "totalCommission": 3750.00
}
```

The mock uses rates of 1% for `LOW`, 1.5% for `MEDIUM`, and 2% for `HIGH`; total commission is loan amount multiplied by the rate, rounded to two decimal places. Loan amount must be positive and no more than AUD 1,000,000,000; loan term must be between 1 and 600 months. The frontend displays an alert when a configured limit is exceeded, and the backend validates the limits independently. The vendor returns 401 for a missing or invalid API key, and may return 503 according to the configured failure rate. The application API returns a safe error response for invalid input, vendor errors, or connection timeouts.

## Tests

Run backend tests from the project root:

```sh
./gradlew test
```

On Windows, use `gradlew.bat test`. The vendor service tests cover quote calculation, API-key rejection, and deterministic simulated failure.

To build the frontend bundle:

```sh
cd frontend
npm run build
```

## AI Usage
Copilot was used for creating the project structure, tests and documentation.
