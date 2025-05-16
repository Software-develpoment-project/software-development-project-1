import React, { useState, useEffect } from 'react';
import {
  Box, TextField, Button, Typography, Rating, Stack, CircularProgress, Alert, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio
} from '@mui/material';

const getStoredNickname = () => localStorage.getItem('studentNickname') || '';
const setStoredNickname = (nickname) => localStorage.setItem('studentNickname', nickname);

const ReviewForm = ({
  quizTitle,
  quizId,
  existingReview = null,
  onSubmitReview,
  onCancel,
  isSubmitting = false,
  submissionError = null
}) => {
  const [nickname, setNickname] = useState(existingReview?.studentNickname || getStoredNickname());
  const [rating, setRating] = useState(existingReview?.rating || 0);
  const [reviewText, setReviewText] = useState(existingReview?.reviewText || '');
  const [validationErrors, setValidationErrors] = useState({});

  useEffect(() => {
    if (existingReview) {
      setNickname(existingReview.studentNickname || getStoredNickname());
      setRating(existingReview.rating || 0);
      setReviewText(existingReview.reviewText || '');
    } else {
      setNickname(getStoredNickname());
    }
  }, [existingReview]);

  const validateForm = () => {
    const errors = {};
    if (!nickname.trim()) errors.nickname = 'Nickname is required.';
    else if (nickname.length < 3) errors.nickname = 'Nickname must be at least 3 characters.';
    else if (nickname.length > 100) errors.nickname = 'Nickname must be at most 100 characters.';

    if (rating === 0 || rating === null) errors.rating = 'Rating is required (select 1-5).';
    else if (rating < 1 || rating > 5) errors.rating = 'Rating must be between 1 and 5.';

    if (!reviewText.trim()) errors.reviewText = 'Review text is required.';
    else if (reviewText.length < 10) errors.reviewText = 'Review must be at least 10 characters.';
    else if (reviewText.length > 2000) errors.reviewText = 'Review must be at most 2000 characters.';

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    if (!validateForm()) return;

    if (!existingReview) {
        setStoredNickname(nickname);
    }

    onSubmitReview({
      studentNickname: nickname,
      rating: Number(rating),
      reviewText,
      quizId: Number(quizId)
    });
  };

  const ratingOptions = [
    { value: 1, label: '1 - Useless' },
    { value: 2, label: '2 - Poor' },
    { value: 3, label: '3 - Ok' },
    { value: 4, label: '4 - Good' },
    { value: 5, label: '5 - Excellent' },
  ];

  return (
    <Box component="form" onSubmit={handleSubmit} sx={{ mt: 1, p:2, border: '1px solid', borderColor: 'divider', borderRadius: 2 }}>
      <Typography variant="h5" gutterBottom component="h2" sx={{textAlign:'center', mb: 2}}>
        {existingReview ? `Edit Your Review for "${quizTitle}"` : `Add a Review for "${quizTitle}"`}
      </Typography>

      {submissionError && <Alert severity="error" sx={{ mb: 2 }} onClose={onCancel}>{submissionError}</Alert>}

      <Stack spacing={2.5}>
        <TextField
          label="Nickname"
          name="nickname"
          value={nickname}
          onChange={(e) => {
            setNickname(e.target.value);
            if(validationErrors.nickname) setValidationErrors(prev => ({...prev, nickname: ''}));
          }}
          error={!!validationErrors.nickname}
          helperText={validationErrors.nickname}
          fullWidth
          required
          variant="outlined"
          disabled={isSubmitting}
        />

        <FormControl component="fieldset" error={!!validationErrors.rating} disabled={isSubmitting}>
            <FormLabel component="legend" required>Rating</FormLabel>
            <RadioGroup
                aria-label="rating"
                name="rating"
                value={rating ? rating.toString() : ''}
                onChange={(e) => {
                    setRating(Number(e.target.value));
                     if(validationErrors.rating) setValidationErrors(prev => ({...prev, rating: ''}));
                }}
                row
                sx={{justifyContent: 'space-around', mt:0.5}}
            >
                {ratingOptions.map(opt => (
                    <FormControlLabel
                        key={opt.value}
                        value={opt.value.toString()}
                        control={<Radio />}
                        label={opt.label}
                    />
                ))}
            </RadioGroup>
            {validationErrors.rating && <Typography color="error" variant="caption" sx={{ml:1.5}}>{validationErrors.rating}</Typography>}
        </FormControl>

        <TextField
          label="Review"
          name="reviewText"
          multiline
          rows={5}
          value={reviewText}
          onChange={(e) => {
            setReviewText(e.target.value);
            if(validationErrors.reviewText) setValidationErrors(prev => ({...prev, reviewText: ''}));
          }}
          error={!!validationErrors.reviewText}
          helperText={validationErrors.reviewText}
          fullWidth
          required
          variant="outlined"
          disabled={isSubmitting}
        />
        <Stack direction="row" spacing={2} justifyContent="flex-end">
          {onCancel && (
            <Button onClick={onCancel} variant="outlined" disabled={isSubmitting}>
              Cancel
            </Button>
          )}
          <Button
            type="submit"
            variant="contained"
            color="primary"
            disabled={isSubmitting}
            startIcon={isSubmitting ? <CircularProgress size={20} color="inherit"/> : null}
          >
            {isSubmitting ? (existingReview ? 'Updating...' : 'Submitting...') : (existingReview ? 'Update Review' : 'Submit Your Review')}
          </Button>
        </Stack>
      </Stack>
    </Box>
  );
};

export default ReviewForm;