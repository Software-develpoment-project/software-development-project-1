import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link as RouterLink } from 'react-router-dom';
import AnswerOptionForm from '../components/AnswerOptionForm';
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

const AnswerOptionManage = () => {
  const { questionId } = useParams();
  const navigate = useNavigate();
  
  const [question, setQuestion] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  useEffect(() => {
    const fetchData = async () => {
      if (!questionId) {
        setError('No question ID provided.');
        setLoading(false);
        return;
      }
      
      try {
        setLoading(true);
        const questionData = await quizService.questions.getById(questionId);
        
        if (!questionData) {
          throw new Error(`Question with ID ${questionId} not found.`);
        }
        
        if (!questionData.quizId) {
          throw new Error('Question data is missing the associated Quiz ID.');
        }
        
        setQuestion(questionData);
        setError(null);
      } catch (err) {
        console.error('Error fetching question data for AnswerOptionManage:', err);
        setError(err.message || 'Failed to load question data. Please try again later.');
      } finally {
        setLoading(false);
      }
    };
    
    fetchData();
  }, [questionId]);
  
  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
        <CircularProgress />
        <Typography variant="h6" sx={{ ml: 2 }}>Loading question data...</Typography>
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
            onClick={() => navigate(question?.quizId ? `/quizzes/${question.quizId}` : '/quizzes')} 
            startIcon={<ArrowBackIcon />}
          >
            {question?.quizId ? 'Back to Quiz' : 'Back to Quizzes'}
          </Button>
        }>
          {error}
        </Alert>
      </Container>
    );
  }
  
  if (!question) {
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
          Question data not available.
        </Alert>
      </Container>
    );
  }
  
  const questionText = question.content || question.questionText || 'Selected Question';
  
  return (
    <Container maxWidth="lg">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center', wordBreak: 'break-word' }}>
          {`Manage Answer Options for: "${questionText}"`}
        </Typography>
        <AnswerOptionForm 
          questionId={questionId}
          // title prop is handled by Typography above
          buttonLabel="Add Answer Option"
          cancelRoute={`/quizzes/${question.quizId}`}
        />
      </Paper>
    </Container>
  );
};

export default AnswerOptionManage; 