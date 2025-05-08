import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import quizService from '../services/quizService';
import { testDirectQuizCreation } from '../services/debug';

const QuizForm = ({ 
  quiz = null, 
  onSubmit = null,
  title = 'Quiz Form',
  buttonLabel = 'Submit',
  cancelRoute = '/quizzes'
}) => {
  // Default empty form state
  const [formData, setFormData] = useState({
    title: '',
    description: ''
  });
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});
  
  const navigate = useNavigate();
  
  // If quiz is provided, prefill the form (used in edit mode)
  useEffect(() => {
    if (quiz) {
      setFormData({
        title: quiz.title || '',
        description: quiz.description || ''
      });
    }
  }, [quiz]);
  
  const validate = () => {
    const errors = {};
    
    if (!formData.title.trim()) {
      errors.title = 'Title is required';
    } else if (formData.title.length < 3) {
      errors.title = 'Title must be at least 3 characters';
    } else if (formData.title.length > 100) {
      errors.title = 'Title must be less than 100 characters';
    }
    
    if (formData.description && formData.description.length > 500) {
      errors.description = 'Description must be less than 500 characters';
    }
    
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
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
      // Log the form data right before submission
      console.log('QuizForm - Data being submitted:', JSON.stringify(formData));
      
      let result;
      
      if (onSubmit) {
        // If an onSubmit handler is provided, use it
        result = await onSubmit(formData);
      } else {
        // Otherwise use default submit behavior
        if (quiz) {
          // Update mode
          result = await quizService.updateQuiz(quiz.id, formData);
        } else {
          // Create mode
          result = await quizService.createQuiz(formData);
        }
      }
      
      // Reset form
      setFormData({ title: '', description: '' });
      
      // Navigate back to quiz list
      navigate('/quizzes');
      
      return result;
    } catch (err) {
      setError('Failed to save quiz. Please try again.');
      console.error('Error saving quiz:', err);
    } finally {
      setLoading(false);
    }
  };
  
  // Debug function to test direct API call
  const handleTestDirectCall = async () => {
    try {
      await testDirectQuizCreation();
      alert('Direct API call succeeded! Check console for details.');
    } catch (err) {
      alert('Direct API call failed! Check console for details.');
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
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-1">
            Title <span className="text-red-500">*</span>
          </label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className={`w-full p-2 border rounded ${validationErrors.title ? 'border-red-500' : 'border-gray-300'}`}
            disabled={loading}
            required
          />
          {validationErrors.title && (
            <p className="mt-1 text-sm text-red-500">{validationErrors.title}</p>
          )}
        </div>
        
        <div>
          <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
            Description
          </label>
          <textarea
            id="description"
            name="description"
            value={formData.description}
            onChange={handleChange}
            rows="4"
            className={`w-full p-2 border rounded ${validationErrors.description ? 'border-red-500' : 'border-gray-300'}`}
            disabled={loading}
          />
          {validationErrors.description && (
            <p className="mt-1 text-sm text-red-500">{validationErrors.description}</p>
          )}
        </div>
        
        <div className="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            onClick={() => navigate(cancelRoute)}
            className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded"
            disabled={loading}
          >
            Cancel
          </button>
          {process.env.NODE_ENV === 'development' && (
            <button
              type="button"
              onClick={handleTestDirectCall}
              className="bg-purple-500 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded"
              disabled={loading}
            >
              Test Direct API
            </button>
          )}
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            disabled={loading}
          >
            {loading ? 'Saving...' : buttonLabel}
          </button>
        </div>
      </form>
    </div>
  );
};

export default QuizForm; 