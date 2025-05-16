import React, { useState, useEffect } from 'react';
import { useParams, Link as RouterLink, useNavigate } from 'react-router-dom';
import { 
    Container, Paper, Typography, CircularProgress, Alert, Button, Box, Grid, Stack 
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import { useQuiz } from '../hooks/useQuiz'; 
import QuizAttemptInterface from '../components/QuizAttemptInterface';
import { quizAttemptService } from '../services/quizAttemptService'; 

const StudentQuizDetail = () => {
    const { id: quizId } = useParams();
    const navigate = useNavigate();
    const { quiz, isLoading, error } = useQuiz(quizId); 

    const [showAttemptInterface, setShowAttemptInterface] = useState(false);
    const [attemptId, setAttemptId] = useState(null);
    const [attemptError, setAttemptError] = useState(null);
    const [isStartingAttempt, setIsStartingAttempt] = useState(false);

    const handleStartQuiz = async () => {
        setIsStartingAttempt(true);
        setAttemptError(null);
        try {
           
            const studentId = null; 
            const attemptData = await quizAttemptService.startQuizAttempt(quizId, studentId);
            setAttemptId(attemptData.id);
            setShowAttemptInterface(true);
        } catch (err) {
            console.error("Failed to start quiz attempt:", err);
            setAttemptError(err.message || 'Could not start the quiz. Please try again.');
        }
        setIsStartingAttempt(false);
    };

    const handleAttemptComplete = () => {
        setShowAttemptInterface(false);
        navigate(`/quizzes/${quizId}/results`);
    };

    if (isLoading) {
        return (
            <Container sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '80vh' }}>
                <CircularProgress />
            </Container>
        );
    }

    if (error) {
        return (
            <Container>
                <Alert severity="error">{error}</Alert>
                <Button 
                    variant="outlined"
                    startIcon={<ArrowBackIcon />}
                    component={RouterLink} 
                    to="/published-quizzes"
                    sx={{ mt: 2 }}
                >
                    Back to Published Quizzes
                </Button>
            </Container>
        );
    }

    if (!quiz) {
        return (
            <Container>
                <Alert severity="warning">Quiz not found.</Alert>
                <Button 
                    variant="outlined"
                    startIcon={<ArrowBackIcon />}
                    component={RouterLink} 
                    to="/published-quizzes"
                    sx={{ mt: 2 }}
                >
                    Back to Published Quizzes
                </Button>
            </Container>
        );
    }

    if (showAttemptInterface && attemptId) {
        return (
            <QuizAttemptInterface
                attemptId={attemptId}
                questions={quiz.questions || []} // Ensure questions are passed
                onAttemptComplete={handleAttemptComplete}
                quizTitle={quiz.title || quiz.name || 'Quiz'}
            />
        );
    }

    return (
        <Container maxWidth="md" sx={{ mt: 4, mb: 4 }}>
            <Paper elevation={3} sx={{ p: 3 }}>
                <Stack spacing={2}>
                    <Typography variant="h4" component="h1" gutterBottom>
                        {quiz.title || quiz.name || 'Quiz Details'}
                    </Typography>
                    
                    <Typography variant="h6">Description:</Typography>
                    <Typography variant="body1" sx={{ whiteSpace: 'pre-line' }}>
                        {quiz.description || 'No description available.'}
                    </Typography>

                    <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                            <Typography variant="subtitle1"><strong>Course Code:</strong> {quiz.courseCode || 'N/A'}</Typography>
                        </Grid>
                        <Grid item xs={12} sm={6}>
                            <Typography variant="subtitle1"><strong>Category:</strong> {quiz.category?.name || 'N/A'}</Typography>
                        </Grid>
                        {/* Add other details like difficulty, topic if available in quiz object */}
                        {/* e.g., <Typography variant="subtitle1"><strong>Difficulty:</strong> {quiz.difficultyLevel || 'N/A'}</Typography> */}
                    </Grid>

                    {attemptError && (
                        <Alert severity="error" sx={{ mt: 2 }}>{attemptError}</Alert>
                    )}

                    <Box sx={{ mt: 3, display: 'flex', justifyContent: 'space-between' }}>
                        <Button 
                            variant="outlined"
                            startIcon={<ArrowBackIcon />}
                            component={RouterLink} 
                            to="/published-quizzes"
                        >
                            Back to Published Quizzes
                        </Button>
                        <Button 
                            variant="contained"
                            color="primary"
                            startIcon={<PlayArrowIcon />}
                            onClick={handleStartQuiz}
                            disabled={isStartingAttempt}
                        >
                            {isStartingAttempt ? <CircularProgress size={24} color="inherit" /> : 'Start Quiz'}
                        </Button>
                    </Box>
                </Stack>
            </Paper>
        </Container>
    );
};

export default StudentQuizDetail; 