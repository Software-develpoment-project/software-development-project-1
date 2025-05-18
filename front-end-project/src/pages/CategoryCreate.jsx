import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Paper,
  Typography,
  Alert,
  Box
} from '@mui/material';
import CategoryForm from '../components/CategoryForm';
import categoryService from '../services/categoryService';

const CategoryCreate = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (categoryData) => {
    setLoading(true);
    setError(null);
    try {
      await categoryService.createCategory(categoryData);
      navigate('/categories'); // Navigate to category list on success
    } catch (err) {
      console.error('Error creating category:', err);
      setError(err.message || 'Failed to create category. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/categories');
  };

  return (
    <Container maxWidth="sm" sx={{ py: 4 }}>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" component="h1" gutterBottom sx={{ textAlign: 'center' }}>
          Add a Category
        </Typography>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
            {error}
          </Alert>
        )}
        <CategoryForm 
          onSubmit={handleSubmit} 
          onCancel={handleCancel}
          loading={loading}
          buttonLabel="Create Category"
        />
      </Paper>
    </Container>
  );
};

export default CategoryCreate; 