import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Form, Button, Container, Alert, Spinner, Card } from 'react-bootstrap';
import quizService from '../services/quizService';

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
    description: '',
    courseCode: '',
    published: false
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
        description: quiz.description || '',
        courseCode: quiz.courseCode || '',
        published: quiz.published || false
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
    
    if (formData.courseCode && formData.courseCode.length > 50) {
      errors.courseCode = 'Course code must be less than 50 characters';
    }
    
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const fieldValue = type === 'checkbox' ? checked : value;
    
    setFormData(prev => ({
      ...prev,
      [name]: fieldValue
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
      setFormData({ 
        title: '', 
        description: '', 
        courseCode: '', 
        published: false 
      });
      
      // Navigate back to quiz list
      navigate('/quizzes');
      
      return result;
    } catch (err) {
      setError('Failed to save quiz. Please try again.');
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
      
      <Card>
        <Card.Body>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formTitle">
              <Form.Label>
                Title <span className="text-danger">*</span>
              </Form.Label>
              <Form.Control
                type="text"
                name="title"
                value={formData.title}
                onChange={handleChange}
                isInvalid={!!validationErrors.title}
                disabled={loading}
                required
              />
              <Form.Control.Feedback type="invalid">
                {validationErrors.title}
              </Form.Control.Feedback>
            </Form.Group>
            
            <Form.Group className="mb-3" controlId="formDescription">
              <Form.Label>Description</Form.Label>
              <Form.Control
                as="textarea"
                rows={4}
                name="description"
                value={formData.description}
                onChange={handleChange}
                isInvalid={!!validationErrors.description}
                disabled={loading}
              />
              <Form.Control.Feedback type="invalid">
                {validationErrors.description}
              </Form.Control.Feedback>
            </Form.Group>
            
            <Form.Group className="mb-3" controlId="formCourseCode">
              <Form.Label>Course Code</Form.Label>
              <Form.Control
                type="text"
                name="courseCode"
                value={formData.courseCode}
                onChange={handleChange}
                isInvalid={!!validationErrors.courseCode}
                disabled={loading}
              />
              <Form.Control.Feedback type="invalid">
                {validationErrors.courseCode}
              </Form.Control.Feedback>
            </Form.Group>
            
            <Form.Group className="mb-4" controlId="formPublished">
              <Form.Check
                type="checkbox"
                label="Published"
                name="published"
                checked={formData.published}
                onChange={handleChange}
                disabled={loading}
              />
            </Form.Group>
            
            <div className="d-flex gap-2">
              <Button
                variant="secondary"
                onClick={() => navigate(cancelRoute)}
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
        </Card.Body>
      </Card>
    </Container>
  );
};

export default QuizForm; 