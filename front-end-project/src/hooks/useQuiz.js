import { useState, useEffect } from 'react';
import { getQuizById } from '../services/quizService';

/**
 * Custom hook for fetching a single quiz by ID
 * 
 * @param {string|number} quizId - The ID of the quiz to fetch
 * @returns {Object} - Object containing quiz data, loading state, and error state
 */
export const useQuiz = (quizId) => {
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchQuiz = async () => {
      // Don't fetch if no quizId is provided
      if (!quizId) {
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);
        
        const response = await getQuizById(quizId);
        setQuiz(response.data);
      } catch (error) {
        setError(error.message || 'Failed to fetch quiz');
      } finally {
        setLoading(false);
      }
    };

    fetchQuiz();
  }, [quizId]);

  return { quiz, loading, error };
};

export default useQuiz; 