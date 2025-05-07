import { BrowserRouter as Router, Routes, Route, Navigate, Link as RouterLink } from 'react-router-dom';
import Layout from './components/Layout';
import QuizList from './pages/QuizList';
import QuizDetail from './pages/QuizDetail';
import QuizCreate from './pages/QuizCreate';
import QuizEdit from './pages/QuizEdit';
import QuestionCreate from './pages/QuestionCreate';
import AnswerOptionManage from './pages/AnswerOptionManage';
import PublishedQuizList from './pages/PublishedQuizList';
import AllQuizList from './pages/AllQuizList';
import {
  Container,
  Button,
  Alert,
  Typography,
  Box
} from '@mui/material';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          {/* Redirect root to quizzes */}
          <Route path="/" element={<Navigate to="/quizzes" replace />} />
          
          {/* Quiz routes */}
          <Route path="/quizzes" element={<QuizList />} />
          <Route path="/quizzes/new" element={<QuizCreate />} />
          <Route path="/quizzes/:id" element={<QuizDetail />} />
          <Route path="/quizzes/:id/edit" element={<QuizEdit />} />
          
          {/* Question routes */}
          <Route path="/quizzes/:quizId/questions/new" element={<QuestionCreate />} />
          
          {/* Answer Option routes */}
          <Route path="/questions/:questionId/answers" element={<AnswerOptionManage />} />
          
          {/* Published quizzes */}
          <Route path="/published-quizzes" element={<PublishedQuizList />} />
          
          {/* All quizzes */}
          <Route path="/all-quizzes" element={<AllQuizList />} />
          
          {/* Catch all for invalid routes */}
          <Route path="*" element={
            <Container maxWidth="sm" sx={{ textAlign: 'center', py: 5 }}>
              <Typography variant="h3" component="h1" gutterBottom>
                Page Not Found
              </Typography>
              <Alert severity="warning" sx={{ mb: 4 }}>
                The page you're looking for doesn't exist.
              </Alert>
              <Button 
                variant="contained"
                component={RouterLink}
                to="/quizzes"
              >
                Back to Quizzes
              </Button>
            </Container>
          } />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
