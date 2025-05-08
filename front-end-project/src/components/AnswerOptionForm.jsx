import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import quizService from '../services/quizService';

// Debug: Check if the functions exist in quizService
console.log('quizService functions available:', {
  getQuestionAnswers: typeof quizService.getQuestionAnswers === 'function',
  createAnswerOption: typeof quizService.createAnswerOption === 'function',
  deleteAnswerOption: typeof quizService.deleteAnswerOption === 'function',
  answerOptions: quizService.answerOptions,
  fullQuizService: quizService
});

const AnswerOptionForm = ({
  questionId,
  answerOption = null,
  onSubmit = null,
  title = 'Manage Answer Options',
  buttonLabel = 'Add Answer',
  cancelRoute = null
}) => {
  // Default form state
  const [formData, setFormData] = useState({
    answerText: '',
    correct: false,
    questionId: questionId
  });
  
  const [answerOptions, setAnswerOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});
  const [listLoading, setListLoading] = useState(true);
  
  const navigate = useNavigate();
  
  const defaultCancelRoute = `/questions/${questionId}`;
  
  // Load existing answer options
  useEffect(() => {
    const fetchAnswerOptions = async () => {
      try {
        setListLoading(true);
        let data;
        
        // Try direct function first
        try {
          console.log("Attempting to use quizService.getQuestionAnswers");
          data = await quizService.getQuestionAnswers(questionId);
        } catch (primaryError) {
          console.error("Primary function failed:", primaryError);
          
          // Fallback to nested function
          console.log("Attempting to use quizService.answerOptions.getByQuestionId");
          data = await quizService.answerOptions.getByQuestionId(questionId);
        }
        
        console.log("Answer options data retrieved:", data);
        setAnswerOptions(data);
      } catch (err) {
        console.error('Error fetching answer options:', err);
        setError('Failed to load existing answer options.');
      } finally {
        setListLoading(false);
      }
    };
    
    fetchAnswerOptions();
  }, [questionId]);
  
  // Prefill form if editing an answer option
  useEffect(() => {
    if (answerOption) {
      setFormData({
        answerText: answerOption.text || '',
        correct: answerOption.correct || false,
        questionId
      });
    }
  }, [answerOption, questionId]);
  
  const validate = () => {
    const errors = {};
    
    if (!formData.answerText.trim()) {
      errors.answerText = 'Answer answerText is required';
    } else if (formData.answerText.length < 1) {
      errors.answerText = 'Answer answerText must not be empty';
    } else if (formData.answerText.length > 255) {
      errors.answerText = 'Answer text must be less than 255 characters';
    }
    
    // Check if we're already at the maximum of 4 answers
    if (answerOptions.length >= 4 && !answerOption) {
      errors.general = 'Maximum 4 answer options allowed per question';
    }
    
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    
    // Clear validation error when user types
    if (validationErrors[name]) {
      setValidationErrors(prev => ({
        ...prev,
        [name]: null
      }));
    }
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    // Validate form
    if (!validate()) {
      return;
    }
    
    setLoading(true);
    setError(null);
    
    try {
      // Create new answer option
      let result;
      
      // Try direct function first
      console.log("Attempting to use quizService.createAnswerOption");
      
      result = await quizService.createAnswerOption(questionId, formData);
      
      console.log("Answer option created:", result);
      
      // Add to list
      setAnswerOptions([...answerOptions, result]);
      
      // Reset form
      setFormData({
        answerText: '',
        correct: false,
        questionId
      });
      
      return result;
    } catch (err) {
      setError('Failed to save answer option. Please try again.');
      console.error('Error saving answer option:', err);
    } finally {
      setLoading(false);
    }
  };
  
  const handleDeleteAnswerOption = async (id) => {
    if (window.confirm('Are you sure you want to delete this answer option?')) {
      try {
        // Try direct function first
        try {
          console.log("Attempting to use quizService.deleteAnswerOption");
          await quizService.deleteAnswerOption(id);
        } catch (primaryError) {
          console.error("Primary function failed:", primaryError);
          
          // Fallback to nested function
          console.log("Attempting to use quizService.answerOptions.delete");
          await quizService.answerOptions.delete(id);
        }
        
        console.log("Answer option deleted:", id);
        setAnswerOptions(answerOptions.filter(option => option.id !== id));
      } catch (err) {
        setError('Failed to delete answer option. Please try again.');
        console.error('Error deleting answer option:', err);
      }
    }
  };
  
  return (
    <div className="max-w-2xl mx-auto p-4">
      <h1 className="text-2xl font-bold mb-6">{title}</h1>
      
      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          <p>{error}</p>
        </div>
      )}
      
      {validationErrors.general && (
        <div className="bg-yellow-100 border border-yellow-400 text-yellow-700 px-4 py-3 rounded mb-4">
          <p>{validationErrors.general}</p>
        </div>
      )}
      
      <div className="mb-8">
        <h2 className="text-lg font-semibold mb-4">Current Answer Options</h2>
        
        {listLoading ? (
          <p>Loading answer options...</p>
        ) : answerOptions.length === 0 ? (
          <p className="text-gray-500 italic">No answer options yet. Add one below.</p>
        ) : (
          <div className="space-y-3">
            {answerOptions.map(option => (
              <div 
                key={option.id} 
                className={`border rounded-lg p-3 flex justify-between items-center ${
                  option.correct ? 'bg-green-50 border-green-200' : 'bg-white'
                }`}
              >
                <div className="flex items-center">
                  <span className={`inline-block w-4 h-4 rounded-full mr-3 ${
                    option.correct ? 'bg-green-500' : 'bg-gray-300'
                  }`}></span>
                  <span>{option.text}</span>
                </div>
                <div className="flex space-x-2">
                  <button
                    onClick={() => handleDeleteAnswerOption(option.id)}
                    className="text-red-500 hover:text-red-700"
                  >
                    Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
      
      <div className="border-t pt-6">
        <h2 className="text-lg font-semibold mb-4">Add New Answer Option</h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="text" className="block text-sm font-medium text-gray-700 mb-1">
              Answer Text <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              id="text"
              name="text"
              value={formData.text}
              onChange={handleChange}
              className={`w-full p-2 border rounded ${validationErrors.text ? 'border-red-500' : 'border-gray-300'}`}
              disabled={loading || answerOptions.length >= 4}
              required
            />
            {validationErrors.text && (
              <p className="mt-1 text-sm text-red-500">{validationErrors.text}</p>
            )}
          </div>
          
          <div className="flex items-center">
            <input
              type="checkbox"
              id="correct"
              name="correct"
              checked={formData.correct}
              onChange={handleChange}
              className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
              disabled={loading || answerOptions.length >= 4}
            />
            <label htmlFor="correct" className="ml-2 block text-sm text-gray-700">
              This is the correct answer
            </label>
          </div>
          
          <div className="flex justify-between pt-4">
            <button
              type="button"
              onClick={() => navigate(cancelRoute || defaultCancelRoute)}
              className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded"
            >
              Back
            </button>
            <button
              type="submit"
              className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              disabled={loading || answerOptions.length >= 4}
            >
              {loading ? 'Saving...' : buttonLabel}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AnswerOptionForm; 