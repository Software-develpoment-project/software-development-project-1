import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
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
            <div className="p-4 text-center">
              <h1 className="text-2xl font-bold mb-4">Page Not Found</h1>
              <p className="mb-4">The page you're looking for doesn't exist.</p>
              <a 
                href="/quizzes" 
                className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
              >
                Back to Quizzes
              </a>
            </div>
          } />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
