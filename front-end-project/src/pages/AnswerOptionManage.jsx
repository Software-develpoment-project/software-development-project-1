import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import AnswerOptionForm from '../components/AnswerOptionForm';
import quizService from '../services/quizService';

const AnswerOptionManage = () => {
  const { questionId } = useParams();
  const navigate = useNavigate();
  
  const [question, setQuestion] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const fetchData = async () => {
      if (!questionId) {
        setError('No question ID provided');
        setLoading(false);
        return;
      }
      
      try {
        setLoading(true);
        
        // Fetch the question data which includes its quizId
        const questionData = await quizService.questions.getById(questionId);
        
        if (!questionData) {
          throw new Error(`Question with ID ${questionId} not found`);
        }
        
        if (!questionData.quizId) {
          throw new Error('Question data is missing quizId reference');
        }
        
        setQuestion(questionData);
        setError(null);
      } catch (err) {
        setError('Failed to load question data. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    
    fetchData();
  }, [questionId]);
  
  if (loading) {
    return <div className="flex justify-center items-center h-64"><p>Loading question data...</p></div>;
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
  
  if (!question) {
    return (
      <div className="p-4">
        <p>Question not found</p>
        <button 
          onClick={() => navigate('/quizzes')} 
          className="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded"
        >
          Back to Quizzes
        </button>
      </div>
    );
  }
  
  const questionText = question.content || question.questionText || 'Unknown Question';
  
  return (
    <AnswerOptionForm 
      questionId={questionId}
      title={`Manage Answers for Question: "${questionText}"`}
      buttonLabel="Add Answer Option"
      cancelRoute={`/quizzes/${question.quizId}`}
    />
  );
};

export default AnswerOptionManage; 