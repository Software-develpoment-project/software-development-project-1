import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { Container, Button, Alert } from 'react-bootstrap';
import Layout from './components/Layout';
import QuizList from './pages/QuizList';
import QuizDetail from './pages/QuizDetail';
import QuizCreate from './pages/QuizCreate';
import QuizEdit from './pages/QuizEdit';
import QuestionCreate from './pages/QuestionCreate';
import AnswerOptionManage from './pages/AnswerOptionManage';
import PublishedQuizList from './pages/PublishedQuizList';
import AllQuizList from './pages/AllQuizList';

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
            <Container className="text-center py-5">
              <h1 className="mb-4">Page Not Found</h1>
              <Alert variant="warning" className="mb-4">
                The page you're looking for doesn't exist.
              </Alert>
              <Button 
                variant="primary"
                href="/quizzes"
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
