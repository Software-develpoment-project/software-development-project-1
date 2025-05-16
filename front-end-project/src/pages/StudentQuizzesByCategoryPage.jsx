import React, { useState, useEffect } from 'react';
import { useParams, Link as RouterLink, useNavigate } from 'react-router-dom';
import {
    Container, Typography, CircularProgress, Alert, Button, Box, Grid,
    Card, CardContent, CardActions, Link as MuiLink, Chip, Paper, Stack, Divider
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import OpenInNewIcon from '@mui/icons-material/OpenInNew';
import CategoryIcon from '@mui/icons-material/Category';
import categoryService from '../services/categoryService'; 

const StudentQuizzesByCategoryPage = () => {
    const { id: categoryId } = useParams();
    const navigate = useNavigate();
    const [category, setCategory] = useState(null);
    const [quizzes, setQuizzes] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            if (!categoryId) {
                setError("Category ID is missing.");
                setIsLoading(false);
                return;
            }
            setIsLoading(true);
            setError(null);
            try {
                const categoryData = await categoryService.getCategoryById(categoryId);
                setCategory(categoryData);

                const quizzesData = await categoryService.getPublishedQuizzesByCategoryId(categoryId);
                setQuizzes(Array.isArray(quizzesData) ? quizzesData : []);

            } catch (err) {
                console.error("Failed to fetch data for category page:", err);
                setError(err.message || 'Could not load data for this category.');
            }
            setIsLoading(false);
        };
        fetchData();
    }, [categoryId]);

    if (isLoading) {
        return (
            <Container sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '70vh' }}>
                <CircularProgress />
                <Typography variant="h6" sx={{ml: 2}}>Loading quizzes...</Typography>
            </Container>
        );
    }

    if (error) {
        return (
            <Container maxWidth="md" sx={{py:3}}>
                <Paper elevation={2} sx={{p:3}}>
                    <Alert severity="error">{error}</Alert>
                    <Button
                        variant="outlined"
                        startIcon={<ArrowBackIcon />}
                        component={RouterLink}
                        to="/student/categories"
                        sx={{ mt: 2 }}
                    >
                        Back to All Categories
                    </Button>
                </Paper>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" sx={{ py: 3 }}>
            <Paper elevation={3} sx={{p:{xs:2, sm:4}}}>
                <Stack spacing={3}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                        <CategoryIcon color="primary" sx={{fontSize: '2.5rem'}}/>
                        <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold' }}>
                            Quizzes in: {category?.name || 'Selected Category'}
                        </Typography>
                    </Box>
                    {category?.description && (
                        <Typography variant="body1" color="text.secondary" sx={{fontStyle:'italic'}}>
                            {category.description}
                        </Typography>
                    )}
                    <Divider />

                    {quizzes.length === 0 ? (
                        <Alert severity="info" sx={{mt:2}}>
                            No published quizzes found in the "{category?.name || 'selected'}" category.
                        </Alert>
                    ) : (
                        <Grid container spacing={3}>
                            {quizzes.map((quiz) => (
                                <Grid item xs={12} sm={6} md={4} key={quiz.id}>
                                    <Card sx={{ height: '100%', display: 'flex', flexDirection: 'column', transition: 'box-shadow 0.3s', '&:hover': { boxShadow: 7,  transform: 'translateY(-2px)'} }}>
                                        <CardContent sx={{ flexGrow: 1 }}>
                                            <Typography gutterBottom variant="h5" component="div">
                                                <MuiLink component={RouterLink} to={`/student/quizzes/${quiz.id}`} underline="hover" color="primary.main">
                                                    {quiz.title || 'Untitled Quiz'}
                                                </MuiLink>
                                            </Typography>
                                            <Typography variant="body2" color="text.secondary" sx={{ mb: 1, minHeight: '60px', display: '-webkit-box', WebkitLineClamp: 3, WebkitBoxOrient: 'vertical', overflow: 'hidden', textOverflow: 'ellipsis' }}>
                                                {quiz.description || 'No description provided.'}
                                            </Typography>
                                            <Chip label={`Course: ${quiz.courseCode || 'N/A'}`} size="small" variant="outlined" sx={{bgcolor: 'action.hover'}}/>
                                        </CardContent>
                                        <CardActions sx={{ justifyContent: 'flex-start', p:2, borderTop: '1px solid', borderColor: 'divider' }}>
                                            <Button
                                                size="small"
                                                variant="contained"
                                                component={RouterLink}
                                                to={`/student/quizzes/${quiz.id}`}
                                                startIcon={<OpenInNewIcon />}
                                            >
                                                View & Start Quiz
                                            </Button>
                                        </CardActions>
                                    </Card>
                                </Grid>
                            ))}
                        </Grid>
                    )}

                    <Box sx={{ mt: 4, display: 'flex', justifyContent: 'flex-start' }}>
                        <Button
                            variant="outlined"
                            startIcon={<ArrowBackIcon />}
                            component={RouterLink}
                            to="/student/categories"
                        >
                            Back to All Categories
                        </Button>
                    </Box>
                </Stack>
            </Paper>
        </Container>
    );
};

export default StudentQuizzesByCategoryPage;