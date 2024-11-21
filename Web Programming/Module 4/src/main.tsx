import React from "react";
import ReactDOM from "react-dom/client";
import DuckComponent from "./components/DuckComponent";

// Define main App component
function App() {
  return (
    <div>
      <h1>Duck Counter</h1>
      <DuckComponent />
    </div>
  );
}
// Render the actual content.
// Creates a React root container.
// Locate the div with id = root
// Tells react what to put in that container
ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);