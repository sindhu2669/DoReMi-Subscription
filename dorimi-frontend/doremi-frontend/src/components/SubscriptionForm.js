import React, { useState } from "react";
import axios from "axios";
import "./form.css";

function SubscriptionForm({ setResult }) {
  const [formData, setFormData] = useState({
    startDate: "",
    category: "MUSIC",
    planName: "FREE",
    topUpName: "",
    topUpMonths: 0,
  });

  const planOptions = {
    MUSIC: [
      { name: "FREE", cost: 0 },
      { name: "PERSONAL", cost: 100 },
      { name: "PREMIUM", cost: 250 },
    ],
    VIDEO: [
      { name: "FREE", cost: 0 },
      { name: "PERSONAL", cost: 200 },
      { name: "PREMIUM", cost: 500 },
    ],
    PODCAST: [
      { name: "FREE", cost: 0 },
      { name: "PERSONAL", cost: 100 },
      { name: "PREMIUM", cost: 300 },
    ],
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    const dateParts = formData.startDate.split("-");
    const formattedDate = `${dateParts[2]}-${dateParts[1]}-${dateParts[0]}`;

    const payload = {
      startDate: formattedDate,
      subscriptions: { [formData.category]: formData.planName },
      topUp: formData.topUpName || null,
      topUpMonths: Number(formData.topUpMonths) || 0,
    };

    try {
      const res = await axios.post(
        "http://localhost:8080/api/subscriptions/calculate",
        payload
      );
      setResult(res.data);
    } catch {
      setResult({ error: "Error calculating subscription." });
    }
  };

  return (
    <form className="subscription-form" onSubmit={handleSubmit}>
      <h2>🎵 DoReMi Subscription</h2>

      <label>Start Date:</label>
      <input
        type="date"
        required
        value={formData.startDate}
        onChange={(e) => setFormData({ ...formData, startDate: e.target.value })}
      />

      <label>Category:</label>
      <select
        value={formData.category}
        onChange={(e) => setFormData({ ...formData, category: e.target.value })}
      >
        <option value="MUSIC">MUSIC</option>
        <option value="VIDEO">VIDEO</option>
        <option value="PODCAST">PODCAST</option>
      </select>

      <label>Plan Type:</label>
      <select
        value={formData.planName}
        onChange={(e) => setFormData({ ...formData, planName: e.target.value })}
      >
        {planOptions[formData.category].map((plan) => (
          <option key={plan.name} value={plan.name}>
            {plan.name} ({plan.cost} INR)
          </option>
        ))}
      </select>

      <label>Top-Up (Optional):</label>
      <select
        value={formData.topUpName}
        onChange={(e) => setFormData({ ...formData, topUpName: e.target.value })}
      >
        <option value="">None</option>
        <option value="FOUR_DEVICE">FOUR_DEVICE (₹50/mo)</option>
        <option value="TEN_DEVICE">TEN_DEVICE (₹100/mo)</option>
      </select>

      <label>Top-Up Duration (Months):</label>
      <input
        type="number"
        min="0"
        max="12"
        value={formData.topUpMonths}
        onChange={(e) => setFormData({ ...formData, topUpMonths: e.target.value })}
      />

      <button type="submit">Calculate Subscription</button>
    </form>
  );
}

export default SubscriptionForm;
