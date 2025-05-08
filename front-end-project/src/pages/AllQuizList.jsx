import React from 'react';
import { Link } from 'react-router-dom';
import useQuizzes from '../hooks/useQuizzes';

/**
 * Component that displays all quizzes (both published and unpublished).
 * Uses the useQuizzes hook to separate data fetching from UI rendering.
 */
const AllQuizList = () => {
  // Use custom hook with onlyPublished=false to get all quizzes
  const { quizzes, loading, error, refetch } = useQuizzes({ onlyPublished: false });

  const handleRefresh = () => {
    refetch();
  };

  if (loading) {
    return <div className="loading-container">Loading quizzes...</div>;
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
    return (
      <div className="empty-container">
        <p>No quizzes available.</p>
        <Link to="/quizzes/new" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
          Create New Quiz
        </Link>
      </div>
    );
  }

  return (
    <div className="all-quizzes-container">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">All Quizzes</h1>
        <div className="flex space-x-2">
          <button 
            onClick={handleRefresh}
            className="bg-gray-200 hover:bg-gray-300 text-gray-800 font-bold py-2 px-4 rounded"
          >
            Refresh
          </button>
          <Link to="/quizzes/new" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Create New Quiz
          </Link>
        </div>
      </div>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {quizzes.map(quiz => (
          <div 
            key={quiz.id} 
            className="border rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-shadow"
          >
            <Link to={`/quizzes/${quiz.id}`} className="block">
              <div className="p-4">
                <h2 className="text-xl font-semibold mb-2">{quiz.title}</h2>
                <p className="text-gray-600 mb-4">{quiz.description || 'No description provided'}</p>
                <div className="flex justify-between items-center">
                  <span className={`text-sm px-2 py-1 rounded ${quiz.published ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                    {quiz.published ? 'Published' : 'Draft'}
                  </span>
                  <span className="text-xs text-gray-500">
                    {quiz.createdAt ? new Date(quiz.createdAt).toLocaleDateString() : ''}
                  </span>
                </div>
              </div>
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default AllQuizList; 