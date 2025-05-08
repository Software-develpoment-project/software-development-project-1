import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import quizService from '../services/quizService';

const QuestionForm = ({
  quizId,
  question = null,
  onSubmit = null,
  title = 'Add Question',
  buttonLabel = 'Save Question',
  cancelRoute = null
}) => {
  // Default form state
  const [formData, setFormData] = useState({
    content: question?.content || '',
    quizId: quizId || question?.quizId
  });
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});
  
  const navigate = useNavigate();
  
  const defaultCancelRoute = `/quizzes/${quizId}`;
  
  const validate = () => {
    const errors = {};
    
    if (!formData.content.trim()) {
      errors.content = 'Question text is required';
    } else if (formData.content.length < 5) {
      errors.content = 'Question text must be at least 5 characters';
    } else if (formData.content.length > 500) {
      errors.content = 'Question text must be less than 500 characters';
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
      let result;
      
      if (onSubmit) {
        // Use provided onSubmit handler
        result = await onSubmit(formData);
      } else {
        // Use default behavior
        if (question && question.id) {
          // Edit existing question (not implemented in API yet)
          throw new Error('Editing questions is not supported yet');
        } else {
          // Create new question
          result = await quizService.questions.create(formData.quizId, formData);
        }
      }
      
      // Reset form and navigate back
      setFormData({ content: '', quizId: formData.quizId });
      navigate(cancelRoute || defaultCancelRoute);
      
      return result;
    } catch (err) {
      setError('Failed to save question. Please try again.');
      console.error('Error saving question:', err);
    } finally {
      setLoading(false);
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
          <label htmlFor="content" className="block text-sm font-medium text-gray-700 mb-1">
            Question Text <span className="text-red-500">*</span>
          </label>
          <textarea
            id="content"
            name="content"
            value={formData.content}
            onChange={handleChange}
            rows="4"
            className={`w-full p-2 border rounded ${validationErrors.content ? 'border-red-500' : 'border-gray-300'}`}
            disabled={loading}
            required
          />
          {validationErrors.content && (
            <p className="mt-1 text-sm text-red-500">{validationErrors.content}</p>
          )}
        </div>
        
        <div className="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            onClick={() => navigate(cancelRoute || defaultCancelRoute)}
            className="bg-gray-300 hover:bg-gray-400 text-gray-800 font-bold py-2 px-4 rounded"
            disabled={loading}
          >
            Cancel
          </button>
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

export default QuestionForm; 