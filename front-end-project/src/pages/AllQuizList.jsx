import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
import useQuizzes from '../hooks/useQuizzes';
import {
  Container,
  Typography,
  Button,
  CircularProgress,
  Alert,
  Grid,
  Card,
  CardContent,
  CardActions,
  Chip,
  Box,
  Link
} from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import AddIcon from '@mui/icons-material/Add';

/**
 * Component that displays all quizzes (both published and unpublished).
 * Uses the useQuizzes hook to separate data fetching from UI rendering.
 */
const AllQuizList = () => {
  // Use custom hook with onlyPublished=false to get all quizzes
  const { quizzes, loading, error, refetch } = useQuizzes({ onlyPublished: false });

  const handleRefresh = () => {
    refetch();
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', py: 5 }}>
        <CircularProgress />
        <Typography variant="h6" sx={{ ml: 2 }}>Loading quizzes...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Container maxWidth="sm" sx={{ py: 5 }}>
        <Alert severity="error" action={
          <Button color="inherit" size="small" onClick={handleRefresh} startIcon={<RefreshIcon />}>
            Try Again
          </Button>
        }>
          {error}
        </Alert>
      </Container>
    );
  }

  if (quizzes.length === 0) {
    return (
      <Box sx={{ textAlign: 'center', py: 5 }}>
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
      </Box>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Typography variant="h4" component="h1">
          All Quizzes
        </Typography>
        <Box>
          <Button 
            variant="outlined"
            onClick={handleRefresh} 
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

      <Grid container spacing={3}>
        {quizzes.map(quiz => (
          <Grid item xs={12} sm={6} md={4} key={quiz.id}>
            <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', transition: 'box-shadow 0.3s', '&:hover': { boxShadow: 6 } }}>
              <CardContent sx={{ flexGrow: 1 }}>
                <Typography variant="h5" component="h2" gutterBottom>
                  <Link component={RouterLink} to={`/quizzes/${quiz.id}`} underline="hover">
                    {quiz.title}
                  </Link>
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2, minHeight: '60px' }}>
                  {quiz.description || 'No description provided'}
                </Typography>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <Chip 
                    label={quiz.published ? 'Published' : 'Draft'} 
                    color={quiz.published ? 'success' : 'default'} 
                    size="small" 
                  />
                  <Typography variant="caption" color="text.secondary">
                    {quiz.createdAt ? new Date(quiz.createdAt).toLocaleDateString() : ''}
                  </Typography>
                </Box>
              </CardContent>
              <CardActions sx={{ justifyContent: 'flex-end'}}>
                  <Button component={RouterLink} to={`/quizzes/${quiz.id}`} size="small">View Details</Button>
              </CardActions>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Container>
  );
};

export default AllQuizList; 