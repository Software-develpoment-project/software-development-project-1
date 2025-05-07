import { get, post } from './api'; // Use named exports from fetch-based api.js
// Removed handleApiError import as error handling is centralized in fetchApi

const BASE_URL = '/quiz-attempts';

/**
 * Start a new quiz attempt
 * @param {string|number} quizId - ID of the quiz to attempt
 * @returns {Promise<any>} - Promise containing the API response data
 */
export const startQuizAttempt = (quizId) => {
  return post(`${BASE_URL}/start`, { quizId });
};

/**
 * Submit an answer for a quiz attempt
 * @param {string|number} attemptId - ID of the current attempt
 * @param {string|number} questionId - ID of the question being answered
 * @param {Array|string} answer - The user's answer(s)
 * @returns {Promise<any>} - Promise containing the API response data
 */
export const submitAnswer = (attemptId, questionId, answer) => {
  return post(`${BASE_URL}/${attemptId}/submit-answer`, {
    questionId,
    answer
  });
};

/**
 * Complete a quiz attempt
 * @param {string|number} attemptId - ID of the attempt to complete
 * @returns {Promise<any>} - Promise containing the API response with results data
 */
export const completeQuizAttempt = (attemptId) => {
  return post(`${BASE_URL}/${attemptId}/complete`);
};

/**
 * Get a specific quiz attempt by ID
 * @param {string|number} attemptId - ID of the attempt to retrieve
 * @returns {Promise<any>} - Promise containing the API response data
 */
export const getQuizAttempt = (attemptId) => {
  return get(`${BASE_URL}/${attemptId}`);
};

/**
 * Get all attempts for a specific quiz
 * @param {string|number} quizId - ID of the quiz
 * @returns {Promise<any>} - Promise containing the API response data
 */
export const getAttemptsByQuiz = (quizId) => {
  return get(`${BASE_URL}/quiz/${quizId}`);
};

/**
 * Get all attempts by current user
 * @returns {Promise<any>} - Promise containing the API response data
 */
export const getUserAttempts = () => {
  return get(`${BASE_URL}/user`);
};

// Export functions individually or as a default object
export default {
  startQuizAttempt,
  submitAnswer,
  completeQuizAttempt,
  getQuizAttempt,
  getAttemptsByQuiz,
  getUserAttempts
}; 