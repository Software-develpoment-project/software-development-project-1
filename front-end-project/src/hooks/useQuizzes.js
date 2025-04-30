import { useState, useEffect } from 'react';
import quizService from '../services/quizService';

/**
 * Custom hook for fetching quizzes
 * 
 * @param {Object} options - Hook configuration options
 * @param {boolean} options.onlyPublished - Whether to fetch only published quizzes
 * @param {Array<any>} options.dependencies - Additional dependencies for useEffect
 * @returns {Object} - Object containing quizzes data, loading state, error state, and refetch function
 */
export const useQuizzes = ({ onlyPublished = false, dependencies = [] } = {}) => {
  const [quizzes, setQuizzes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchQuizzes = async () => {
    try {
      setLoading(true);
      setError(null);
      
      // Use appropriate service method based on the onlyPublished flag
      const data = onlyPublished 
        ? await quizService.getPublishedQuizzes()
        : await quizService.getAllQuizzes();
      
      setQuizzes(data);
    } catch (error) {
      setError(error.message || 'Failed to fetch quizzes');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchQuizzes();
  }, [onlyPublished, ...dependencies]);

  return { 
    quizzes, 
    loading, 
    error,
    refetch: fetchQuizzes
  };
};

export default useQuizzes; 