import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Button,
  Checkbox,
  TextField,
  Container,
  Alert,
  CircularProgress,
  Paper,
  Typography,
  FormControlLabel,
  Box,
  Stack,
  Grid,
  IconButton,
  Chip,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import quizService from '../services/quizService';

const AnswerOptionForm = ({
  questionId,
  // answerOption prop for editing - potentially future feature
  // title prop is handled by parent page
  buttonLabel = 'Add Answer Option',
  cancelRoute = null // Parent should provide this
}) => {
  const [formData, setFormData] = useState({
    answerText: '', // Renamed from content
    correct: false,  // Renamed from isCorrect
    questionId: questionId
  });
  
  const [answerOptions, setAnswerOptions] = useState([]);
  const [loading, setLoading] = useState(false); // Loading state for form submission
  const [listLoading, setListLoading] = useState(true); // Loading state for the list
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});
  const [confirmDeleteDialogOpen, setConfirmDeleteDialogOpen] = useState(false);
  const [optionToDelete, setOptionToDelete] = useState(null);
  
  const navigate = useNavigate();
  
  const defaultCancelRoute = questionId ? `/quizzes/${questionId}` : '/quizzes'; // Fallback needed

  // Fetch existing options
  const fetchAnswerOptions = async () => {
    if (!questionId) {
      setError('Cannot load answers: Question ID is missing.');
      setListLoading(false);
      return;
    }
    try {
      setListLoading(true);
      const data = await quizService.answerOptions.getByQuestionId(questionId);
      setAnswerOptions(Array.isArray(data) ? data : []);
      setError(null);
    } catch (err) {
      console.error('Failed to load answer options:', err);
      setError('Failed to load existing answer options.');
      setAnswerOptions([]);
    } finally {
      setListLoading(false);
    }
  };
  
  useEffect(() => {
    fetchAnswerOptions();
  }, [questionId]);
  
  const validate = () => {
    const errors = {};
    if (!formData.answerText.trim()) errors.answerText = 'Answer text is required'; // Use answerText
    else if (formData.answerText.length > 500) errors.answerText = 'Answer text must be less than 500 characters'; // Use answerText
    
    if (answerOptions.length >= 4) {
       errors.general = 'Maximum 4 answer options allowed per question.';
       setError('Maximum 4 answer options allowed per question.'); // Show error prominently
    }
    
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({ ...prev, [name]: type === 'checkbox' ? checked : value }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({ ...prev, [name]: null }));
    }
    if (error && name === 'answerText') setError(null); // Clear general error on text input
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(null); // Clear previous errors
    if (!validate()) return;
    
    setLoading(true);
    
    const payload = { 
      answerText: formData.answerText, // Correct field name
      correct: formData.correct,    // Correct field name
      questionId: formData.questionId // Include questionId explicitly
    };
    
    try {
      const newOption = await quizService.answerOptions.create(questionId, payload);
      setAnswerOptions(prevOptions => [...prevOptions, newOption]); // Assuming the create call returns the new object with ID
      setFormData({ answerText: '', correct: false, questionId }); // Reset form with correct field names
      setValidationErrors({});
    } catch (err) {
      console.error('Failed to save answer option:', err);
      setError(err.message || 'Failed to save answer option. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteClick = (option) => {
    setOptionToDelete(option);
    setConfirmDeleteDialogOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (optionToDelete) {
      try {
        await quizService.answerOptions.delete(optionToDelete.id);
        setAnswerOptions(prevOptions => prevOptions.filter(opt => opt.id !== optionToDelete.id));
        setError(null);
      } catch (err) {
         console.error('Failed to delete answer option:', err);
         setError(err.message || 'Failed to delete answer option. Please try again.');
      }
    }
    setConfirmDeleteDialogOpen(false);
    setOptionToDelete(null);
  };

  const handleCloseConfirmDialog = () => {
    setConfirmDeleteDialogOpen(false);
    setOptionToDelete(null);
  };
  
  return (
    <Box sx={{ mt: 2 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
            <Typography variant="h5" component="h2">
                Current Answer Options
            </Typography>
            <Button
              variant="outlined"
              startIcon={<ArrowBackIcon />}
              onClick={() => navigate(cancelRoute || defaultCancelRoute)}
            >
              Back to Quiz Detail
            </Button>
        </Box>

      {listLoading ? (
        <Box sx={{ display: 'flex', justifyContent: 'center', py: 3 }}>
          <CircularProgress />
        </Box>
      ) : answerOptions.length === 0 && !error ? (
        <Typography color="text.secondary" sx={{ fontStyle: 'italic', mb: 2 }}>
            No answer options yet. Add one using the form below.
        </Typography>
      ) : (
        <TableContainer component={Paper} elevation={1} sx={{ mb: 4 }}>
          <Table size="small">
            <TableHead sx={{ backgroundColor: 'grey.100' }}>
              <TableRow>
                <TableCell>Answer Text</TableCell>
                <TableCell align="center">Is Correct?</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {answerOptions.map(option => (
                <TableRow key={option.id} hover>
                  <TableCell sx={{ wordBreak: 'break-word' }}>{option.answerText}</TableCell>
                  <TableCell align="center">
                    <Chip 
                      label={option.correct ? 'Correct' : 'Incorrect'} 
                      color={option.correct ? 'success' : 'error'} 
                      size="small" 
                      variant="outlined"
                    />
                  </TableCell>
                  <TableCell align="right">
                    <IconButton 
                        size="small"
                        onClick={() => handleDeleteClick(option)}
                        aria-label="delete answer option"
                        color="error"
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
      
      {/* Add New Answer Form */}  
      <Paper component="form" onSubmit={handleSubmit} elevation={1} sx={{ p: 2 }}>
          <Typography variant="h6" component="h3" gutterBottom>
              Add New Answer Option
          </Typography>
          {error && (
            <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
              {error}
            </Alert>
          )}
          {validationErrors.general && (
            <Alert severity="warning" sx={{ mb: 2 }} onClose={() => setValidationErrors(prev => ({ ...prev, general: null}))}> 
              {validationErrors.general}
            </Alert>
          )}
          <Grid container spacing={2} alignItems="center">
            <Grid item xs={12} sm={8}>
                <TextField
                    name="answerText"
                    label="Answer Text"
                    value={formData.answerText}
                    onChange={handleChange}
                    error={!!validationErrors.answerText}
                    helperText={validationErrors.answerText}
                    disabled={loading || answerOptions.length >= 4}
                    required
                    fullWidth
                    size="small"
                />
            </Grid>
            <Grid item xs={12} sm={2}>
                <FormControlLabel
                    control={<Checkbox 
                                name="correct"
                                checked={formData.correct}
                                onChange={handleChange} 
                                disabled={loading || answerOptions.length >= 4}
                             />}
                    label="Correct"
                    sx={{ justifyContent: 'flex-start'}}
                />
            </Grid>
            <Grid item xs={12} sm={2}>
              <Button
                variant="contained"
                type="submit"
                disabled={loading || answerOptions.length >= 4}
                startIcon={loading ? <CircularProgress size={20} /> : <AddIcon />}
                fullWidth
              >
                {loading ? 'Adding...' : buttonLabel}
              </Button>
            </Grid>
          </Grid>
        </Paper>
        
      {/* Confirmation Dialog for Delete */}    
      <Dialog
        open={confirmDeleteDialogOpen}
        onClose={handleCloseConfirmDialog}
      >
        <DialogTitle>Confirm Delete Answer Option</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete the answer: "{optionToDelete?.answerText}"? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseConfirmDialog}>Cancel</Button>
          <Button onClick={handleConfirmDelete} color="error" autoFocus>
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AnswerOptionForm; 