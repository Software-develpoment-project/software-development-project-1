import React, { useState, useEffect } from 'react';
import {
  TextField,
  Button,
  Box,
  Stack,
  Grid,
  CircularProgress,
  Alert
} from '@mui/material';

const CategoryForm = ({
  category = null,
  onSubmit,
  onCancel,
  buttonLabel = 'Add Category',
  loading = false,
  error = null
}) => {
  const [formData, setFormData] = useState({
    name: '',
    description: ''
  });
  const [validationErrors, setValidationErrors] = useState({});

  useEffect(() => {
    if (category) {
      setFormData({
        name: category.name || '',
        description: category.description || ''
      });
    }
  }, [category]);

  const validate = () => {
    const errors = {};
    if (!formData.name.trim()) errors.name = 'Name is required';
    else if (formData.name.length < 3) errors.name = 'Name must be at least 3 characters';
    else if (formData.name.length > 100) errors.name = 'Name must be less than 100 characters';

    if (formData.description && formData.description.length > 255) {
      errors.description = 'Description must be less than 255 characters';
    }
    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({ ...prev, [name]: null }));
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!validate()) return;
    onSubmit(formData);
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
            name="name"
            label="Category Name"
            value={formData.name}
            onChange={handleChange}
            error={!!validationErrors.name}
            helperText={validationErrors.name}
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
            rows={3}
            value={formData.description}
            onChange={handleChange}
            error={!!validationErrors.description}
            helperText={validationErrors.description}
            disabled={loading}
            fullWidth
          />
        </Grid>
      </Grid>
      <Stack direction="row" spacing={2} sx={{ mt: 3, justifyContent: 'flex-end' }}>
        {onCancel && (
          <Button
            variant="outlined"
            onClick={onCancel}
            disabled={loading}
          >
            Cancel
          </Button>
        )}
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

export default CategoryForm;