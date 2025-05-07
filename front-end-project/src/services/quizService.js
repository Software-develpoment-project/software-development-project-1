import { get, post, put, del, createResourceApi } from './api';
import { handleApiError, mapToBackendDTO, mapToFrontendModel } from './apiUtils';

/**
 * Simplified API request handler wrapper.
 * Assumes fetchApi in api.js handles primary error catching and formatting.
 * This handler now focuses on executing the API call and mapping results.
 * @param {Function} apiCall - The function that makes the actual API call (e.g., quizApi.getAll)
 * @param {string|Function} [errorMessage] - Optional: Specific error message context (less critical now)
 * @returns {Function} - A function that executes the API call
 */
const createApiHandler = (apiCall, errorMessage) => {
  return async (...args) => {
    try {
      // Directly await the result. fetchApi will throw formatted errors.
      return await apiCall(...args);
    } catch (error) {
      // Re-throw the error received from fetchApi (already formatted)
      // Optionally add more context here if needed, but avoid duplication
      console.error(`API Handler Error (${typeof errorMessage === 'function' ? errorMessage(...args) : errorMessage}):`, error);
      throw error; // Propagate the error
    }
  };
};

// Create resource-specific API clients using the fetch-based createResourceApi
const quizApi = createResourceApi('quizzes');
// Note: Paths for nested resources might need careful handling if createResourceApi assumes simple base path.
// Direct calls using get/post might be clearer for nested resources.
// const questionApi = createResourceApi('quizzes/questions'); // May generate incorrect paths like /quizzes/questions/:id/delete
// const answerApi = createResourceApi('quizzes/answers'); // May generate incorrect paths like /quizzes/answers/:id/delete

/**
 * Service for Quiz API operations.
 */
const quizService = {
  // Quiz management operations
  getAllQuizzes: createApiHandler(
    // Use the fetch-based quizApi
    () => quizApi.getAll().then(data => Array.isArray(data) ? data.map(quiz => mapToFrontendModel(quiz, 'quiz')) : []), 
    'Failed to fetch quizzes'
  ),

  getPublishedQuizzes: createApiHandler(
    // Use the fetch-based get method directly to the correct published endpoint
    () => get('published-quizzes').then(data => Array.isArray(data) ? data.map(quiz => mapToFrontendModel(quiz, 'quiz')) : []), 
    'Failed to fetch published quizzes'
  ),

  getQuizById: createApiHandler(
    (id) => quizApi.getById(id).then(quiz => mapToFrontendModel(quiz, 'quiz')), 
    (id) => `Failed to fetch quiz with ID ${id}`
  ),

  createQuiz: createApiHandler(
    (quizData) => {
      const mappedData = mapToBackendDTO(quizData, 'quiz');
      return quizApi.create(mappedData).then(quiz => mapToFrontendModel(quiz, 'quiz'));
    }, 
    'Failed to create quiz'
  ),

  updateQuiz: createApiHandler(
    (id, quizData) => {
      const mappedData = mapToBackendDTO(quizData, 'quiz');
      return quizApi.update(id, mappedData).then(quiz => mapToFrontendModel(quiz, 'quiz'));
    }, 
    (id) => `Failed to update quiz with ID ${id}`
  ),

  deleteQuiz: createApiHandler(
    (id) => quizApi.delete(id), 
    (id) => `Failed to delete quiz with ID ${id}`
  ),

  // Publish/Unpublish stubs remain similar, using fetch-based customAction if defined in createResourceApi
  // OR direct put/post calls to the specific backend endpoints when implemented.
  publishQuiz: createApiHandler(
    // Example: Assuming a PUT request to /api/quizzes/{id}/publish sets published=true
    // async (id) => await put(`quizzes/${id}/publish`), // Requires backend endpoint
    // Stub implementation:
    async (id) => { console.warn(`[frontend stub] publishQuiz called for ID ${id}`); return { id, published: true }; },
    (id) => `Failed to publish quiz with ID ${id}`
  ),

  unpublishQuiz: createApiHandler(
    // Example: Assuming a PUT request to /api/quizzes/{id}/unpublish sets published=false
    // async (id) => await put(`quizzes/${id}/unpublish`), // Requires backend endpoint
    // Stub implementation:
     async (id) => { console.warn(`[frontend stub] unpublishQuiz called for ID ${id}`); return { id, published: false }; },
    (id) => `Failed to unpublish quiz with ID ${id}`
  ),

  // --- Nested Resource Operations --- 
  // Use direct get, post, del calls for clarity with nested paths

  // Question operations related to a specific quiz
  questions: {
    getByQuizId: createApiHandler(
      (quizId) => get(`quizzes/${quizId}/questions`), 
      (quizId) => `Failed to fetch questions for quiz with ID ${quizId}`
    ),

    getById: createApiHandler(
      // Corrected path: Added /quizzes/ prefix
      (questionId) => get(`quizzes/questions/${questionId}`)
        .then(question => mapToFrontendModel(question, 'question')),
      (questionId) => `Failed to fetch question with ID ${questionId}`
    ),

    create: createApiHandler(
      (quizId, questionData) => {
        const mappedData = mapToBackendDTO(questionData, 'question');
        // POST to the collection URI associated with the quiz
        return post(`quizzes/${quizId}/questions`, mappedData);
      }, 
      (quizId) => `Failed to create question for quiz with ID ${quizId}`
    ),

    delete: createApiHandler(
      // Corrected path: Added /quizzes/ prefix
      (questionId) => del(`quizzes/questions/${questionId}`), 
      (questionId) => `Failed to delete question with ID ${questionId}`
    )
  },

  // Answer option operations related to a specific question
  answerOptions: {
    getByQuestionId: createApiHandler(
      // Corrected path: Added /quizzes/ prefix
      (questionId) => get(`quizzes/questions/${questionId}/answers`), 
      (questionId) => `Failed to fetch answer options for question with ID ${questionId}`
    ),

    create: createApiHandler(
      (questionId, answerData) => {
        const mappedData = mapToBackendDTO(answerData, 'answer');
        // Corrected path: Added /quizzes/ prefix
        return post(`quizzes/questions/${questionId}/answers`, mappedData);
      }, 
      (questionId) => `Failed to create answer option for question with ID ${questionId}`
    ),

    delete: createApiHandler(
       // Corrected path: Added /quizzes/ prefix
      (answerId) => del(`quizzes/answers/${answerId}`), 
      (answerId) => `Failed to delete answer option with ID ${answerId}`
    )
  }
};

export default quizService; 