import { BrowserRouter as Router, Routes, Route, Navigate, Link as RouterLink } from 'react-router-dom';
import Layout from './components/Layout';

// Teacher Dashboard Pages
import QuizList from './pages/QuizList';
import QuizDetail from './pages/QuizDetail';
import QuizCreate from './pages/QuizCreate';
import QuizEdit from './pages/QuizEdit';
import QuestionCreate from './pages/QuestionCreate';
import AnswerOptionManage from './pages/AnswerOptionManage';
import AllQuizList from './pages/AllQuizList';
import CategoryList from './pages/CategoryList';
import CategoryCreate from './pages/CategoryCreate';

// Student Dashboard Pages
import StudentPublishedQuizList from './pages/StudentPublishedQuizList';
import StudentQuizViewPage from './pages/StudentQuizViewPage';
import StudentQuizResultsPage from './pages/StudentQuizResultsPage';
import StudentCategoryListPage from './pages/StudentCategoryListPage';
import StudentQuizzesByCategoryPage from './pages/StudentQuizzesByCategoryPage';
import QuizReviewsPage from './pages/QuizReviewsPage'; // **** NEW IMPORT ****
// AddReviewPage might not be needed if ReviewForm is embedded
// EditReviewPage might not be needed if ReviewForm is embedded and prefilled

import {
  Container, Button, Alert, Typography, Box, Paper
} from '@mui/material';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Navigate to="/student/quizzes" replace />} />
          
          {/* Teacher Routes */}
          <Route path="/quizzes" element={<QuizList />} />
          <Route path="/quizzes/new" element={<QuizCreate />} />
          <Route path="/quizzes/:id" element={<QuizDetail />} />
          <Route path="/quizzes/:id/edit" element={<QuizEdit />} />
          <Route path="/categories" element={<CategoryList />} />
          <Route path="/categories/new" element={<CategoryCreate />} />
          <Route path="/quizzes/:quizId/questions/new" element={<QuestionCreate />} />
          <Route path="/questions/:questionId/answers" element={<AnswerOptionManage />} />
          <Route path="/all-quizzes" element={<AllQuizList />} />

          {/* Student Routes */}
          <Route path="/student/quizzes" element={<StudentPublishedQuizList />} />
          <Route path="/student/quizzes/:quizId" element={<StudentQuizViewPage />} />
          <Route path="/student/quizzes/:quizId/results" element={<StudentQuizResultsPage />} />
          <Route path="/student/categories" element={<StudentCategoryListPage />} />
          <Route path="/student/categories/:id/quizzes" element={<StudentQuizzesByCategoryPage />} />
          
          {/* **** NEW REVIEW ROUTES FOR STUDENTS **** */}
          <Route path="/student/quizzes/:quizId/reviews" element={<QuizReviewsPage />} />
          {/* Adding/Editing will be handled within QuizReviewsPage by toggling ReviewForm visibility */}
          {/* If you opt for dedicated pages:
          <Route path="/student/quizzes/:quizId/reviews/new" element={<AddReviewPage />} />
          <Route path="/student/reviews/:reviewId/edit" element={<EditReviewPage />} /> 
          */}

          <Route path="*" element={
            <Container maxWidth="sm" sx={{ textAlign: 'center', py: 5 }}>
              <Paper elevation={3} sx={{ p: 4}}>
                <Typography variant="h3" component="h1" gutterBottom>404 - Page Not Found</Typography>
                <Alert severity="warning" sx={{ mb: 4 }}>Oops! The page you're looking for doesn't seem to exist.</Alert>
                <Box>
                  <Button variant="contained" component={RouterLink} to="/student/quizzes" sx={{ mr: 1 }}>
                    Go to Student Quizzes
                  </Button>
                  <Button variant="outlined" component={RouterLink} to="/quizzes">
                    Go to Teacher Dashboard
                  </Button>
                </Box>
              </Paper>
            </Container>
          } />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;