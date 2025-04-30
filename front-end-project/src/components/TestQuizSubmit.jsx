import { useState } from 'react';
import axios from 'axios';

/**
 * A test component to debug direct API calls to the backend
 * without going through the service layer
 */
const TestQuizSubmit = () => {
  const [result, setResult] = useState(null);
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  
  // Fixed test quiz data with title and description
  const testData = {
    title: "Test Quiz Title",
    description: "This is a test description"
  };
  
  const handleTestSubmit = async () => {
    setLoading(true);
    setError(null);
    setResult(null);
    
    try {
      console.log("Sending test data to API:", JSON.stringify(testData));
      
      const response = await axios.post(
        'http://localhost:8080/api/quizzes', 
        testData,
        {
          headers: {
            'Content-Type': 'application/json'
          }
        }
      );
      
      console.log("API response:", response.data);
      setResult(response.data);
    } catch (err) {
      console.error("Test API call failed:", err);
      setError(err.response?.data?.message || err.message);
    } finally {
      setLoading(false);
    }
  };
  
  return (
    <div className="max-w-md mx-auto p-4 border rounded shadow mt-10">
      <h2 className="text-xl font-bold mb-4">Test Quiz API</h2>
      
      <div className="mb-4 p-3 bg-gray-100 rounded">
        <pre>{JSON.stringify(testData, null, 2)}</pre>
      </div>
      
      <button
        onClick={handleTestSubmit}
        disabled={loading}
        className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 disabled:opacity-50"
      >
        {loading ? "Sending..." : "Test Direct Submit"}
      </button>
      
      {error && (
        <div className="mt-4 p-3 bg-red-100 text-red-700 rounded">
          <strong>Error:</strong> {error}
        </div>
      )}
      
      {result && (
        <div className="mt-4">
          <h3 className="font-semibold">Response:</h3>
          <div className="p-3 bg-green-100 rounded">
            <pre>{JSON.stringify(result, null, 2)}</pre>
          </div>
        </div>
      )}
    </div>
  );
};

export default TestQuizSubmit; 