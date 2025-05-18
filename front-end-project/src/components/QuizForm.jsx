import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  TextField,
  Button,
  Alert,
  CircularProgress,
  Checkbox,
  FormControlLabel,
  Box,
  Stack,
  Grid,
  Select,
  MenuItem,
  FormControl,
  InputLabel
} from '@mui/material';
import quizService from '../services/quizService';
import categoryService from '../services/categoryService';

const QuizForm = ({
  quiz = null,
  onSubmit = null,
  buttonLabel = 'Submit',
  cancelRoute = '/quizzes'
}) => {
  const [formData, setFormData] = useState({
    title: '',
    description: '',
    courseCode: '',
    published: false,
    categoryId: ''
  });

  const [categories, setCategories] = useState([]);
  const [categoriesLoading, setCategoriesLoading] = useState(true);
  const [categoriesError, setCategoriesError] = useState(null);

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const navigate = useNavigate();

  useEffect(() => {
    const fetchCategories = async () => {
      try {
        setCategoriesLoading(true);
        const data = await categoryService.getAllCategories();
        setCategories(Array.isArray(data) ? data : []);
        setCategoriesError(null);
      } catch (err) {
        setCategoriesError(err.message || 'Failed to load categories.');
        setCategories([]);
      } finally {
        setCategoriesLoading(false);
      }
    };
    fetchCategories();
  }, []);

  useEffect(() => {
    if (quiz) {
      setFormData({
        title: quiz.title || '',
        description: quiz.description || '',
        courseCode: quiz.courseCode || '',
        published: quiz.published || false,
        categoryId: quiz.category?.id || quiz.categoryId || ''
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
      const payload = { ...formData };
      if (payload.categoryId === '') {
          payload.categoryId = null;
      } else {
          payload.categoryId = Number(payload.categoryId);
      }

      if (onSubmit) {
        result = await onSubmit(payload);
      } else {
        if (quiz && quiz.id) {
          result = await quizService.updateQuiz(quiz.id, payload);
        } else {
          result = await quizService.createQuiz(payload);
        }
      }

      if (result && result.id) {
        navigate(cancelRoute || (quiz && quiz.id ? `/quizzes/${quiz.id}` : '/quizzes'));
      }
    } catch (err) {
      setError(err.response?.data?.message || err.message || 'Failed to save quiz. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
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
          <FormControl fullWidth error={!!validationErrors.categoryId} disabled={categoriesLoading || !!categoriesError}>
            <InputLabel id="category-select-label">Category</InputLabel>
            <Select
              labelId="category-select-label"
              id="category-select"
              name="categoryId"
              value={formData.categoryId || ''}
              onChange={handleChange}
              label="Category"
            >
              <MenuItem value="">
                <em>None</em>
              </MenuItem>
              {categoriesLoading && <MenuItem value="" disabled><em>Loading categories...</em></MenuItem>}
              {categoriesError && <MenuItem value="" disabled><em>Error: {categoriesError}</em></MenuItem>}
              {!categoriesLoading && !categoriesError && categories.length === 0 && (
                <MenuItem value="" disabled><em>No categories available.</em></MenuItem>
              )}
              {!categoriesLoading && !categoriesError && categories.map(category => (
                <MenuItem key={category.id} value={category.id}>
                  {category.name}
                </MenuItem>
              ))}
            </Select>
            {validationErrors.categoryId && <Typography color="error" variant="caption">{validationErrors.categoryId}</Typography>}
          </FormControl>
        </Grid>
        <Grid item xs={12}>
          <FormControlLabel
            control={<Checkbox
                        name="published"
                        checked={formData.published}
                        onChange={handleChange}
                        disabled={loading}
                      />}
            label="Published"
            sx={{ justifyContent: 'flex-start', width: '100%', mt:0 }}
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