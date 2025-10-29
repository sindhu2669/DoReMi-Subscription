import React, { useState, useRef } from "react";
import SubscriptionForm from "./components/SubscriptionForm";
import ResultCard from "./components/ResultCard";
import "./components/form.css";
import "./components/result.css";
import "./App.css";

function App() {
  const [result, setResult] = useState(null);
  const resultRef = useRef(null);

  const handleResult = (data) => {
    setResult(data);
    // 👇 Automatically scroll to result when updated
    setTimeout(() => {
      if (resultRef.current) {
        resultRef.current.scrollIntoView({ behavior: "smooth", block: "center" });
      }
    }, 200);
  };

  return (
    <div className="app-container">
      <div className="content-wrapper">
        <SubscriptionForm setResult={handleResult} />
        {result && (
          <div ref={resultRef}>
            <ResultCard result={result} />
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
