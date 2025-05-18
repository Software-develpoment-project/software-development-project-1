import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import QuizForm from '../components/QuizForm';
import quizService from '../services/quizService';
import {
  Container,
  Typography,
  Paper,
  CircularProgress,
  Alert,
  Button,
  Box
} from '@mui/material';

const QuizEdit = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const fetchQuiz = async () => {
      try {
        setLoading(true);
        const data = await quizService.getQuizById(id);
        setQuiz(data);
        setError(null);
      } catch (err) {
        setError('Failed to load quiz. Please try again later.');
        console.error('Error fetching quiz:', err);
      } finally {
        setLoading(false);
      }
    };
    
    fetchQuiz();
  }, [id]);
  
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
        <CircularProgress />
        <Typography variant="h6" sx={{ ml: 2 }}>Loading quiz data...</Typography>
      </Box>
    );
  }
  
  if (error) {
    return (
      <Container maxWidth="sm" sx={{ mt: 4 }}>
        <Alert severity="error" action={
          <Button color="inherit" size="small" onClick={() => navigate('/quizzes')}>
            Back to Quizzes
          </Button>
        }>
          {error}
        </Alert>
      </Container>
    );
  }
  
  if (!quiz) {
    return (
<<<<<<< HEAD
      <div className="p-4">
        <p>Quiz not found</p>
        <button 
          onClick={() => navigate('/quizzes')} 
          className="mt-2 bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-2 rounded">
          Back to Quizzes
        </button>
      </div>
=======
      <Container maxWidth="sm" sx={{ mt: 4 }}>
        <Alert severity="warning" action={
          <Button color="inherit" size="small" onClick={() => navigate('/quizzes')}>
            Back to Quizzes
          </Button>
        }>
          Quiz not found.
        </Alert>
      </Container>
>>>>>>> develop
    );
  }
  
  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center' }}>
          {`Edit Quiz: ${quiz.title}`}
        </Typography>
        <QuizForm 
          quiz={quiz}
          buttonLabel="Update Quiz"
        />
      </Paper>
    </Container>
  );
};

export default QuizEdit; 