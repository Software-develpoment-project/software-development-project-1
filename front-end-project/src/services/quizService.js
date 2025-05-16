import { get, post, put, del, createResourceApi } from './api';
import { mapToBackendDTO, mapToFrontendModel } from './apiUtils';

const createApiHandler = (apiCall, errorMessageContext) => {
  return async (...args) => {
    try {
      return await apiCall(...args);
    } catch (error) {
      throw error;
    }
  };
};

const quizApi = createResourceApi('quizzes');

const quizService = {
  getAllQuizzes: createApiHandler(
    () => quizApi.getAll().then(data => Array.isArray(data) ? data.map(quiz => mapToFrontendModel(quiz, 'quiz')) : []),
    'Failed to fetch quizzes'
  ),

  getPublishedQuizzes: createApiHandler(
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


  questions: {
    getByQuizId: createApiHandler(
      (quizId) => get(`quizzes/${quizId}/questions`).then(data => Array.isArray(data) ? data.map(q => mapToFrontendModel(q, 'question')) : []),
      (quizId) => `Failed to fetch questions for quiz ID ${quizId}`
    ),
    getById: createApiHandler(
      (questionId) => get(`quizzes/questions/${questionId}`).then(question => mapToFrontendModel(question, 'question')),
      (questionId) => `Failed to fetch question ID ${questionId}`
    ),
    create: createApiHandler(
      (quizId, questionData) => {
        const mappedData = mapToBackendDTO(questionData, 'question');
        return post(`quizzes/${quizId}/questions`, mappedData).then(q => mapToFrontendModel(q, 'question'));
      },
      (quizId) => `Failed to create question for quiz ID ${quizId}`
    ),
    delete: createApiHandler(
      (questionId) => del(`quizzes/questions/${questionId}`),
      (questionId) => `Failed to delete question ID ${questionId}`
    )
  },

  answerOptions: {
    getByQuestionId: createApiHandler(
      (questionId) => get(`quizzes/questions/${questionId}/answers`),
      (questionId) => `Failed to fetch answers for question ID ${questionId}`
    ),
    create: createApiHandler(
      (questionId, answerData) => {
        const mappedData = mapToBackendDTO(answerData, 'answer');
        return post(`quizzes/questions/${questionId}/answers`, mappedData);
      },
      (questionId) => `Failed to create answer for question ID ${questionId}`
    ),
    delete: createApiHandler(
      (answerId) => del(`quizzes/answers/${answerId}`),
      (answerId) => `Failed to delete answer ID ${answerId}`
    )
  }
};

export default quizService;