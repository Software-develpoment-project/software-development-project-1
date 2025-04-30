import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import QuestionForm from '../components/QuestionForm';
import quizService from '../services/quizService';

const QuestionCreate = () => {
  const { quizId } = useParams();
  const navigate = useNavigate();
  
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const fetchQuiz = async () => {
      try {
        setLoading(true);
        const data = await quizService.getQuizById(quizId);
        setQuiz(data);
        setError(null);
      } catch (err) {
        setError('Failed to load quiz. Please try again later.');
        console.error('Error fetching quiz:', err);
      } finally {
        setLoading(false);
      }
    };
    
    fetchQuiz();
  }, [quizId]);
  
  if (loading) {
    return <div className="flex justify-center items-center h-64"><p>Loading quiz data...</p></div>;
  }
  
  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        <p>{error}</p>
        <button 
          onClick={() => navigate(`/quizzes/${quizId}`)} 
          className="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded"
        >
          Back to Quiz
        </button>
      </div>
    );
  }
  
  if (!quiz) {
    return (
      <div className="p-4">
        <p>Quiz not found</p>
        <button 
          onClick={() => navigate('/quizzes')} 
          className="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded"
        >
          Back to Quizzes
        </button>
      </div>
    );
  }
  
  return (
    <QuestionForm 
      quizId={quizId}
      title={`Add Question to "${quiz.title}"`}
      buttonLabel="Add Question"
      cancelRoute={`/quizzes/${quizId}`}
    />
  );
};

export default QuestionCreate; 