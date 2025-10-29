DoReMi Subscription

DoReMi Subscription is a small full-stack application (React frontend + Spring Boot backend) that calculates subscription renewal dates and the total renewal amount for a user-selected set of plans and an optional top-up.

The app implements the business rules for music, video, and podcast subscription plans and a top-up that increases the number of streaming devices. It also computes reminder dates (10 days before each plan renewal).

What the app does

Accepts a subscription start date, one or more subscription categories (MUSIC, VIDEO, PODCAST) with plan names (FREE, PERSONAL, PREMIUM), and an optional top-up (FOUR_DEVICE or TEN_DEVICE) with a number of months.

Calculates for each subscribed category:

plan duration (FREE → 1 month, PERSONAL → 1 month, PREMIUM → 3 months),

renewal date = start date + duration months,

reminder date = renewal date - 10 days.

Calculates the total renewal amount = sum(plan amounts) + top-up cost × months.

Returns subscription items (category, plan, amount, duration, renewal date, reminder date) and the total amount.

Business rules (exact)

Categories: MUSIC, VIDEO, PODCAST.

Plans and prices:

MUSIC — FREE: ₹0 (1 month), PERSONAL: ₹100 (1 month), PREMIUM: ₹250 (3 months).

VIDEO — FREE: ₹0 (1 month), PERSONAL: ₹200 (1 month), PREMIUM: ₹500 (3 months).

PODCAST — FREE: ₹0 (1 month), PERSONAL: ₹100 (1 month), PREMIUM: ₹300 (3 months).

Top-ups:

FOUR_DEVICE — ₹50 per month.

TEN_DEVICE — ₹100 per month.

A user can only choose one plan per category and at most one top-up.

Reminder is always 10 days before the renewal date.

Date format used by backend: dd-MM-yyyy for returned dates. The frontend sends ISO yyyy-MM-dd (HTML date input) and the frontend formats to dd-MM-yyyy before sending.

API (backend)

Base URL (local dev): http://localhost:8080

POST /api/subscriptions/calculate

Request body (JSON) — the frontend sends:

{
  "startDate": "10-02-2025",               // dd-MM-yyyy (frontend formats before sending)
  "subscriptions": { "MUSIC": "PREMIUM" }, // key = category, value = plan
  "topUp": "TEN_DEVICE",                   // optional: "FOUR_DEVICE" | "TEN_DEVICE" | null
  "topUpMonths": 3                         // integer (0 if none)
}


Response (successful) — example:

{
  "subscriptions": [
    {
      "category": "MUSIC",
      "plan": "PREMIUM",
      "amount": 250,
      "duration": 3,
      "renewalDate": "02-01-2026",
      "renewBefore": "23-12-2025"
    }
  ],
  "total": 550,
  "message": "Calculation successful"
}

Frontend behavior

The React form accepts a date via <input type="date">, category, plan, optional top-up and top-up months.

On submit the frontend:

converts yyyy-mm-dd from the date input to dd-MM-yyyy,

posts the payload to /api/subscriptions/calculate,

receives subscription items and total,

displays a ResultCard with:

category, plan, duration (months),

Renewal Reminder (renewalDate),

Reminder: Renew before (renewBefore),

cost and total renewal amount.

The app auto-scrolls or brings the result into view after response so users see results immediately.

Key implementation notes.

Backend date parsing uses LocalDate and DateTimeFormatter to compute start + months - 1 day (renewal date logic in your service) then reminder = renewalDate - 10 days (formatted dd-MM-yyyy).

Plan durations come from a small in-memory map: FREE=1, PERSONAL=1, PREMIUM=3 months.

Plan prices come from a map keyed by category and plan name.

Top-up costs are multiplied by the topUpMonths parameter; if months <= 0 or topUp is null/empty, it is ignored.

How to run locally 
Backend (Spring Boot)

Open the backend folder (the dori or dori/doremi-subscription folder).

Build & run with Maven (from project root where pom.xml is):

./mvnw spring-boot:run      # or: mvn spring-boot:run


Backend runs on http://localhost:8080.

Frontend (React)

Open the frontend folder:

cd dorimi-frontend/doremi-frontend


Install and run:

npm install
npm start


Frontend runs on http://localhost:3000 and calls the backend at http://localhost:8080/api/subscriptions/calculate.

Example (manual check)

Start Date: 10-02-2025

Plan: MUSIC → PREMIUM (3 months, ₹250)

Top-up: TEN_DEVICE for 3 months (₹100 × 3 = ₹300)

Renewal date = 10-02-2025 + 3 months = 02-01-2026
Reminder date = 02-01-2026 - 10 days = 23-12-2025
Total = 250 + 300 = 550
