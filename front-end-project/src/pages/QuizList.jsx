import React, { useState, useEffect } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
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
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle
} from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import RefreshIcon from '@mui/icons-material/Refresh';
import quizService from '../services/quizService';

const QuizList = () => {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [confirmDeleteDialogOpen, setConfirmDeleteDialogOpen] = useState(false);
  const [quizToDelete, setQuizToDelete] = useState(null);
  const navigate = useNavigate();

  const fetchQuizzes = async () => {
    try {
      setLoading(true);
      const data = await quizService.getAllQuizzes();
      setQuizzes(Array.isArray(data) ? data : []);
      setError(null);
    } catch (err) {
      setError('Failed to fetch quizzes. Please try again later.');
      console.error('Error fetching quizzes:', err);
      setQuizzes([]); // Ensure quizzes is an array in case of error
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchQuizzes();
  }, []);

  const handleDeleteClick = (quiz) => {
    setQuizToDelete(quiz);
    setConfirmDeleteDialogOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (quizToDelete) {
      try {
        await quizService.deleteQuiz(quizToDelete.id);
        setQuizzes(prevQuizzes => prevQuizzes.filter(quiz => quiz.id !== quizToDelete.id));
        setError(null);
      } catch (err) {
        setError('Failed to delete quiz. Please try again later.');
        console.error('Error deleting quiz:', err);
      }
    }
    setConfirmDeleteDialogOpen(false);
    setQuizToDelete(null);
  };

  const handleCloseConfirmDialog = () => {
    setConfirmDeleteDialogOpen(false);
    setQuizToDelete(null);
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', py: 5 }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Loading quizzes...</Typography>
      </Box>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Typography variant="h4" component="h1">
          My Quizzes
        </Typography>
        <Box>
          <Button 
            variant="outlined" 
            onClick={fetchQuizzes} 
            startIcon={<RefreshIcon />} 
            sx={{ mr: 2 }}
          >
            Refresh
          </Button>
          <Button 
            variant="contained"
            component={RouterLink} 
            to="/quizzes/new"
            startIcon={<AddIcon />}
          >
            Create New Quiz
          </Button>
        </Box>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
          {error}
        </Alert>
      )}

      {quizzes.length === 0 && !loading ? (
        <Paper sx={{ p: 3, textAlign: 'center' }}>
          <Typography variant="h6" gutterBottom>
            No quizzes available.
          </Typography>
          <Button 
            variant="contained" 
            component={RouterLink} 
            to="/quizzes/new"
            startIcon={<AddIcon />}
          >
            Create Your First Quiz
          </Button>
        </Paper>
      ) : (
        <TableContainer component={Paper} elevation={3}>
          <Table sx={{ minWidth: 650 }} aria-label="quizzes table">
            <TableHead sx={{ backgroundColor: 'grey.200' }}>
              <TableRow>
                <TableCell>Title</TableCell>
                <TableCell>Description</TableCell>
                <TableCell>Course Code</TableCell>
                <TableCell align="center">Published</TableCell>
                <TableCell>Date Added</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {quizzes.map(quiz => (
                <TableRow 
                  key={quiz.id} 
                  hover 
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                  onClick={() => navigate(`/quizzes/${quiz.id}`)} // Navigate on row click
                  style={{ cursor: 'pointer' }}
                >
                  <TableCell component="th" scope="row">
                    {quiz.title}
                  </TableCell>
                  <TableCell>{quiz.description || '-'}</TableCell>
                  <TableCell>{quiz.courseCode || 'N/A'}</TableCell>
                  <TableCell align="center">
                    <Chip 
                      label={quiz.published ? 'Published' : 'Draft'} 
                      color={quiz.published ? 'success' : 'default'} 
                      size="small" 
                    />
                  </TableCell>
                  <TableCell>
                    {quiz.createdAt ? new Date(quiz.createdAt).toLocaleDateString() : '-'}
                  </TableCell>
                  <TableCell align="right">
                    <IconButton 
                      component={RouterLink} 
                      to={`/quizzes/${quiz.id}/edit`} 
                      color="primary" 
                      size="small"
                      onClick={(e) => e.stopPropagation()} // Prevent row click
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton 
                      color="error" 
                      size="small"
                      onClick={(e) => { 
                        e.stopPropagation(); // Prevent row click
                        handleDeleteClick(quiz); 
                      }}
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

      <Dialog
        open={confirmDeleteDialogOpen}
        onClose={handleCloseConfirmDialog}
        aria-labelledby="alert-dialog-title"
        aria-describedby="alert-dialog-description"
      >
        <DialogTitle id="alert-dialog-title">
          {"Confirm Delete"}
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="alert-dialog-description">
            Are you sure you want to delete the quiz "{quizToDelete?.title}"? This action cannot be undone.
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

export default QuizList; 