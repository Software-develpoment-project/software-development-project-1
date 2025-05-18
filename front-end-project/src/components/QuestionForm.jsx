import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  TextField,
  Button,
  Alert,
  CircularProgress,
  Typography,
  FormControl,
  FormLabel,
  RadioGroup,
  FormControlLabel,
  Radio,
  Box,
  Stack,
  Grid
} from '@mui/material';
import quizService from '../services/quizService';

const QuestionForm = ({
  quizId,
  question = null,
  onSubmit = null,
  buttonLabel = 'Save Question',
  cancelRoute = null
}) => {
  const [formData, setFormData] = useState({
    content: question?.content || '',
    quizId: quizId || question?.quizId || '',
    difficultyLevel: question?.difficultyLevel || 'MEDIUM'
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [validationErrors, setValidationErrors] = useState({});

  const navigate = useNavigate();

  const defaultCancelRoute = quizId ? `/quizzes/${quizId}` : '/quizzes';

  useEffect(() => {
    if (question) {
      setFormData({
        content: question.content || '',
        quizId: question.quizId || quizId || '',
        difficultyLevel: question.difficultyLevel || 'MEDIUM',
      });
    }
  }, [question, quizId]);

  const validate = () => {
    const errors = {};
    if (!formData.content.trim()) errors.content = 'Question text is required';
    else if (formData.content.length < 5) errors.content = 'Question text must be at least 5 characters';
    else if (formData.content.length > 1000) errors.content = 'Question text must be less than 1000 characters';

    if (!formData.quizId) errors.quizId = 'Quiz ID is missing. Cannot save question.';

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

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    setError(null);

    const payload = {
        questionText: formData.content,
        difficultyLevel: formData.difficultyLevel,
        quizId: formData.quizId
    };

    try {
      let result;
      if (onSubmit) {
        result = await onSubmit(payload);
      } else {
        if (question && question.id) {
          throw new Error('Editing questions via this form is not fully supported yet.');
        } else {
          result = await quizService.questions.create(formData.quizId, payload);
        }
      }
      navigate(cancelRoute || defaultCancelRoute);
    } catch (err) {
      setError(err.message || 'Failed to save question. Please try again.');
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
            name="content"
            label="Question Text"
            multiline
            rows={4}
            value={formData.content}
            onChange={handleChange}
            error={!!validationErrors.content}
            helperText={validationErrors.content}
            disabled={loading}
            required
            fullWidth
            autoFocus
          />
        </Grid>
        <Grid item xs={12}>
          <FormControl component="fieldset" disabled={loading} error={!!validationErrors.difficultyLevel} fullWidth>
            <FormLabel component="legend">Difficulty Level</FormLabel>
            <RadioGroup
              row
              aria-label="difficulty level"
              name="difficultyLevel"
              value={formData.difficultyLevel}
              onChange={handleChange}
            >
              <FormControlLabel value="EASY" control={<Radio />} label="Easy" />
              <FormControlLabel value="MEDIUM" control={<Radio />} label="Medium" />
              <FormControlLabel value="HARD" control={<Radio />} label="Hard" />
            </RadioGroup>
            {validationErrors.difficultyLevel && <Typography color="error" variant="caption">{validationErrors.difficultyLevel}</Typography>}
          </FormControl>
        </Grid>
      </Grid>

      <Stack direction="row" spacing={2} sx={{ mt: 3, justifyContent: 'flex-end' }}>
        <Button
          variant="outlined"
          onClick={() => navigate(cancelRoute || defaultCancelRoute)}
          disabled={loading}
        >
          Cancel
        </Button>
        <Button
          variant="contained"
          type="submit"
          disabled={loading || !formData.quizId}
          startIcon={loading ? <CircularProgress size={20} /> : null}
        >
          {loading ? 'Saving...' : buttonLabel}
        </Button>
      </Stack>
    </Box>
  );
};

export default QuestionForm;