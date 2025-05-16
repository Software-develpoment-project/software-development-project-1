import React, { useState, useEffect } from 'react';
import {
    Container, Paper, Typography, FormControl, FormLabel, RadioGroup, FormControlLabel, Radio,
    Button, CircularProgress, Alert, Box, LinearProgress, Stack, IconButton,
} from '@mui/material';
import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import HighlightOffIcon from '@mui/icons-material/HighlightOff';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { submitStudentAnswer } from '../services/quizAttemptService';

const QuizAttemptInterface = ({ attemptId, questions, onAttemptComplete, quizTitle }) => {
    const [currentQuestionIndex, setCurrentQuestionIndex] = useState(0);
    const [selectedAnswerOptionId, setSelectedAnswerOptionId] = useState(null);
    const [isSubmitting, setIsSubmitting] = useState(false);
    const [submissionError, setSubmissionError] = useState(null);
    const [submissionSuccess, setSubmissionSuccess] = useState(false);
    const [answeredQuestions, setAnsweredQuestions] = useState({});

    const totalQuestions = questions.length;
    const currentQuestion = questions[currentQuestionIndex];

    useEffect(() => {
        setSelectedAnswerOptionId(null);
        setSubmissionSuccess(false);
        setSubmissionError(null);
    }, [currentQuestionIndex]);

    const handleAnswerChange = (event) => {
        setSelectedAnswerOptionId(event.target.value);
        setSubmissionError(null);
    };

    const handleSubmitAnswer = async () => {
        if (!selectedAnswerOptionId) {
            setSubmissionError('Please select an answer before submitting.');
            return;
        }
        setIsSubmitting(true);
        setSubmissionError(null);
        setSubmissionSuccess(false);

        try {
            const submissionDTO = { answerOptionId: selectedAnswerOptionId };
            const result = await submitStudentAnswer(attemptId, currentQuestion.id, submissionDTO);

            setAnsweredQuestions(prev => ({
                ...prev,
                [currentQuestion.id]: {
                    chosenAnswerId: selectedAnswerOptionId,
                    isCorrect: result.correct
                }
            }));
            setSubmissionSuccess(true);

            setTimeout(() => {
                // Potential cleanup or state reset after feedback shown
            }, 1500);

        } catch (err) {
            setSubmissionError(err.message || 'Could not submit your answer. Please try again.');
        }
        setIsSubmitting(false);
    };

    const handleNextQuestion = () => {
        setSubmissionSuccess(false);
        setSubmissionError(null);
        setSelectedAnswerOptionId(null);
        if (currentQuestionIndex < totalQuestions - 1) {
            setCurrentQuestionIndex(currentQuestionIndex + 1);
        }
    };

    const handleFinishQuiz = () => {
        onAttemptComplete();
    };

    if (!currentQuestion) {
        return (
            <Container sx={{ mt: 4, textAlign: 'center' }}>
                <Typography variant="h6" gutterBottom>
                    {totalQuestions > 0 ? "No more questions or quiz ended." : "This quiz has no questions."}
                </Typography>
                <Button onClick={onAttemptComplete} variant="contained" sx={{mt: 2}}>
                    {totalQuestions > 0 ? "Finish & View Results" : "Back to Quiz Details"}
                </Button>
            </Container>
        );
    }

    const progressPercentage = totalQuestions > 0 ? ((Object.keys(answeredQuestions).length) / totalQuestions) * 100 : 0;
    const currentQuestionAnsweredData = answeredQuestions[currentQuestion.id];

    return (
        <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
            <Paper elevation={3} sx={{ p: { xs: 2, sm: 3 } }}>
                <Stack spacing={3}>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <Typography variant="h5" component="h1">{quizTitle}</Typography>
                        <Typography variant="caption">
                            Question {currentQuestionIndex + 1} of {totalQuestions}
                        </Typography>
                    </Box>
                    <Box>
                        <Typography variant="body2" color="text.secondary">Attempt Progress</Typography>
                        <LinearProgress variant="determinate" value={progressPercentage} sx={{ height: 10, borderRadius: 5, mb: 2 }} />
                    </Box>

                    <Paper variant="outlined" sx={{ p: 2,  backgroundColor: 'grey.50' }}>
                        <Typography variant="h6" gutterBottom sx={{fontWeight: 'medium'}}>
                            {currentQuestion.questionText || currentQuestion.content || 'Question text not available'}
                        </Typography>
                        <Typography variant="caption" color="text.secondary">
                            Points: {currentQuestion.points || 1} | Difficulty: {currentQuestion.difficultyLevel || 'N/A'}
                        </Typography>
                    </Paper>

                    <FormControl component="fieldset" disabled={isSubmitting || submissionSuccess}>
                        <FormLabel component="legend" sx={{mb:1}}>Choose your answer:</FormLabel>
                        <RadioGroup
                            aria-label="answer options"
                            name="answer-options-group"
                            value={selectedAnswerOptionId || ''}
                            onChange={handleAnswerChange}
                        >
                            {(currentQuestion.answerOptions || []).map((option) => (
                                <FormControlLabel
                                    key={option.id}
                                    value={option.id.toString()}
                                    control={<Radio />}
                                    label={option.answerText || option.content}
                                    sx={{mb: 0.5, p:0.5, borderRadius:1, '&:hover': {backgroundColor: 'action.hover'}}}
                                />
                            ))}
                        </RadioGroup>
                    </FormControl>

                    {submissionError && <Alert severity="error" sx={{ mt: 2 }} onClose={() => setSubmissionError(null)}>{submissionError}</Alert>}

                    {submissionSuccess && !isSubmitting && currentQuestionAnsweredData && (
                        <Alert
                            severity={currentQuestionAnsweredData.isCorrect ? "success" : "error"}
                            iconMapping={{
                                success: <CheckCircleOutlineIcon fontSize="inherit" />,
                                error: <HighlightOffIcon fontSize="inherit" />,
                            }}
                            sx={{ mt: 2 }}
                        >
                            {currentQuestionAnsweredData.isCorrect ? "That's Correct!" : "That's Incorrect."}
                        </Alert>
                    )}

                    <Box sx={{ mt: 3, display: 'flex', flexDirection: { xs: 'column', sm: 'row' }, justifyContent: 'space-between', gap: 2 }}>
                        <Button
                            variant="outlined"
                            onClick={onAttemptComplete}
                            startIcon={<ArrowBackIcon />}
                            sx={{ width: {xs: '100%', sm: 'auto'}}}
                        >
                            End Attempt
                        </Button>

                        {!submissionSuccess ? (
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={handleSubmitAnswer}
                                disabled={isSubmitting || !selectedAnswerOptionId}
                                sx={{ width: {xs: '100%', sm: 'auto'}}}
                            >
                                {isSubmitting ? <CircularProgress size={24} color="inherit" /> : 'Submit Answer'}
                            </Button>
                        ) : currentQuestionIndex < totalQuestions - 1 ? (
                            <Button
                                variant="contained"
                                color="primary"
                                onClick={handleNextQuestion}
                                sx={{ width: {xs: '100%', sm: 'auto'}}}
                            >
                                Next Question
                            </Button>
                        ) : (
                            <Button
                                variant="contained"
                                color="success"
                                onClick={handleFinishQuiz}
                                sx={{ width: {xs: '100%', sm: 'auto'}}}
                            >
                                Finish Quiz & View Results
                            </Button>
                        )}
                    </Box>
                </Stack>
            </Paper>
        </Container>
    );
};

export default QuizAttemptInterface;