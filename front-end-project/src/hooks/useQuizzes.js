import { useState, useEffect, useCallback } from 'react'; 
import quizService from '../services/quizService';

export const useQuizzes = ({ onlyPublished = false, dependencies = [] } = {}) => {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchQuizzes = useCallback(async () => {
    try {
      setLoading(true);
      setError(null);
      const data = onlyPublished
        ? await quizService.getPublishedQuizzes()
        : await quizService.getAllQuizzes();
      setQuizzes(Array.isArray(data) ? data : []); 
    } catch (err) {
      setError(err.message || 'Failed to fetch quizzes');
      setQuizzes([]); 
    } finally {
      setLoading(false);
    }
  }, [onlyPublished]); 

  useEffect(() => {
    fetchQuizzes();
  }, [fetchQuizzes, ...dependencies]);

  return {
    quizzes,
    loading,
    error,
    refetch: fetchQuizzes
  };
};

export default useQuizzes;