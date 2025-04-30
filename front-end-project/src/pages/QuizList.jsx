import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Table, Button, Badge, Container, Spinner } from 'react-bootstrap';
import quizService from '../services/quizService';

const QuizList = () => {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuizzes = async () => {
      try {
        setLoading(true);
        const data = await quizService.getAllQuizzes();
        setQuizzes(data);
        setError(null);
      } catch (err) {
        setError('Failed to fetch quizzes. Please try again later.');
        console.error('Error fetching quizzes:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchQuizzes();
  }, []);

  const handleDeleteQuiz = async (id, e) => {
    e.preventDefault();
    e.stopPropagation();
    
    if (window.confirm('Are you sure you want to delete this quiz?')) {
      try {
        await quizService.deleteQuiz(id);
        setQuizzes(quizzes.filter(quiz => quiz.id !== id));
      } catch (err) {
        setError('Failed to delete quiz. Please try again later.');
        console.error('Error deleting quiz:', err);
      }
    }
  };

  if (loading) {
    return (
      <Container className="d-flex justify-content-center align-items-center" style={{ height: '200px' }}>
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Loading quizzes...</span>
        </Spinner>
      </Container>
    );
  }

  if (error) {
    return (
      <Container className="mt-4">
        <div className="alert alert-danger" role="alert">
          <p>{error}</p>
          <Button 
            variant="outline-danger" 
            onClick={() => window.location.reload()} 
            className="mt-2"
          >
            Retry
          </Button>
        </div>
      </Container>
    );
  }

  return (
    <Container className="mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Quizzes</h1>
        <Button as={Link} to="/quizzes/new" variant="primary">
          Create New Quiz
        </Button>
      </div>
      
      {quizzes.length === 0 ? (
        <p className="text-muted">No quizzes available. Create a new one to get started!</p>
      ) : (
        <Table responsive striped hover>
          <thead>
            <tr>
              <th>Name</th>
              <th>Description</th>
              <th>Course Code</th>
              <th>Published</th>
              <th>Added on</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {quizzes.map(quiz => (
              <tr key={quiz.id}>
                <td>
                  <Link to={`/quizzes/${quiz.id}`}>
                    {quiz.title}
                  </Link>
                </td>
                <td>{quiz.description || 'No description provided'}</td>
                <td>{quiz.courseCode || 'N/A'}</td>
                <td>
                  <Badge bg={quiz.published ? 'success' : 'secondary'} pill>
                    {quiz.published ? 'Published' : 'Not published'}
                  </Badge>
                </td>
                <td>
                  {quiz.createdAt ? new Date(quiz.createdAt).toLocaleDateString() : 'Date unknown'}
                </td>
                <td>
                  <Button 
                    as={Link} 
                    to={`/quizzes/${quiz.id}/edit`} 
                    variant="warning" 
                    size="sm" 
                    className="me-2"
                  >
                    Edit
                  </Button>
                  <Button 
                    variant="danger" 
                    size="sm"
                    onClick={(e) => handleDeleteQuiz(quiz.id, e)}
                  >
                    Delete
                  </Button>
                </td>
              </tr>
            ))}
          </tbody>
        </Table>
      )}
    </Container>
  );
};

export default QuizList; 