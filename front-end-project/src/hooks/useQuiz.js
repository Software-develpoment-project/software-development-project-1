import { useState, useEffect } from 'react';
import { getQuizById } from '../services/quizService'; 

export const useQuiz = (quizId) => {
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuiz = async () => {
      if (!quizId) {
        setQuiz(null); // Explicitly set to null if no quizId
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);
        const quizData = await getQuizById(quizId);
        setQuiz(quizData);
      } catch (err) {
        setError(err.message || 'Failed to fetch quiz');
        setQuiz(null); 
      } finally {
        setLoading(false);
      }
    };

    fetchQuiz();
  }, [quizId]);

  return { quiz, loading, error };
};

export default useQuiz;