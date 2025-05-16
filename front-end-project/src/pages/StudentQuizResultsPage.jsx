import React, { useState, useEffect } from 'react';
import { useParams, Link as RouterLink } from 'react-router-dom';
import {
    Container, Paper, Typography, CircularProgress, Alert, Button, Box, Stack,
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Chip,
    Divider,
    Grid
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import AssessmentIcon from '@mui/icons-material/Assessment';
import { getQuizResults } from '../services/quizAttemptService';
import quizService from '../services/quizService';

const StudentQuizResultsPage = () => {
    const { quizId } = useParams();
    const [quizDetails, setQuizDetails] = useState(null);
    const [results, setResults] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchQuizDataAndResults = async () => {
            if (!quizId) {
                setError("Quiz ID is missing.");
                setIsLoading(false);
                return;
            }
            setIsLoading(true);
            setError(null);
            try {
                const quizData = await quizService.getQuizById(quizId);
                setQuizDetails(quizData);

                const resultsData = await getQuizResults(quizId);
                setResults(resultsData);

            } catch (err) {
                setError(err.message || 'Could not load quiz results or details.');
            }
            setIsLoading(false);
        };
        fetchQuizDataAndResults();
    }, [quizId]);

    const getDifficultyChipColor = (difficulty) => {
        switch (difficulty?.toUpperCase()) {
            case 'EASY': return 'success';
            case 'MEDIUM': return 'warning';
            case 'HARD': return 'error';
            default: return 'default';
        }
    };

    if (isLoading) {
        return (
            <Container sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '70vh' }}>
                <CircularProgress />
                 <Typography variant="h6" sx={{ ml: 2 }}>Loading quiz results...</Typography>
            </Container>
        );
    }

    if (error) {
        return (
            <Container maxWidth="md" sx={{ py: 3 }}>
                <Paper elevation={2} sx={{p:3}}>
                    <Alert severity="error">{error}</Alert>
                    <Button
                        variant="outlined"
                        startIcon={<ArrowBackIcon />}
                        component={RouterLink}
                        to="/student/quizzes"
                        sx={{ mt: 2 }}
                    >
                        Back to Available Quizzes
                    </Button>
                </Paper>
            </Container>
        );
    }

    if (!results || !results.questionResults || !quizDetails) {
        return (
            <Container maxWidth="md" sx={{ py: 3 }}>
                 <Paper elevation={2} sx={{p:3}}>
                    <Alert severity="info">Quiz results or details are not available for quiz ID: {quizId}.</Alert>
                    <Button
                        variant="outlined"
                        startIcon={<ArrowBackIcon />}
                        component={RouterLink}
                        to="/student/quizzes"
                        sx={{ mt: 2 }}
                    >
                       Back to Available Quizzes
                    </Button>
                </Paper>
            </Container>
        );
    }

    const totalQuestionsInResults = results.questionResults.length;
    const overallTotalAnswers = results.questionResults.reduce((sum, qr) => sum + qr.totalAnswers, 0);
    const overallCorrectAnswers = results.questionResults.reduce((sum, qr) => sum + qr.correctAnswers, 0);
    const overallAccuracy = overallTotalAnswers > 0 ? ((overallCorrectAnswers / overallTotalAnswers) * 100).toFixed(1) : 0;


    return (
        <Container maxWidth="lg" sx={{ py: 3 }}>
            <Paper elevation={3} sx={{ p: {xs: 2, sm: 4} }}>
                <Stack spacing={3}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1}}>
                        <AssessmentIcon color="primary" sx={{fontSize: '2.5rem'}} />
                        <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold' }}>
                            Quiz Results: {results.quizTitle || quizDetails.title || 'Quiz'}
                        </Typography>
                    </Box>
                    <Divider />

                     <Typography variant="h6" component="h2" sx={{ mt: 2 }}>Overall Performance</Typography>
                    <Grid container spacing={2} sx={{p:2, border: '1px solid', borderColor: 'divider', borderRadius: 1}}>
                        <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="body1">Total Questions in Quiz:</Typography>
                            <Typography variant="h5" component="p" sx={{fontWeight: 'bold'}}>{totalQuestionsInResults}</Typography>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="body1">Total Answers Submitted (All Users):</Typography>
                            <Typography variant="h5" component="p" sx={{fontWeight: 'bold'}}>{overallTotalAnswers}</Typography>
                        </Grid>
                        <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="body1">Overall Correct Answers (All Users):</Typography>
                            <Typography variant="h5" component="p" color="success.main" sx={{fontWeight: 'bold'}}>{overallCorrectAnswers}</Typography>
                        </Grid>
                         <Grid item xs={12} sm={6} md={3}>
                            <Typography variant="body1">Overall Accuracy (All Users):</Typography>
                            <Typography variant="h5" component="p" color={overallAccuracy >= 50 ? "success.main" : "error.main"} sx={{fontWeight: 'bold'}}>
                                {overallAccuracy}%
                            </Typography>
                        </Grid>
                    </Grid>
                    <Divider sx={{my:2}} />


                    <Typography variant="h6" component="h2">Detailed Question Performance</Typography>
                    {results.questionResults.length === 0 ? (
                        <Alert severity="info">No detailed results found for individual questions in this quiz yet.</Alert>
                    ) : (
                        <TableContainer component={Paper} variant="outlined">
                            <Table sx={{ minWidth: 650 }} aria-label="quiz results table">
                                <TableHead sx={{ backgroundColor: 'action.hover' }}>
                                    <TableRow>
                                        <TableCell sx={{ fontWeight: 'bold', width: '40%' }}>Question</TableCell>
                                        <TableCell align="center" sx={{ fontWeight: 'bold' }}>Difficulty</TableCell>
                                        <TableCell align="right" sx={{ fontWeight: 'bold' }}>Submissions</TableCell>
                                        <TableCell align="right" sx={{ fontWeight: 'bold' }}>Correct</TableCell>
                                        <TableCell align="right" sx={{ fontWeight: 'bold' }}>Incorrect</TableCell>
                                        <TableCell align="right" sx={{ fontWeight: 'bold' }}>Accuracy</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {results.questionResults.map((qResult) => {
                                        const accuracy = qResult.totalAnswers > 0 ? ((qResult.correctAnswers / qResult.totalAnswers) * 100).toFixed(1) : 0;
                                        return (
                                            <TableRow
                                                key={qResult.questionId}
                                                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
                                            >
                                                <TableCell component="th" scope="row">
                                                    {qResult.questionText}
                                                </TableCell>
                                                <TableCell align="center">
                                                    <Chip label={qResult.questionDifficulty || 'N/A'} color={getDifficultyChipColor(qResult.questionDifficulty)} size="small" />
                                                </TableCell>
                                                <TableCell align="right">{qResult.totalAnswers}</TableCell>
                                                <TableCell align="right" sx={{ color: 'success.dark', fontWeight: 'medium' }}>
                                                    {qResult.correctAnswers}
                                                </TableCell>
                                                <TableCell align="right" sx={{ color: 'error.dark', fontWeight: 'medium' }}>
                                                    {qResult.wrongAnswers}
                                                </TableCell>
                                                <TableCell align="right" sx={{ fontWeight: 'medium', color: accuracy >= 50 ? 'success.dark' : 'error.dark' }}>
                                                    {accuracy}%
                                                </TableCell>
                                            </TableRow>
                                        );
                                    })}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    )}
                    <Box sx={{ mt: 4, display: 'flex', justifyContent: 'flex-start' }}>
                        <Button
                            variant="outlined"
                            startIcon={<ArrowBackIcon />}
                            component={RouterLink}
                            to="/student/quizzes"
                        >
                            Back to Available Quizzes
                        </Button>
                    </Box>
                </Stack>
            </Paper>
        </Container>
    );
};

export default StudentQuizResultsPage;