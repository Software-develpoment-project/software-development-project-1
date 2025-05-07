import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { Table, Button, Badge, Container, Spinner, Alert } from 'react-bootstrap';
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
      } finally {
        setLoading(false);
      }
    };
    
    fetchQuizData();
  }, [id]);
  
  const handleDeleteQuestion = async (questionId, e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (window.confirm('Are you sure you want to delete this question?')) {
      try {
        await quizService.questions.delete(questionId);
        setQuestions(questions.filter(q => q.id !== questionId));
      } catch (err) {
        setError('Failed to delete question. Please try again.');
      }
    }
  };
  
  const getDifficultyBadgeVariant = (difficulty) => {
    switch (difficulty?.toUpperCase()) {
      case 'EASY':
        return 'success';
      case 'NORMAL':
      case 'MEDIUM':
        return 'warning';
      case 'HARD':
        return 'danger';
      default:
        return 'secondary';
    }
  };
  
  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center" style={{ height: '200px' }}>
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading quiz data...</span>
        </Spinner>
      </Container>
    );
  }
  
  if (error) {
    return (
      <Container className="mt-4">
        <Alert variant="danger">
          <p>{error}</p>
          <Button 
            variant="primary" 
            onClick={() => navigate('/quizzes')} 
            className="mt-2"
          >
            Back to Quizzes
          </Button>
        </Alert>
      </Container>
    );
  }
  
  if (!quiz) {
    return (
      <Container className="mt-4">
        <p>Quiz not found</p>
        <Button 
          as={Link} 
          to="/quizzes" 
          variant="secondary"
        >
          Back to Quizzes
        </Button>
      </Container>
    );
  }
  
  return (
    <Container className="py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>{quiz.title}</h1>
        <div className="d-flex gap-2">
          <Button 
            as={Link} 
            to="/quizzes" 
            variant="secondary"
          >
            Back
          </Button>
          <Button 
            as={Link} 
            to={`/quizzes/${id}/edit`} 
            variant="primary"
          >
            Edit Quiz
          </Button>
        </div>
      </div>
      
      {quiz.description && (
        <Alert variant="light" className="mb-4">
          <p className="mb-0">{quiz.description}</p>
        </Alert>
      )}
      
      <div className="mb-4">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h2>Questions</h2>
          <Button 
            as={Link} 
            to={`/quizzes/${id}/questions/new`} 
            variant="success"
          >
            Add Question
          </Button>
        </div>
        
        {questions.length === 0 ? (
          <p className="text-muted fst-italic">No questions yet. Add one to get started!</p>
        ) : (
          <Table responsive striped hover>
            <thead>
              <tr>
                <th>#</th>
                <th>Question text</th>
                <th>Difficulty</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {questions.map((question, index) => (
                <tr key={question.id}>
                  <td>{index + 1}</td>
                  <td>
                    <Link to={`/questions/${question.id}/answers`}>
                      {question.questionText}
                    </Link>
                  </td>
                  <td>
                    <Badge 
                      bg={getDifficultyBadgeVariant(question.difficultyLevel)} 
                      pill
                    >
                      {question.difficultyLevel || 'Unknown'}
                    </Badge>
                  </td>
                  <td>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={(e) => handleDeleteQuestion(question.id, e)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        )}
      </div>
    </Container>
  );
};

export default QuizDetail; 