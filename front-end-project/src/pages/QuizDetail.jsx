import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import quizService from '../services/quizService';

const QuizDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [quiz, setQuiz] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const fetchQuizData = async () => {
      try {
        setLoading(true);
        
        // Fetch quiz details
        const quizData = await quizService.getQuizById(id);
        setQuiz(quizData);
        
        // Fetch questions for this quiz
        const questionsData = await quizService.questions.getByQuizId(id);
        setQuestions(questionsData);
        
        setError(null);
      } catch (err) {
        setError('Failed to load quiz data. Please try again later.');
        console.error('Error fetching quiz data:', err);
      } finally {
        setLoading(false);
      }
    };
    
    fetchQuizData();
  }, [id]);
  
  const handleDeleteQuestion = async (questionId) => {
    if (window.confirm('Are you sure you want to delete this question?')) {
      try {
        await quizService.questions.delete(questionId);
        setQuestions(questions.filter(q => q.id !== questionId));
      } catch (err) {
        setError('Failed to delete question. Please try again.');
        console.error('Error deleting question:', err);
      }
    }
  };
  
  if (loading) {
    return <div className="flex justify-center items-center h-64"><p>Loading quiz data...</p></div>;
  }
  
  if (error) {
    return (
      <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
        <p>{error}</p>
        <button 
          onClick={() => navigate('/quizzes')} 
          className="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded"
        >
          Back to Quizzes
        </button>
      </div>
    );
  }
  
  if (!quiz) {
    return (
      <div className="p-4">
        <p>Quiz not found</p>
        <Link to="/quizzes" className="text-blue-500 hover:text-blue-700">
          Back to Quizzes
        </Link>
      </div>
    );
  }
  
  return (
    <div className="max-w-4xl mx-auto p-4">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">{quiz.title}</h1>
        <div className="flex space-x-2">
          <Link 
            to="/quizzes" 
            className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded"
          >
            Back
          </Link>
          <Link 
            to={`/quizzes/${id}/edit`} 
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
          >
            Edit Quiz
          </Link>
        </div>
      </div>
      
      {quiz.description && (
        <div className="bg-gray-100 p-4 rounded-lg mb-6">
          <p className="text-gray-700">{quiz.description}</p>
        </div>
      )}
      
      <div className="mb-8">
        <div className="flex justify-between items-center mb-4">
          <h2 className="text-xl font-semibold">Questions</h2>
          <Link 
            to={`/quizzes/${id}/questions/new`} 
            className="bg-green-500 hover:bg-green-700 text-white font-bold py-2 px-4 rounded"
          >
            Add Question
          </Link>
        </div>
        
        {questions.length === 0 ? (
          <p className="text-gray-500 italic">No questions yet. Add one to get started!</p>
        ) : (
          <div className="space-y-4">
            {questions.map((question, index) => (
              <div key={question.id} className="border rounded-lg p-4 shadow-sm">
                <div className="flex justify-between items-start">
                  <div>
                    <span className="font-medium text-gray-500 mr-2">Q{index + 1}.</span>
                    <span className="font-medium">{question.questionText}</span>
                  </div>
                  <div className="flex space-x-2">
                    <Link 
                      to={`/questions/${question.id}/answers`}
                      className="text-blue-500 hover:text-blue-700"
                    >
                      Manage Answers
                    </Link>
                    <button
                      onClick={() => handleDeleteQuestion(question.id)}
                      className="text-red-500 hover:text-red-700"
                    >
                      Delete
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default QuizDetail; 