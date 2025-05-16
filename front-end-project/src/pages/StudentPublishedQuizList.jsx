import React from 'react';
import { Link as RouterLink } from 'react-router-dom';
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
  Box,
  Link as MuiLink,
  Chip,
  Paper
} from '@mui/material';
import RefreshIcon from '@mui/icons-material/Refresh';
import RateReviewIcon from '@mui/icons-material/RateReview';
import SchoolIcon from '@mui/icons-material/School';
import { useQuizzes } from '../hooks/useQuizzes';

const StudentPublishedQuizList = () => {
  const { quizzes, loading, error, refetch } = useQuizzes({ onlyPublished: true });

  if (loading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '70vh' }}>
        <CircularProgress size={50} />
        <Typography variant="h6" sx={{ ml: 2 }}>Loading available quizzes...</Typography>
      </Box>
    );
  }

  if (error) {
    return (
      <Container maxWidth="md" sx={{ py: 5 }}>
        <Paper elevation={3} sx={{ p: 3, textAlign: 'center' }}>
            <Alert severity="error" icon={<SchoolIcon fontSize="inherit" />} sx={{ mb: 2 }}>
            Error: {error}
            </Alert>
            <Button color="primary" variant="outlined" size="small" onClick={refetch} startIcon={<RefreshIcon />}>
                Try Again
            </Button>
        </Paper>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 2 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
        <Typography variant="h4" component="h1" sx={{ display: 'flex', alignItems: 'center' }}>
          <SchoolIcon sx={{ mr: 1, fontSize: '2.5rem' }} color="primary" /> Available Quizzes
        </Typography>
        <Button 
          variant="outlined"
          onClick={refetch} 
          startIcon={<RefreshIcon />}
        >
          Refresh List
        </Button>
      </Box>

      {quizzes.length === 0 ? (
        <Paper elevation={1} sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h6" color="text.secondary" gutterBottom>
            No published quizzes are available at the moment.
          </Typography>
          <Typography color="text.secondary">
            Please check back later!
          </Typography>
        </Paper>
      ) : (
        <Grid container spacing={3}>
          {quizzes.map(quiz => (
            <Grid xs={12} sm={6} md={4} key={quiz.id}> 
              <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', transition: 'box-shadow 0.3s', '&:hover': { boxShadow: 7, transform: 'translateY(-2px)' } }}>
                <CardContent sx={{ flexGrow: 1 }}>
                  <Typography gutterBottom variant="h5" component="div">
                    <MuiLink component={RouterLink} to={`/student/quizzes/${quiz.id}`} underline="hover" color="primary.main">
                      {quiz.title || 'Untitled Quiz'}
                    </MuiLink>
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 1, minHeight: '60px',  
                        display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                    {quiz.description || 'No description provided.'}
                  </Typography>
                  {quiz.category && (
                    <Chip label={quiz.category.name} size="small" variant="outlined" sx={{ mr: 0.5, mb: 0.5, bgcolor: 'action.hover' }}/>
                  )}
                  {quiz.courseCode && (
                    <Chip label={quiz.courseCode} size="small" variant="outlined" sx={{ bgcolor: 'action.hover' }}/>
                  )}
                </CardContent>
                <CardActions sx={{ justifyContent: 'space-between', p:2, borderTop: '1px solid', borderColor: 'divider' }}>
                  <Button 
                    component={RouterLink} 
                    to={`/student/quizzes/${quiz.id}`} 
                    variant="contained"
                    size="small"
                  >
                    View & Start Quiz
                  </Button>
                  <Button
                    component={RouterLink}
                    to={`/student/quizzes/${quiz.id}/reviews`} 
                    variant="outlined"
                    size="small"
                    startIcon={<RateReviewIcon />}
                  >
                    Reviews
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}
    </Container>
  );
};

export default StudentPublishedQuizList;