import React from 'react';
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
  Box,
  Link as MuiLink // Alias Link to avoid conflict with RouterLink if used in same file
} from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import { Link as RouterLink } from 'react-router-dom'; // For navigation

/**
 * Component that displays only published quizzes.
 * Follows Single Responsibility Principle by focusing only on rendering published quizzes.
 * Uses the useQuizzes hook to separate data fetching from UI rendering.
 */
const PublishedQuizList = () => {
  // Use custom hook with onlyPublished=true
  const { quizzes, loading, error, refetch } = useQuizzes({ onlyPublished: true });

  const handleRefresh = () => {
    refetch();
  };

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', py: 5 }}>
        <CircularProgress />
        <Typography variant="h6" sx={{ ml: 2 }}>Loading published quizzes...</Typography>
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

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Typography variant="h4" component="h1">
          Published Quizzes
        </Typography>
        <Button 
          variant="outlined"
          onClick={handleRefresh} 
          startIcon={<RefreshIcon />}
        >
          Refresh
        </Button>
      </Box>

      {quizzes.length === 0 ? (
        <Box sx={{ textAlign: 'center', py: 5 }}>
          <Typography variant="h6">
            No published quizzes available.
          </Typography>
        </Box>
      ) : (
        <Grid container spacing={3}>
          {quizzes.map(quiz => (
            <Grid item xs={12} sm={6} md={4} key={quiz.id}>
              <Card sx={{ height: '100%', transition: 'box-shadow 0.3s', '&:hover': { boxShadow: 6 } }}>
                <CardContent>
                  <Typography variant="h5" component="h2" gutterBottom>
                    <MuiLink component={RouterLink} to={`/quizzes/${quiz.id}`} underline="hover">
                        {quiz.title}
                    </MuiLink>
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {quiz.description || 'No description provided'}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Container>
  );
};

export default PublishedQuizList; 