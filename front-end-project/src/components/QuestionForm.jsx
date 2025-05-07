import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';
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
    quizId: quizId || question?.quizId,
    difficultyLevel: question?.difficultyLevel || 'MEDIUM'
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
      setFormData({ 
        content: '', 
        quizId: formData.quizId, 
        difficultyLevel: 'MEDIUM' 
      });
      navigate(cancelRoute || defaultCancelRoute);
      
      return result;
    } catch (err) {
      setError('Failed to save question. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
  return (
    <Container className="py-4">
      <h1 className="mb-4">{title}</h1>
      
      {error && (
        <Alert variant="danger" className="mb-4">
          {error}
        </Alert>
      )}
      
      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3" controlId="formQuestionText">
          <Form.Label>
            Question Text <span className="text-danger">*</span>
          </Form.Label>
          <Form.Control
            as="textarea"
            rows={4}
            name="content"
            value={formData.content}
            onChange={handleChange}
            isInvalid={!!validationErrors.content}
            disabled={loading}
            required
          />
          <Form.Control.Feedback type="invalid">
            {validationErrors.content}
          </Form.Control.Feedback>
        </Form.Group>
        
        <Form.Group className="mb-4" controlId="formDifficultyLevel">
          <Form.Label>Difficulty</Form.Label>
          <div>
            <Form.Check
              inline
              type="radio"
              id="difficulty-easy"
              label="Easy"
              name="difficultyLevel"
              value="EASY"
              checked={formData.difficultyLevel === 'EASY'}
              onChange={handleChange}
              className="me-3"
            />
            <Form.Check
              inline
              type="radio"
              id="difficulty-normal"
              label="Normal"
              name="difficultyLevel"
              value="MEDIUM"
              checked={formData.difficultyLevel === 'MEDIUM'}
              onChange={handleChange}
              className="me-3"
            />
            <Form.Check
              inline
              type="radio"
              id="difficulty-hard"
              label="Hard"
              name="difficultyLevel"
              value="HARD"
              checked={formData.difficultyLevel === 'HARD'}
              onChange={handleChange}
            />
          </div>
        </Form.Group>
        
        <div className="d-flex justify-content-end gap-2">
          <Button
            variant="secondary"
            onClick={() => navigate(cancelRoute || defaultCancelRoute)}
            disabled={loading}
          >
            Cancel
          </Button>
          <Button
            variant="primary"
            type="submit"
            disabled={loading}
          >
            {loading ? (
              <>
                <Spinner
                  as="span"
                  animation="border"
                  size="sm"
                  role="status"
                  aria-hidden="true"
                  className="me-1"
                />
                Saving...
              </>
            ) : (
              buttonLabel
            )}
          </Button>
        </div>
      </Form>
    </Container>
  );
};

export default QuestionForm; 