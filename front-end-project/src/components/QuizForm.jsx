import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  TextField,
  Button,
  Container,
  Alert,
  CircularProgress,
  Paper,
  Typography,
  Checkbox,
  FormControlLabel,
  Box,
  Stack,
  Grid
} from '@mui/material';
import quizService from '../services/quizService';

const QuizForm = ({ 
  quiz = null, 
  onSubmit = null,
  // title prop is now typically handled by the parent page component
  buttonLabel = 'Submit',
  cancelRoute = '/quizzes'
}) => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    courseCode: '',
    published: false
  });
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});
  
  const navigate = useNavigate();
  
  useEffect(() => {
    if (quiz) {
      setFormData({
        title: quiz.title || '',
        description: quiz.description || '',
        courseCode: quiz.courseCode || '',
        published: quiz.published || false
      });
    }
  }, [quiz]);
  
  const validate = () => {
    const errors = {};
    if (!formData.title.trim()) errors.title = 'Title is required';
    else if (formData.title.length < 3) errors.title = 'Title must be at least 3 characters';
    else if (formData.title.length > 100) errors.title = 'Title must be less than 100 characters';
    
    if (formData.description && formData.description.length > 500) {
      errors.description = 'Description must be less than 500 characters';
    }
    if (formData.courseCode && formData.courseCode.length > 50) {
      errors.courseCode = 'Course code must be less than 50 characters';
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };
  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    const fieldValue = type === 'checkbox' ? checked : value;
    setFormData(prev => ({ ...prev, [name]: fieldValue }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({ ...prev, [name]: null }));
    }
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;
    
    setLoading(true);
    setError(null);
    
    try {
      let result;
      const payload = { ...formData }; // Ensure we send the current form data

      if (onSubmit) {
        result = await onSubmit(payload);
      } else {
        if (quiz && quiz.id) {
          result = await quizService.updateQuiz(quiz.id, payload);
        } else {
          result = await quizService.createQuiz(payload);
        }
      }
      // Navigate after successful submission
      // Parent component might handle navigation if onSubmit is provided and returns a specific signal
      if (!onSubmit || (result && result.id)) { // Basic check, adjust if onSubmit has different success criteria
         navigate(quiz && quiz.id ? `/quizzes/${quiz.id}` : '/quizzes');
      }
    } catch (err) {
      console.error('Quiz form submission error:', err);
      setError(err.message || 'Failed to save quiz. Please try again.');
    } finally {
      setLoading(false);
    }
  };
  
  return (
    <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}
      <Grid container spacing={2}>
        <Grid item xs={12}>
          <TextField
            name="title"
            label="Title"
            value={formData.title}
            onChange={handleChange}
            error={!!validationErrors.title}
            helperText={validationErrors.title}
            disabled={loading}
            required
            fullWidth
            autoFocus
          />
        </Grid>
        <Grid item xs={12}>
          <TextField
            name="description"
            label="Description"
            multiline
            rows={4}
            value={formData.description}
            onChange={handleChange}
            error={!!validationErrors.description}
            helperText={validationErrors.description}
            disabled={loading}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <TextField
            name="courseCode"
            label="Course Code"
            value={formData.courseCode}
            onChange={handleChange}
            error={!!validationErrors.courseCode}
            helperText={validationErrors.courseCode}
            disabled={loading}
            fullWidth
          />
        </Grid>
        <Grid item xs={12} sm={6}>
          <FormControlLabel
            control={<Checkbox 
                        name="published" 
                        checked={formData.published} 
                        onChange={handleChange} 
                        disabled={loading} 
                      />}
            label="Published"
            sx={{ justifyContent: 'flex-start', width: '100%', mt:1 }} // Adjust alignment if needed
          />
        </Grid>
      </Grid>
      <Stack direction="row" spacing={2} sx={{ mt: 3, justifyContent: 'flex-end' }}>
        <Button
          variant="outlined"
          onClick={() => navigate(cancelRoute)}
          disabled={loading}
        >
          Cancel
        </Button>
        <Button
          variant="contained"
          type="submit"
          disabled={loading}
          startIcon={loading ? <CircularProgress size={20} /> : null}
        >
          {loading ? 'Saving...' : buttonLabel}
        </Button>
      </Stack>
    </Box>
  );
};

export default QuizForm; 