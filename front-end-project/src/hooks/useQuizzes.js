import { useState, useEffect } from 'react';
import quizService from '../services/quizService';

/**
 * Custom hook for fetching quizzes
 * Follows:
 * - Single Responsibility Principle by focusing only on quiz data fetching
 * - Interface Segregation Principle by clearly defining inputs and outputs
 * - Dependency Inversion Principle by depending on service abstractions
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
      console.error('Error in useQuizzes hook:', error);
      setError(error.message || 'Failed to fetch quizzes');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchQuizzes();
  }, [onlyPublished, ...dependencies]);

  // Return quizzes data, loading state, error state, and refetch function
  return { 
    quizzes, 
    loading, 
    error,
    refetch: fetchQuizzes
  };
};

export default useQuizzes; 