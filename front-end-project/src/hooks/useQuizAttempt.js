import { useState, useEffect } from 'react';
import {
  startQuizAttempt,
  submitStudentAnswer as submitAnswerService, 
} from '../services/quizAttemptService';

const useQuizAttempt = (quizId, attemptIdProp) => {
  const [attempt, setAttempt] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [currentQuestion, setCurrentQuestion] = useState(null);

  const findNextQuestion = (attemptData) => {
    if (attemptData?.questions) {
      const nextUnanswered = attemptData.questions.find(q => !q.answered);
      setCurrentQuestion(nextUnanswered || null);
    } else {
      setCurrentQuestion(null);
    }
  };

  const startAttempt = async (currentQuizId) => {
    const idToUse = currentQuizId || quizId;
    if (!idToUse) {
      setError('Quiz ID is required to start an attempt');
      return null;
    }
    setLoading(true);
    setError(null);
    try {
      const attemptData = await startQuizAttempt(idToUse);
      setAttempt(attemptData);
      findNextQuestion(attemptData);
      return attemptData;
    } catch (err) {
      setError(err.message || 'Failed to start quiz attempt');
      return null;
    } finally {
      setLoading(false);
    }
  };

  const submitAnswer = async (questionId, answerPayload) => {
    if (!attempt?.id) {
      setError('No active quiz attempt');
      return null;
    }
    setLoading(true);
    setError(null);
    try {
      const updatedAttemptData = await submitAnswerService(attempt.id, questionId, answerPayload);
      setAttempt(updatedAttemptData);
      findNextQuestion(updatedAttemptData);
      return updatedAttemptData;
    } catch (err) {
      setError(err.message || 'Failed to submit answer');
      return null;
    } finally {
      setLoading(false);
    }
  };



  useEffect(() => {
    if (attemptIdProp) {
    }
  }, [attemptIdProp]);

  return {
    attempt,
    loading,
    error,
    currentQuestion,
    startAttempt,
    submitAnswer,
    isComplete: attempt?.status === 'COMPLETED',
    hasNextQuestion: !!currentQuestion,
    progress: attempt?.questions?.length
      ? {
          total: attempt.questions.length,
          answered: attempt.questions.filter(q => q.answered).length,
          percentage: Math.round((attempt.questions.filter(q => q.answered).length / attempt.questions.length) * 100)
        }
      : null
  };
};

export default useQuizAttempt;