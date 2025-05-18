import React from 'react';
import QuizForm from '../components/QuizForm';
import { Container, Typography, Paper } from '@mui/material';

const QuizCreate = () => {
  return (
    <Container maxWidth="md">
      <Paper elevation={3} sx={{ p: 4, mt: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center' }}>
          Create New Quiz
        </Typography>
        <QuizForm 
          buttonLabel="Create Quiz" 
        />
      </Paper>
    </Container>
  );
};

export default QuizCreate; 