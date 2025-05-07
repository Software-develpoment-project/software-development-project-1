import React, { useState, useEffect } from 'react';
import { useParams, Link as RouterLink, useNavigate } from 'react-router-dom';
import {
  Container,
  Button,
  Typography,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  CircularProgress,
  Alert,
  IconButton,
  Chip,
  Box,
  Grid,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import quizService from '../services/quizService';

const QuizDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  
  const [quiz, setQuiz] = useState(null);
  const [questions, setQuestions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [confirmDeleteDialogOpen, setConfirmDeleteDialogOpen] = useState(false);
  const [questionToDelete, setQuestionToDelete] = useState(null);

  useEffect(() => {
    const fetchQuizData = async () => {
      try {
        setLoading(true);
        const quizData = await quizService.getQuizById(id);
        setQuiz(quizData);
        const questionsData = await quizService.questions.getByQuizId(id);
        setQuestions(Array.isArray(questionsData) ? questionsData : []);
        setError(null);
      } catch (err) {
        setError('Failed to load quiz data. Please try again later.');
        console.error('Error fetching quiz data:', err);
      } finally {
        setLoading(false);
      }
    };
    
    fetchQuizData();
  }, [id]);

  const handleDeleteClick = (question) => {
    setQuestionToDelete(question);
    setConfirmDeleteDialogOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (questionToDelete) {
      try {
        await quizService.questions.delete(questionToDelete.id);
        setQuestions(prevQuestions => prevQuestions.filter(q => q.id !== questionToDelete.id));
        setError(null);
      } catch (err) {
        setError('Failed to delete question. Please try again.');
        console.error('Error deleting question:', err);
      }
    }
    setConfirmDeleteDialogOpen(false);
    setQuestionToDelete(null);
  };

  const handleCloseConfirmDialog = () => {
    setConfirmDeleteDialogOpen(false);
    setQuestionToDelete(null);
  };
  
  const getDifficultyChipColor = (difficulty) => {
    switch (difficulty?.toUpperCase()) {
      case 'EASY':
        return 'success';
      case 'MEDIUM':
        return 'warning';
      case 'HARD':
        return 'error';
      default:
        return 'default';
    }
  };
  
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
      <Container maxWidth="md" sx={{ mt: 4 }}>
        <Alert severity="error" action={
          <Button color="inherit" size="small" onClick={() => navigate('/quizzes')} startIcon={<ArrowBackIcon />}>
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
      <Container maxWidth="md" sx={{ mt: 4 }}>
        <Alert severity="warning" action={
          <Button color="inherit" size="small" component={RouterLink} to="/quizzes" startIcon={<ArrowBackIcon />}>
            Back to Quizzes
          </Button>
        }>
          Quiz not found.
        </Alert>
      </Container>
    );
  }
  
  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Paper elevation={3} sx={{ p: 3, mb: 4}}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h4" component="h1" gutterBottom>
            {quiz.title}
          </Typography>
          <Box>
            <Button 
              component={RouterLink} 
              to="/quizzes" 
              variant="outlined" 
              startIcon={<ArrowBackIcon />} 
              sx={{ mr: 1 }}
            >
              Back to List
            </Button>
            <Button 
              component={RouterLink} 
              to={`/quizzes/${id}/edit`} 
              variant="contained" 
              startIcon={<EditIcon />}
            >
              Edit Quiz
            </Button>
          </Box>
        </Box>
        {quiz.description && (
          <Typography variant="subtitle1" color="text.secondary" paragraph>
            {quiz.description}
          </Typography>
        )}
         <Chip 
            label={quiz.published ? 'Published' : 'Draft'} 
            color={quiz.published ? 'success' : 'default'} 
            size="small" 
            sx={{mb:1}}
          />
        <Typography variant="caption" display="block" color="text.secondary">
            Course Code: {quiz.courseCode || 'N/A'}
        </Typography>
        <Typography variant="caption" display="block" color="text.secondary">
            Created: {quiz.createdAt ? new Date(quiz.createdAt).toLocaleString() : 'N/A'}
        </Typography>
      </Paper>
      
      <Paper elevation={3} sx={{ p: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="h5" component="h2">
            Questions
          </Typography>
          <Button 
            component={RouterLink} 
            to={`/quizzes/${id}/questions/new`} 
            variant="contained" 
            color="secondary"
            startIcon={<AddIcon />}
          >
            Add Question
          </Button>
        </Box>
        
        {questions.length === 0 ? (
          <Typography color="text.secondary" sx={{ fontStyle: 'italic'}}>
            No questions yet. Add one to get started!
          </Typography>
        ) : (
          <TableContainer>
            <Table sx={{ minWidth: 650 }} aria-label="questions table">
              <TableHead sx={{ backgroundColor: 'grey.100' }}>
                <TableRow>
                  <TableCell sx={{ width: '5%' }}>#</TableCell>
                  <TableCell>Question Text</TableCell>
                  <TableCell align="center">Difficulty</TableCell>
                  <TableCell align="right">Actions</TableCell>
                </TableRow>
              </TableHead>
              <TableBody>
                {questions.map((question, index) => (
                  <TableRow key={question.id} hover>
                    <TableCell>{index + 1}</TableCell>
                    <TableCell>
                      <RouterLink to={`/questions/${question.id}/answers`} style={{ textDecoration: 'none', color: 'inherit' }}>
                        {question.questionText || question.content}
                      </RouterLink>
                    </TableCell>
                    <TableCell align="center">
                      <Chip 
                        label={question.difficultyLevel || 'Unknown'} 
                        color={getDifficultyChipColor(question.difficultyLevel)} 
                        size="small" 
                      />
                    </TableCell>
                    <TableCell align="right">
                      <IconButton 
                        color="error" 
                        size="small"
                        onClick={() => handleDeleteClick(question)}
                      >
                        <DeleteIcon />
                      </IconButton>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        )}
      </Paper>

      <Dialog
        open={confirmDeleteDialogOpen}
        onClose={handleCloseConfirmDialog}
      >
        <DialogTitle>Confirm Delete Question</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete the question: "{questionToDelete?.questionText || questionToDelete?.content}"? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseConfirmDialog}>Cancel</Button>
          <Button onClick={handleConfirmDelete} color="error" autoFocus>
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default QuizDetail; 