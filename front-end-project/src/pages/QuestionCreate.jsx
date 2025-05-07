import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link as RouterLink } from 'react-router-dom';
import QuestionForm from '../components/QuestionForm';
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
import ArrowBackIcon from '@mui/icons-material/ArrowBack';

const QuestionCreate = () => {
  const { quizId } = useParams();
  const navigate = useNavigate();
  
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const fetchQuiz = async () => {
      try {
        setLoading(true);
        const data = await quizService.getQuizById(quizId);
        setQuiz(data);
        setError(null);
      } catch (err) {
        setError('Failed to load quiz details. Please ensure the quiz exists.');
        console.error('Error fetching quiz for QuestionCreate:', err);
      } finally {
        setLoading(false);
      }
    };
    
    if (quizId) fetchQuiz();
  }, [quizId]);
  
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
          <Button 
            color="inherit" 
            size="small" 
            onClick={() => navigate(quizId ? `/quizzes/${quizId}` : '/quizzes')} 
            startIcon={<ArrowBackIcon />}
          >
            {quizId ? 'Back to Quiz' : 'Back to Quizzes'}
          </Button>
        }>
          {error}
        </Alert>
      </Container>
    );
  }
  
  if (!quiz) {
    return (
      <Container maxWidth="sm" sx={{ mt: 4 }}>
        <Alert severity="warning" action={
          <Button 
            color="inherit" 
            size="small" 
            component={RouterLink} 
            to="/quizzes"
            startIcon={<ArrowBackIcon />}
          >
            Back to Quizzes
          </Button>
        }>
          Quiz not found. Cannot add questions.
        </Alert>
      </Container>
    );
  }
  
  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center' }}>
          {`Add Question to "${quiz.title}"`}
        </Typography>
        <QuestionForm 
          quizId={quizId}
          buttonLabel="Add Question"
          cancelRoute={`/quizzes/${quizId}`}
        />
      </Paper>
    </Container>
  );
};

export default QuestionCreate; 