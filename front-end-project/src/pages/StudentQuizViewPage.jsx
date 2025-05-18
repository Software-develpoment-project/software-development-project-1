import React, { useState, useEffect } from 'react';
import { useParams, Link as RouterLink, useNavigate } from 'react-router-dom';
import {
    Container, Paper, Typography, CircularProgress, Alert, Button, Box, Grid, Stack, Chip 
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import RateReviewIcon from '@mui/icons-material/RateReview'; 
import quizService from '../services/quizService';
import QuizAttemptInterface from '../components/QuizAttemptInterface';
import { startQuizAttempt } from '../services/quizAttemptService';

const StudentQuizViewPage = () => {
    const { quizId } = useParams();
    const navigate = useNavigate();

    const [quiz, setQuiz] = useState(null);
    const [isLoadingQuiz, setIsLoadingQuiz] = useState(true);
    const [quizError, setQuizError] = useState(null);

    const [showAttemptInterface, setShowAttemptInterface] = useState(false);
    const [attemptId, setAttemptId] = useState(null);
    const [attemptError, setAttemptError] = useState(null);
    const [isStartingAttempt, setIsStartingAttempt] = useState(false);

    useEffect(() => {
        const fetchQuizDetails = async () => {
            if (!quizId) {
                setQuizError("No Quiz ID provided.");
                setIsLoadingQuiz(false);
                return;
            }
            setIsLoadingQuiz(true);
            setQuizError(null);
            try {
                const data = await quizService.getQuizById(quizId);
                if (!data.published) {
                    setQuizError("This quiz is not currently published and cannot be attempted.");
                    setQuiz(null);
                } else {
                    setQuiz(data);
                }
            } catch (err) {
                setQuizError(err.message || 'Could not load quiz details.');
            }
            setIsLoadingQuiz(false);
        };
        fetchQuizDetails();
    }, [quizId]);


    const handleStartQuiz = async () => {
        if (!quiz || !quiz.published) {
            setAttemptError("Cannot start: Quiz is not available or not published.");
            return;
        }
        setIsStartingAttempt(true);
        setAttemptError(null);
        try {
            const studentId = null;
            const attemptData = await startQuizAttempt(quizId, studentId);
            if (attemptData && attemptData.id) {
                setAttemptId(attemptData.id);
                setShowAttemptInterface(true);
            } else {
                throw new Error(attemptData?.message || "Failed to initialize quiz attempt.");
            }
        } catch (err) {
            setAttemptError(err.message || 'Could not start the quiz. Please try again.');
        }
        setIsStartingAttempt(false);
    };

    const handleAttemptComplete = () => {
        setShowAttemptInterface(false);
        navigate(`/student/quizzes/${quizId}/results`);
    };

   
    if (isLoadingQuiz) { return <Container sx={{display:'flex', justifyContent:'center'}}><CircularProgress /></Container> }
    if (quizError) {  return <Container><Alert severity="error">{quizError}</Alert></Container> }
    if (!quiz) { return <Container><Alert severity="warning">Quiz not found.</Alert></Container> }


    if (showAttemptInterface && attemptId && quiz.questions) {
        return (
            <QuizAttemptInterface
                attemptId={attemptId}
                questions={quiz.questions}
                onAttemptComplete={handleAttemptComplete}
                quizTitle={quiz.title || 'Quiz Attempt'}
            />
        );
    }

    return (
        <Container maxWidth="md" sx={{ py: 3 }}>
            <Paper elevation={3} sx={{ p: { xs: 2, sm: 4 } }}>
                <Stack spacing={3}>
                    <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: 'bold', textAlign: 'center' }}>
                        {quiz.title || 'Quiz Details'}
                    </Typography>

                    {quiz.description && (
                         <Paper variant="outlined" sx={{ p: 2, backgroundColor: 'grey.50' }}>
                            <Typography variant="h6" component="h2" sx={{ mb: 1 }}>Description:</Typography>
                            <Typography variant="body1" sx={{ whiteSpace: 'pre-line', color: 'text.secondary' }}>
                                {quiz.description}
                            </Typography>
                        </Paper>
                    )}

                    <Grid container spacing={1}>
                        <Grid xs={12} sm={6}>
                            <Typography variant="subtitle1">
                                <strong>Course Code:</strong> {quiz.courseCode || 'N/A'}
                            </Typography>
                        </Grid>
                        <Grid xs={12} sm={6}>
                            <Typography variant="subtitle1">
                                <strong>Category:</strong> {quiz.category?.name || 'N/A'}
                            </Typography>
                        </Grid>
                        <Grid xs={12}>
                           <Chip
                                label={quiz.published ? 'Available' : 'Not Available'}
                                color={quiz.published ? 'success' : 'error'}
                                size="small"
                           />
                        </Grid>
                    </Grid>

                    {attemptError && (
                        <Alert severity="error" onClose={() => setAttemptError(null)}>{attemptError}</Alert>
                    )}

                    <Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between', alignItems: 'center', gap: 2, flexWrap: 'wrap' }}>
                        <Button
                            variant="outlined"
                            startIcon={<ArrowBackIcon />}
                            component={RouterLink}
                            to="/student/quizzes"
                        >
                            Back to Quizzes
                        </Button>
                        <Button
                            variant="outlined"
                            color="secondary" 
                            startIcon={<RateReviewIcon />}
                            component={RouterLink}
                            to={`/student/quizzes/${quizId}/reviews`}
                        >
                            Read/Write Reviews
                        </Button>
                        <Button
                            variant="contained"
                            color="primary"
                            size="large"
                            startIcon={isStartingAttempt ? <CircularProgress size={20} color="inherit"/> : <PlayArrowIcon />}
                            onClick={handleStartQuiz}
                            disabled={isStartingAttempt || !quiz.published || !quiz.questions || quiz.questions.length === 0}
                        >
                            {isStartingAttempt ? 'Starting...' : (quiz.questions && quiz.questions.length > 0 ? 'Start Quiz Attempt' : 'No Questions Available')}
                        </Button>
                    </Box>
                     {quiz.questions && quiz.questions.length === 0 && quiz.published &&
                        <Alert severity="warning" sx={{mt: 2}}>This quiz is published but currently has no questions.</Alert>
                     }
                </Stack>
            </Paper>
        </Container>
    );
};

export default StudentQuizViewPage;