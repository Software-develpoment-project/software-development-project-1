import React, { useState, useEffect } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import {
    Container, Typography, CircularProgress, Alert, Button, Box, Stack, Paper,
    List, ListItem, ListItemButton, ListItemText, ListItemIcon, Divider,
    IconButton 
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import CategoryIcon from '@mui/icons-material/Category';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import categoryService from '../services/categoryService';

const StudentCategoryListPage = () => {
    const [categories, setCategories] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchCategories = async () => {
            setIsLoading(true);
            setError(null);
            try {
                const data = await categoryService.getAllCategories();
                setCategories(Array.isArray(data) ? data : []);
            } catch (err) {
                console.error("Failed to fetch categories:", err);
                setError(err.message || 'Could not load categories. Please try again.');
            }
            setIsLoading(false);
        };
        fetchCategories();
    }, []);

    if (isLoading) {
        return (
            <Container sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', minHeight: '70vh' }}>
                <CircularProgress />
                <Typography variant="h6" sx={{ml: 2}}>Loading categories...</Typography>
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
                        to="/student/quizzes"
                        sx={{ mt: 2 }}
                    >
                        Back to Quizzes
                    </Button>
                </Paper>
            </Container>
        );
    }

    return (
        <Container maxWidth="lg" sx={{ py: 3 }}>
            <Paper elevation={3} sx={{ p: {xs:2, sm:4} }}>
                <Stack spacing={3}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, mb: 1 }}>
                        <CategoryIcon color="primary" sx={{fontSize: '2.5rem'}}/>
                        <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold' }}>
                            Browse Quiz Categories
                        </Typography>
                    </Box>
                    <Divider/>

                    {categories.length === 0 ? (
                         <Alert severity="info" sx={{mt:2}}>No quiz categories are available at the moment.</Alert>
                    ) : (
                       
                        <List component={Paper} variant="outlined" sx={{ borderRadius: 1}}>
                            {categories.map((category, index) => (
                                <React.Fragment key={category.id}>
                                    <ListItem
                                        disablePadding
                                        secondaryAction={
                                            <IconButton edge="end" aria-label="view quizzes in category" component={RouterLink} to={`/student/categories/${category.id}/quizzes`}>
                                                <ChevronRightIcon />
                                            </IconButton>
                                        }
                                    >
                                        <ListItemButton component={RouterLink} to={`/student/categories/${category.id}/quizzes`}>
                                            <ListItemIcon>
                                                <CategoryIcon />
                                            </ListItemIcon>
                                            <ListItemText
                                                primary={category.name || 'Unnamed Category'}
                                                secondary={category.description || 'No description available.'}
                                                primaryTypographyProps={{ fontWeight: 'medium', color: 'text.primary' }}
                                                secondaryTypographyProps={{ color: 'text.secondary', noWrap: true, textOverflow: 'ellipsis' }}
                                            />
                                        </ListItemButton>
                                    </ListItem>
                                    {index < categories.length - 1 && <Divider component="li" />}
                                </React.Fragment>
                            ))}
                        </List>
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

export default StudentCategoryListPage;