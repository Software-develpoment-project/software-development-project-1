import api from './api';
import { handleApiError } from '../utils/errorHandler';

const BASE_URL = '/quiz-attempts';

/**
 * Start a new quiz attempt
 * @param {string|number} quizId - ID of the quiz to attempt
 * @returns {Promise} - Promise containing the API response
 */
export const startQuizAttempt = async (quizId) => {
  try {
    return await api.post(`${BASE_URL}/start`, { quizId });
  } catch (error) {
    return handleApiError(error);
  }
};

/**
 * Submit an answer for a quiz attempt
 * @param {string|number} attemptId - ID of the current attempt
 * @param {string|number} questionId - ID of the question being answered
 * @param {Array|string} answer - The user's answer(s)
 * @returns {Promise} - Promise containing the API response
 */
export const submitAnswer = async (attemptId, questionId, answer) => {
  try {
    return await api.post(`${BASE_URL}/${attemptId}/submit-answer`, {
      questionId,
      answer
    });
  } catch (error) {
    return handleApiError(error);
  }
};

/**
 * Complete a quiz attempt
 * @param {string|number} attemptId - ID of the attempt to complete
 * @returns {Promise} - Promise containing the API response with results
 */
export const completeQuizAttempt = async (attemptId) => {
  try {
    return await api.post(`${BASE_URL}/${attemptId}/complete`);
  } catch (error) {
    return handleApiError(error);
  }
};

/**
 * Get a specific quiz attempt by ID
 * @param {string|number} attemptId - ID of the attempt to retrieve
 * @returns {Promise} - Promise containing the API response
 */
export const getQuizAttempt = async (attemptId) => {
  try {
    return await api.get(`${BASE_URL}/${attemptId}`);
  } catch (error) {
    return handleApiError(error);
  }
};

/**
 * Get all attempts for a specific quiz
 * @param {string|number} quizId - ID of the quiz
 * @returns {Promise} - Promise containing the API response
 */
export const getAttemptsByQuiz = async (quizId) => {
  try {
    return await api.get(`${BASE_URL}/quiz/${quizId}`);
  } catch (error) {
    return handleApiError(error);
  }
};

/**
 * Get all attempts by current user
 * @returns {Promise} - Promise containing the API response
 */
export const getUserAttempts = async () => {
  try {
    return await api.get(`${BASE_URL}/user`);
  } catch (error) {
    return handleApiError(error);
  }
};

export default {
  startQuizAttempt,
  submitAnswer,
  completeQuizAttempt,
  getQuizAttempt,
  getAttemptsByQuiz,
  getUserAttempts
}; 