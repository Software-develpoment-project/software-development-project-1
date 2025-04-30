import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Table, Button, Badge, Form, Container, Alert, Card, Spinner } from 'react-bootstrap';
import quizService from '../services/quizService';

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
    text: '',
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
        const data = await quizService.answerOptions.getByQuestionId(questionId);
        setAnswerOptions(data);
      } catch (err) {
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
        text: answerOption.text || '',
        correct: answerOption.correct || false,
        questionId
      });
    }
  }, [answerOption, questionId]);
  
  const validate = () => {
    const errors = {};
    
    if (!formData.text.trim()) {
      errors.text = 'Answer text is required';
    } else if (formData.text.length < 1) {
      errors.text = 'Answer text must not be empty';
    } else if (formData.text.length > 255) {
      errors.text = 'Answer text must be less than 255 characters';
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
      let result;
      
      // Create the payload with correct field name for backend DTO
      const payload = {
        answerText: formData.text, // Map 'text' to 'answerText' for backend compatibility
        correct: formData.correct,
        questionId: formData.questionId
      };
      
      result = await quizService.answerOptions.create(questionId, payload);
      
      // Add to list
      setAnswerOptions([...answerOptions, result]);
      
      // Reset form
      setFormData({
        text: '',
        correct: false,
        questionId
      });
      
      return result;
    } catch (err) {
      setError('Failed to save answer option. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
  const handleDeleteAnswerOption = async (id, e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (window.confirm('Are you sure you want to delete this answer option?')) {
      try {
        await quizService.answerOptions.delete(id);
        setAnswerOptions(answerOptions.filter(option => option.id !== id));
      } catch (err) {
        setError('Failed to delete answer option. Please try again.');
      }
    }
  };
  
  return (
    <Container className="py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>{title}</h1>
        <Button
          variant="secondary"
          onClick={() => navigate(cancelRoute || defaultCancelRoute)}
        >
          Back to Questions
        </Button>
      </div>
      
      {error && (
        <Alert variant="danger" className="mb-4">
          {error}
        </Alert>
      )}
      
      {validationErrors.general && (
        <Alert variant="warning" className="mb-4">
          {validationErrors.general}
        </Alert>
      )}
      
      <Card className="mb-4">
        <Card.Header>
          <h2 className="mb-0">Current Answer Options</h2>
        </Card.Header>
        <Card.Body>
          {listLoading ? (
            <div className="text-center py-3">
              <Spinner animation="border" role="status">
                <span className="visually-hidden">Loading answer options...</span>
              </Spinner>
            </div>
          ) : answerOptions.length === 0 ? (
            <p className="text-muted fst-italic">No answer options yet. Add one below.</p>
          ) : (
            <Table responsive striped hover>
              <thead>
                <tr>
                  <th>Answer option text</th>
                  <th>Correctness</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {answerOptions.map(option => (
                  <tr key={option.id}>
                    <td>{option.text || option.answerText}</td>
                    <td>
                      <Badge 
                        bg={option.correct ? 'success' : 'danger'} 
                        pill
                      >
                        {option.correct ? 'Correct' : 'Not correct'}
                      </Badge>
                    </td>
                    <td>
                      <Button
                        variant="danger"
                        size="sm"
                        onClick={(e) => handleDeleteAnswerOption(option.id, e)}
                      >
                        Delete
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          )}
        </Card.Body>
      </Card>
      
      <Card>
        <Card.Header>
          <h2 className="mb-0">Add New Answer Option</h2>
        </Card.Header>
        <Card.Body>
          <Form onSubmit={handleSubmit}>
            <Form.Group className="mb-3" controlId="formAnswerText">
              <Form.Label>
                Answer Text <span className="text-danger">*</span>
              </Form.Label>
              <Form.Control
                type="text"
                name="text"
                value={formData.text}
                onChange={handleChange}
                isInvalid={!!validationErrors.text}
                disabled={loading || answerOptions.length >= 4}
                required
              />
              <Form.Control.Feedback type="invalid">
                {validationErrors.text}
              </Form.Control.Feedback>
            </Form.Group>
            
            <Form.Group className="mb-3" controlId="formCorrect">
              <Form.Check
                type="checkbox"
                label="This is the correct answer"
                name="correct"
                checked={formData.correct}
                onChange={handleChange}
                disabled={loading || answerOptions.length >= 4}
              />
            </Form.Group>
            
            <div className="d-flex justify-content-end gap-2">
              <Button
                variant="primary"
                type="submit"
                disabled={loading || answerOptions.length >= 4}
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

export default AnswerOptionForm; 