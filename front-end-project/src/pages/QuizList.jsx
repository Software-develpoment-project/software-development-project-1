import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import quizService from '../services/quizService';

const QuizList = () => {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuizzes = async () => {
      try {
        setLoading(true);
        const data = await quizService.getAllQuizzes();
        setQuizzes(data);
        setError(null);
      } catch (err) {
        setError('Failed to fetch quizzes. Please try again later.');
        console.error('Error fetching quizzes:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchQuizzes();
  }, []);

  const handleDeleteQuiz = async (id, e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (window.confirm('Are you sure you want to delete this quiz?')) {
      try {
        await quizService.deleteQuiz(id);
        setQuizzes(quizzes.filter(quiz => quiz.id !== id));
      } catch (err) {
        setError('Failed to delete quiz. Please try again later.');
        console.error('Error deleting quiz:', err);
      }
    }
  };

  if (loading) {
    return <div className="flex justify-center items-center h-64"><p>Loading quizzes...</p></div>;
  }

  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        <p>{error}</p>
        <button 
          onClick={() => window.location.reload()} 
          className="mt-2 bg-red-500 hover:bg-red-700 text-white font-bold py-1 px-2 rounded"
        >
          Retry
        </button>
      </div>
    );
  }

  if (quizzes.length === 0) {
    return (
      <div className="p-4">
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold">Quizzes</h1>
          <Link to="/quizzes/new" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
            Create New Quiz
          </Link>
        </div>
        <p className="text-gray-500">No quizzes available. Create a new one to get started!</p>
      </div>
    );
  }

  return (
    <div className="p-4">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Quizzes</h1>
        <Link to="/quizzes/new" className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
          Create New Quiz
        </Link>
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
                <p className="text-gray-600">{quiz.description || 'No description provided'}</p>
                <div className="flex justify-between items-center mt-4">
                  <span className="text-sm text-gray-500">
                    {quiz.createdAt ? new Date(quiz.createdAt).toLocaleDateString() : 'Date unknown'}
                  </span>
                  <div className="flex space-x-2">
                    <Link 
                      to={`/quizzes/${quiz.id}/edit`} 
                      className="text-blue-500 hover:text-blue-700"
                      onClick={e => e.stopPropagation()}
                    >
                      Edit
                    </Link>
                    <button 
                      onClick={(e) => handleDeleteQuiz(quiz.id, e)} 
                      className="text-red-500 hover:text-red-700"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            </Link>
          </div>
        ))}
      </div>
    </div>
  );
};

export default QuizList; 