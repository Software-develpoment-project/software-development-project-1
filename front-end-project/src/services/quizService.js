import api, { createResourceApi } from './api';
import { handleApiError, mapToBackendDTO, mapToFrontendModel } from './apiUtils';
import axios from 'axios';

const API_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

/**
 * Create a standardized API request handler with error handling
 * @param {Function} apiCall - The function that makes the actual API call
 * @param {string|Function} errorMessage - Default error message or function that returns error message
 * @returns {Function} - A function that executes the API call with error handling
 */
const createApiHandler = (apiCall, errorMessage) => {
  return async (...args) => {
    try {
      return await apiCall(...args);
    } catch (error) {
      const message = typeof errorMessage === 'function' 
        ? errorMessage(...args) 
        : errorMessage;
      throw new Error(handleApiError(error, message));
    }
  };
};

// Create resource-specific API clients
const quizApi = createResourceApi('quizzes');
const questionApi = createResourceApi('quizzes/questions');
const answerApi = createResourceApi('quizzes/answers');
const categoryApi = createResourceApi('categories');
const teacherapi = createResourceApi('teachers');


/**
 * Service for Quiz API operations.
 * Follows:
 * - Single Responsibility Principle: focuses only on quiz-related API calls.
 * - Open/Closed Principle: can be extended without modification.
 * - Interface Segregation Principle: separated into domain-specific interfaces.
 * - Dependency Inversion Principle: depends on abstractions (api module).
 */
const quizService = {
  // Quiz management operations
  getAllQuizzes: createApiHandler(
    () => quizApi.getAll().then(data => Array.isArray(data) ? data.map(quiz => mapToFrontendModel(quiz, 'quiz')) : []), 
    'Failed to fetch quizzes'
  ),

  getPublishedQuizzes: createApiHandler(
    () => quizApi.findBy({ published: true }).then(data => Array.isArray(data) ? data.map(quiz => mapToFrontendModel(quiz, 'quiz')) : []), 
    'Failed to fetch published quizzes'
  ),

  getQuizById: createApiHandler(
    (id) => quizApi.getById(id).then(quiz => mapToFrontendModel(quiz, 'quiz')), 
    (id) => `Failed to fetch quiz with ID ${id}`
  ),

  createQuiz: createApiHandler(
    (quizData) => {
      // Ensure data is properly mapped to backend expectations
      const mappedData = mapToBackendDTO(quizData, 'quiz');
      console.log('QuizData being sent to API (after mapping):', JSON.stringify(mappedData));
      return quizApi.create(mappedData).then(quiz => mapToFrontendModel(quiz, 'quiz'));
    }, 
    'Failed to create quiz'
  ),

  updateQuiz: createApiHandler(
    (id, quizData) => {
      // Ensure data is properly mapped to backend expectations
      const mappedData = mapToBackendDTO(quizData, 'quiz');
      return quizApi.update(id, mappedData).then(quiz => mapToFrontendModel(quiz, 'quiz'));
    }, 
    (id) => `Failed to update quiz with ID ${id}`
  ),

  deleteQuiz: createApiHandler(
    (id) => quizApi.delete(id), 
    (id) => `Failed to delete quiz with ID ${id}`
  ),

  publishQuiz: createApiHandler(
    (id) => quizApi.customAction(id, 'publish'), 
    (id) => `Failed to publish quiz with ID ${id}`
  ),

  unpublishQuiz: createApiHandler(
    (id) => quizApi.customAction(id, 'unpublish'), 
    (id) => `Failed to unpublish quiz with ID ${id}`
  ),

  // Function aliases for backward compatibility
  // These functions are called directly from AnswerOptionForm.jsx
  getQuestionAnswers: createApiHandler(
    (questionId) => api.get(`quizzes/questions/${questionId}/answers`), 
    (questionId) => `Failed to fetch answer options for question with ID ${questionId}`
  ),

  createAnswerOption: createApiHandler(
    (questionId, answerData) => {
      const mappedData = mapToBackendDTO(answerData, 'answer');
      return api.post(`quizzes/questions/${questionId}/answers`, mappedData);
    }, 
    (questionId) => `Failed to create answer option for question with ID ${questionId}`
  ),

  deleteAnswerOption: createApiHandler(
    (answerId) => answerApi.delete(answerId),
    (answerId) => `Failed to delete answer option with ID ${answerId}`
  ),

  // Question management operations
  questions: {
    getByQuizId: createApiHandler(
      (quizId) => api.get(`quizzes/${quizId}/questions`), 
      (quizId) => `Failed to fetch questions for quiz with ID ${quizId}`
    ),

    getById: createApiHandler(
      (questionId) => api.get(`quizzes/questions/${questionId}`)
        .then(question => {
          console.log(`Raw question data from API for ID ${questionId}:`, question);
          return mapToFrontendModel(question, 'question');
        }),
      (questionId) => `Failed to fetch question with ID ${questionId}`
    ),

    create: createApiHandler(
      (quizId, questionData) => {
        const mappedData = mapToBackendDTO(questionData, 'question');
        return api.post(`quizzes/${quizId}/questions`, mappedData);
      }, 
      (quizId) => `Failed to create question for quiz with ID ${quizId}`
    ),

    delete: createApiHandler(
      (questionId) => questionApi.delete(questionId), 
      (questionId) => `Failed to delete question with ID ${questionId}`
    )
  },

  // Answer option management operations
  answerOptions: {
    getByQuestionId: createApiHandler(
      (questionId) => api.get(`quizzes/questions/${questionId}/answers`), 
      (questionId) => `Failed to fetch answer options for question with ID ${questionId}`
    ),

    create: createApiHandler(
      (questionId, answerData) => {
        const mappedData = mapToBackendDTO(answerData, 'answer');
        return api.post(`quizzes/questions/${questionId}/answers`, mappedData);
      }, 
      (questionId) => `Failed to create answer option for question with ID ${questionId}`
    ),

    delete: createApiHandler(
      (answerId) => answerApi.delete(answerId), 
      (answerId) => `Failed to delete answer option with ID ${answerId}`
    )
  }
};

// For backward compatibility with existing code
export const fetchQuizzes = async () => {
  try {
    const response = await axios.get(`${API_URL}/quizzes`);
    return response.data;
  } catch (error) {
    console.error('Error fetching quizzes:', error);
    throw new Error('Failed to fetch quizzes');
  }
};

export const createQuiz = async (quizData) => {
  try {
    // Ensure data is properly mapped to backend expectations
    const mappedData = mapToBackendDTO(quizData, 'quiz');
    console.log('Standalone createQuiz - Data being sent to API:', JSON.stringify(mappedData));
    
    const response = await axios.post(`${API_URL}/quizzes`, mappedData);
    return response.data;
  } catch (error) {
    console.error('Error creating quiz:', error);
    throw new Error('Failed to create quiz');
  }
};

export const updateQuiz = async (id, quizData) => {
  try {
    // Ensure data is properly mapped to backend expectations
    const mappedData = mapToBackendDTO(quizData, 'quiz');
    console.log(`Standalone updateQuiz - Updating quiz ${id} with:`, JSON.stringify(mappedData));
    
    const response = await axios.put(`${API_URL}/quizzes/${id}`, mappedData);
    return response.data;
  } catch (error) {
    console.error(`Error updating quiz ${id}:`, error);
    throw new Error('Failed to update quiz');
  }
};

export const deleteQuiz = async (id) => {
  try {
    await axios.delete(`${API_URL}/quizzes/${id}`);
  } catch (error) {
    console.error(`Error deleting quiz ${id}:`, error);
    throw new Error('Failed to delete quiz');
  }
};

export const publishQuiz = async (id) => {
  try {
    await axios.post(`${API_URL}/quizzes/${id}/publish`);
  } catch (error) {
    console.error(`Error publishing quiz ${id}:`, error);
    throw new Error('Failed to publish quiz');
  }
};

export const unpublishQuiz = async (id) => {
  try {
    await axios.post(`${API_URL}/quizzes/${id}/unpublish`);
  } catch (error) {
    console.error(`Error unpublishing quiz ${id}:`, error);
    throw new Error('Failed to unpublish quiz');
  }
};
export const getAllCategories = async () => {
  try {
    const response = await axios.get(`${API_URL}/api/categories`);
    return response.data;
  } catch (error) {
    console.error('Error fetching categories:', error);
    throw new Error('Failed to fetch categories');
  }
}



export default quizService; 