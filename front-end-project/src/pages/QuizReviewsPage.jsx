import React, { useState, useEffect, useCallback } from 'react';
import { useParams, Link as RouterLink } from 'react-router-dom';
import {
  Container, Typography, CircularProgress, Alert, Button, Box, Paper, Stack,
  Rating, Divider, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import RateReviewIcon from '@mui/icons-material/RateReview';
import EditIcon from '@mui/icons-material/Edit';
import reviewService from '../services/reviewService';
import quizService from '../services/quizService';
import ReviewItem from '../components/ReviewItem';
import ReviewForm from '../components/ReviewForm';

const QuizReviewsPage = () => {
  const { quizId } = useParams();

  const [quiz, setQuiz] = useState(null);
  const [reviewsData, setReviewsData] = useState({ reviews: [], totalReviews: 0, averageRating: 0 });
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentStudentNickname, setCurrentStudentNickname] = useState('');

  const [showReviewForm, setShowReviewForm] = useState(false);
  const [editingReview, setEditingReview] = useState(null); // This will store the *full review object* when editing
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submissionError, setSubmissionError] = useState(null);

  const [showDeleteConfirm, setShowDeleteConfirm] = useState(false);
  const [reviewToDeleteId, setReviewToDeleteId] = useState(null);

  const fetchQuizAndReviews = useCallback(async () => {
    if (!quizId) {
      setError("Quiz ID is missing.");
      setIsLoading(false);
      return;
    }
    setIsLoading(true);
    setError(null); // Clear previous errors before fetching
    try {
      const [quizDetails, reviewsResponse] = await Promise.all([
        quizService.getQuizById(quizId),
        reviewService.getReviewsForQuiz(quizId)
      ]);
      setQuiz(quizDetails);
      setReviewsData(reviewsResponse || { reviews: [], totalReviews: 0, averageRating: 0 });
    } catch (err) {
      setError(err.message || 'Failed to load data for this page.');
    } finally {
      setIsLoading(false);
    }
  }, [quizId]);

  useEffect(() => {
    setCurrentStudentNickname(reviewService.getCurrentStudentNickname());
    fetchQuizAndReviews();
  }, [fetchQuizAndReviews]); // quizId is a dep of fetchQuizAndReviews

  const handleOpenReviewForm = (reviewToEdit = null) => {
    // If reviewToEdit is provided and has an id, it means we are editing.
    // Otherwise, we are creating a new review.
    setEditingReview(reviewToEdit && reviewToEdit.id ? reviewToEdit : null);
    setShowReviewForm(true);
    setSubmissionError(null);
  };

  const handleCloseReviewForm = () => {
    setShowReviewForm(false);
    setEditingReview(null); // Always clear editing state on close
    setSubmissionError(null);
  };

  const handleSubmitReview = async (reviewFormData) => {
    setIsSubmitting(true);
    setSubmissionError(null);
    try {
      if (editingReview && editingReview.id) { // Check for editingReview and its ID
        // For update, the reviewFormData from ReviewForm includes quizId, nickname, rating, text
        // Ensure your updateReview service sends what the backend expects.
        // The existing editingReview.id is used for the URL.
        await reviewService.updateReview(editingReview.id, reviewFormData);
      } else {
        // For create, reviewFormData also has quizId, nickname, rating, text.
        await reviewService.createReview(quizId, reviewFormData); // quizId here is from useParams
      }
      setShowReviewForm(false);
      setEditingReview(null);
      fetchQuizAndReviews(); // Refresh data
    } catch (err) {
      setSubmissionError(err.message || "Failed to submit review.");
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteReview = (id) => {
    setReviewToDeleteId(id);
    setShowDeleteConfirm(true);
  };

  const confirmDeleteReview = async () => {
    if (!reviewToDeleteId) return;
    setIsSubmitting(true);
    setError(null);
    try {
      await reviewService.deleteReview(reviewToDeleteId);
      fetchQuizAndReviews();
    } catch (err) {
      setError(err.message || "Failed to delete review.");
    } finally {
      setIsSubmitting(false);
      setShowDeleteConfirm(false);
      setReviewToDeleteId(null);
    }
  };

  if (isLoading) {
    return <Container sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight:'70vh', py: 5 }}><CircularProgress /><Typography sx={{ml:2}}>Loading reviews...</Typography></Container>;
  }

  if (!quiz && error) { // If quiz details themselves failed to load initially
    return <Container sx={{ py: 3 }}><Alert severity="error">{error}</Alert></Container>;
  }
  
  const { reviews, totalReviews, averageRating } = reviewsData;

  const userHasReviewed = reviews.some(r => r.studentNickname?.toLowerCase() === currentStudentNickname?.toLowerCase());
  const reviewToEditForButton = userHasReviewed ? reviews.find(r => r.studentNickname?.toLowerCase() === currentStudentNickname?.toLowerCase()) : null;


  return (
    <Container maxWidth="lg" sx={{ py: 3 }}>
      <Paper elevation={3} sx={{ p: {xs:2, md:4} }}>
        <Stack spacing={3}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', flexWrap: 'wrap', gap: 2 }}>
            <Typography variant="h4" component="h1" sx={{ display: 'flex', alignItems: 'center', gap:1 }}>
                <RateReviewIcon color="primary" sx={{fontSize: '2.2rem'}}/>
                Reviews for "{quiz?.title || 'Quiz'}"
            </Typography>
            <Button
              variant="outlined"
              startIcon={<ArrowBackIcon />}
              component={RouterLink}
              to={`/student/quizzes/${quizId}`}
            >
              Back to Quiz Details
            </Button>
          </Box>

          {error && !showReviewForm && ( // Show general page error if not related to form submission
            <Alert severity="error" onClose={() => setError(null)} sx={{width: '100%'}}>{error}</Alert>
          )}

          <Paper variant="outlined" sx={{ p: 2, display: 'flex', justifyContent: 'space-around', alignItems: 'center', flexWrap: 'wrap', gap: 2, bgcolor: 'grey.100' }}>
            <Box sx={{textAlign: 'center'}}>
              <Typography variant="h6">{totalReviews}</Typography>
              <Typography variant="caption" color="text.secondary">Total Reviews</Typography>
            </Box>
            <Box sx={{textAlign: 'center'}}>
              <Rating value={averageRating || 0} precision={0.1} readOnly />
              <Typography variant="caption" color="text.secondary">
                ({averageRating.toFixed(1)} average rating)
              </Typography>
            </Box>
             <Button
                variant="contained"
                color="primary"
                onClick={() => handleOpenReviewForm(reviewToEditForButton)} // Pass the specific review if user has one
                startIcon={<EditIcon />}
                disabled={!quiz?.published}
             >
                {userHasReviewed ? "Edit Your Review" : "Write Your Review"}
            </Button>
          </Paper>

          {showReviewForm && quiz && (
            <ReviewForm
                quizTitle={quiz.title}
                quizId={quizId} // Pass the quizId from useParams
                existingReview={editingReview} // This will be the specific review object or null
                onSubmitReview={handleSubmitReview}
                onCancel={handleCloseReviewForm}
                isSubmitting={isSubmitting}
                submissionError={submissionError}
            />
          )}

          <Divider sx={{ my: 2 }} />
          <Typography variant="h5" component="h2" sx={{mt:1}}>
            Student Feedback
          </Typography>
          { reviews.length === 0 && !isLoading && !error ? ( // If no reviews and not loading/error
            <Typography color="text.secondary" sx={{ fontStyle: 'italic', textAlign: 'center', py: 3 }}>
              Be the first to review this quiz!
            </Typography>
          ) : (
            <Stack spacing={2.5}>
              {reviews.map(review => (
                <ReviewItem
                  key={review.id}
                  review={review}
                  currentStudentNickname={currentStudentNickname}
                  onEdit={() => handleOpenReviewForm(review)} // Pass the specific review object here
                  onDelete={handleDeleteReview}
                />
              ))}
            </Stack>
          )}
        </Stack>
      </Paper>

      <Dialog open={showDeleteConfirm} onClose={() => {if(!isSubmitting) setShowDeleteConfirm(false);}}>
        <DialogTitle>Confirm Delete Review</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to delete this review? This action cannot be undone.
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setShowDeleteConfirm(false)} disabled={isSubmitting}>Cancel</Button>
          <Button onClick={confirmDeleteReview} color="error" autoFocus disabled={isSubmitting}>
            {isSubmitting ? <CircularProgress size={20} color="inherit"/> : "Delete"}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default QuizReviewsPage;