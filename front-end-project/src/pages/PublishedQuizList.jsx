import React from 'react';
import useQuizzes from '../hooks/useQuizzes';

/**
 * Component that displays only published quizzes.
 * Follows Single Responsibility Principle by focusing only on rendering published quizzes.
 * Uses the useQuizzes hook to separate data fetching from UI rendering.
 */
const PublishedQuizList = () => {
  // Use custom hook with onlyPublished=true
  const { quizzes, loading, error, refetch } = useQuizzes({ onlyPublished: true });

  const handleRefresh = () => {
    refetch();
  };

  if (loading) {
    return <div className="loading-container">Loading published quizzes...</div>;
  }

  if (error) {
    return (
      <div className="error-container">
        <p>{error}</p>
        <button 
          onClick={handleRefresh}
          className="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded"
        >
          Try Again
        </button>
      </div>
    );
  }

  if (quizzes.length === 0) {
    return <div className="empty-container">No published quizzes available.</div>;
  }

  return (
    <div className="published-quizzes-container">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Published Quizzes</h1>
        <button 
          onClick={handleRefresh}
          className="bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-2 px-4 rounded"
        >
          Refresh
        </button>
      </div>
      
      <ul className="quiz-list grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {quizzes.map(quiz => (
          <li key={quiz.id} className="quiz-item bg-white p-4 mb-3 rounded shadow">
            <h2 className="text-lg font-semibold">{quiz.title}</h2>
            <p className="text-gray-600">{quiz.description || 'No description provided'}</p>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default PublishedQuizList; 