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
  Box,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Stack
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import AddIcon from '@mui/icons-material/Add';
import ArrowBackIcon from '@mui/icons-material/ArrowBack'; 
import RefreshIcon from '@mui/icons-material/Refresh';
import categoryService from '../services/categoryService';

const CategoryList = () => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [confirmDeleteDialogOpen, setConfirmDeleteDialogOpen] = useState(false);
  const [categoryToDelete, setCategoryToDelete] = useState(null);
  const navigate = useNavigate();

  const fetchCategories = async () => {
    try {
      setLoading(true);
      const data = await categoryService.getAllCategories();
      setCategories(Array.isArray(data) ? data : []);
      setError(null);
    } catch (err) {
      setError(err.message || 'Failed to fetch categories. Please try again later.');
      console.error('Error fetching categories:', err);
      setCategories([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCategories();
  }, []);

  const handleDeleteClick = (category) => {
    setCategoryToDelete(category);
    setConfirmDeleteDialogOpen(true);
  };

  const handleConfirmDelete = async () => {
    if (categoryToDelete) {
      try {
        await categoryService.deleteCategory(categoryToDelete.id);
        setCategories(prevCategories => prevCategories.filter(cat => cat.id !== categoryToDelete.id));
        setError(null);
      } catch (err) {
        setError(err.message || 'Failed to delete category. Please try again later.');
        console.error('Error deleting category:', err);
      }
    }
    setConfirmDeleteDialogOpen(false);
    setCategoryToDelete(null);
  };

  const handleCloseConfirmDialog = () => {
    setConfirmDeleteDialogOpen(false);
    setCategoryToDelete(null);
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', py: 5 }}>
        <CircularProgress />
        <Typography sx={{ ml: 2 }}>Loading categories...</Typography>
      </Box>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
        <Typography variant="h4" component="h1">
          Categories
        </Typography>
        <Stack direction="row" spacing={2}>
          <Button 
            variant="outlined" 
            onClick={fetchCategories} 
            startIcon={<RefreshIcon />} 
          >
            Refresh
          </Button>
          <Button 
            variant="contained"
            component={RouterLink} 
            to="/categories/new"
            startIcon={<AddIcon />}
          >
            Add New Category
          </Button>
        </Stack>
      </Box>

      <Button 
          component={RouterLink} 
          to="/quizzes" 
          startIcon={<ArrowBackIcon />} 
          sx={{ mb: 2 }}
        >
          Back to Quizzes
      </Button>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }} onClose={() => setError(null)}>
          {error}
        </Alert>
      )}

      {categories.length === 0 && !loading && !error ? (
        <Paper sx={{ p: 3, textAlign: 'center' }}>
          <Typography variant="h6" gutterBottom>
            No categories found.
          </Typography>
          <Button 
            variant="contained" 
            component={RouterLink} 
            to="/categories/new"
            startIcon={<AddIcon />}
          >
            Create Your First Category
          </Button>
        </Paper>
      ) : categories.length > 0 && (
        <TableContainer component={Paper} elevation={3}>
          <Table sx={{ minWidth: 650 }} aria-label="categories table">
            <TableHead sx={{ backgroundColor: 'grey.200' }}>
              <TableRow>
                <TableCell>Name</TableCell>
                <TableCell>Description</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {categories.map(category => (
                <TableRow 
                  key={category.id} 
                  hover 
                  sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                >
                  <TableCell component="th" scope="row">
                    {category.name}
                  </TableCell>
                  <TableCell>{category.description || '-'}</TableCell>
                  <TableCell align="right">

                    <IconButton 
                      color="error" 
                      size="small"
                      onClick={(e) => { 
                        e.stopPropagation(); 
                        handleDeleteClick(category); 
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
            Are you sure you want to delete the category "{categoryToDelete?.name}"? This action cannot be undone.
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

export default CategoryList; 