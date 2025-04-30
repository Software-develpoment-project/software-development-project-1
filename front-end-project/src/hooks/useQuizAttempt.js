import { useState, useEffect } from 'react';
import { 
  startQuizAttempt, 
  getQuizAttempt, 
  submitAnswer,
  completeQuizAttempt 
} from '../services/quizAttemptService';

/**
 * Custom hook for managing a quiz attempt
 * @param {string|number} quizId - Optional quiz ID to start an attempt with
 * @param {string|number} attemptId - Optional attempt ID to load an existing attempt
 * @returns {Object} - Object containing quiz attempt data and functions
 */
const useQuizAttempt = (quizId, attemptId) => {
  const [attempt, setAttempt] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentQuestion, setCurrentQuestion] = useState(null);

  // Start a new quiz attempt
  const startAttempt = async (newQuizId) => {
    const id = newQuizId || quizId;
    if (!id) {
      setError('Quiz ID is required to start an attempt');
      return null;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await startQuizAttempt(id);
      if (response.success) {
        setAttempt(response.data);
        if (response.data.questions && response.data.questions.length > 0) {
          setCurrentQuestion(response.data.questions[0]);
        }
        return response.data;
      } else {
        setError(response.message || 'Failed to start quiz attempt');
        return null;
      }
    } catch (err) {
      console.error('Error starting quiz attempt:', err);
      setError('Failed to start quiz attempt');
      return null;
    } finally {
      setLoading(false);
    }
  };

  // Load an existing quiz attempt
  const loadAttempt = async (id) => {
    const loadId = id || attemptId;
    if (!loadId) {
      setError('Attempt ID is required to load an attempt');
      return null;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await getQuizAttempt(loadId);
      if (response.success) {
        setAttempt(response.data);
        // Find current/next unanswered question
        if (response.data.questions) {
          const nextQuestion = response.data.questions.find(q => !q.answered);
          setCurrentQuestion(nextQuestion || response.data.questions[0]);
        }
        return response.data;
      } else {
        setError(response.message || 'Failed to load quiz attempt');
        return null;
      }
    } catch (err) {
      console.error('Error loading quiz attempt:', err);
      setError('Failed to load quiz attempt');
      return null;
    } finally {
      setLoading(false);
    }
  };

  // Submit an answer for the current question
  const submitQuestionAnswer = async (questionId, answer) => {
    if (!attempt?.id) {
      setError('No active quiz attempt');
      return null;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await submitAnswer(attempt.id, questionId, answer);
      if (response.success) {
        // Update the attempt with new data
        setAttempt(response.data);
        
        // Find the next unanswered question
        if (response.data.questions) {
          const nextQuestion = response.data.questions.find(q => !q.answered);
          setCurrentQuestion(nextQuestion || null);
        }
        
        return response.data;
      } else {
        setError(response.message || 'Failed to submit answer');
        return null;
      }
    } catch (err) {
      console.error('Error submitting answer:', err);
      setError('Failed to submit answer');
      return null;
    } finally {
      setLoading(false);
    }
  };

  // Complete the quiz attempt
  const completeAttempt = async () => {
    if (!attempt?.id) {
      setError('No active quiz attempt');
      return null;
    }

    setLoading(true);
    setError(null);

    try {
      const response = await completeQuizAttempt(attempt.id);
      if (response.success) {
        setAttempt(response.data);
        setCurrentQuestion(null);
        return response.data;
      } else {
        setError(response.message || 'Failed to complete quiz attempt');
        return null;
      }
    } catch (err) {
      console.error('Error completing quiz attempt:', err);
      setError('Failed to complete quiz attempt');
      return null;
    } finally {
      setLoading(false);
    }
  };

  // Load attempt if attemptId is provided on initial render
  useEffect(() => {
    if (attemptId) {
      loadAttempt(attemptId);
    }
  }, [attemptId]);

  return {
    attempt,
    loading,
    error,
    currentQuestion,
    startAttempt,
    loadAttempt,
    submitAnswer: submitQuestionAnswer,
    completeAttempt,
    // Helper functions
    isComplete: attempt?.status === 'COMPLETED',
    hasNextQuestion: !!currentQuestion,
    progress: attempt ? {
      total: attempt.questions?.length || 0,
      answered: attempt.questions?.filter(q => q.answered)?.length || 0,
      percentage: attempt.questions?.length 
        ? Math.round((attempt.questions.filter(q => q.answered).length / attempt.questions.length) * 100) 
        : 0
    } : null
  };
};

export default useQuizAttempt;