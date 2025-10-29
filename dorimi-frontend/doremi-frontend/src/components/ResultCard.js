import React from "react";
import "./result.css";

function ResultCard({ result }) {
  if (!result || !result.subscriptions) return null;

  return (
    <div className="result-card">
      <h2 className="result-title">📄 Subscription Summary</h2>

      {result.subscriptions.map((sub, index) => (
        <div key={index} className="result-section">
          <div className="result-header">
            <span className="result-category">{sub.category}</span>
            <span className="result-plan">— {sub.plan}</span>
          </div>

          <div className="result-details">
            <p>
              <span className="label">📆 Duration:</span> {sub.duration} month(s)
            </p>
            <p>
              <span className="label">🔔 Reminder Date:</span>{" "}
              {sub.renewBefore}
            </p>
            <p>
              <span className="label">⏰ Renewal Date:</span> {sub.renewalDate}
            </p>
            <p>
              <span className="label">💸 Cost:</span> ₹{sub.amount}
            </p>
          </div>
        </div>
      ))}

      <div className="result-footer">
        <h3 className="total-amount">
          💰 Total Renewal Amount: ₹{result.total}
        </h3>
      </div>
    </div>
  );
}

export default ResultCard;
