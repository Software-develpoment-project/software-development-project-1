import { get, post } from './api';

const SERVICE_RELATIVE_PATH = 'quiz-attempts';

export const startQuizAttempt = (quizId, studentId = null) => {
  return post(`${SERVICE_RELATIVE_PATH}/start`, { quizId, studentId });
};

export const submitStudentAnswer = (attemptId, questionId, submissionDTO) => {
  return post(`${SERVICE_RELATIVE_PATH}/${attemptId}/questions/${questionId}/submit-answer`, submissionDTO);
};


export const getQuizAttemptDetails = (attemptId) => {
  return get(`${SERVICE_RELATIVE_PATH}/${attemptId}`);
};

export const getQuizResults = (quizId) => {
  return get(`${SERVICE_RELATIVE_PATH}/quizzes/${quizId}/results`);
};