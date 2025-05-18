import { get, post, put, del } from './api';

const QUIZZES_BASE_PATH = 'quizzes';
const REVIEWS_BASE_PATH = 'reviews';

const getCurrentStudentNickname = () => {
    return localStorage.getItem('studentNickname');
};

export const getReviewsForQuiz = (quizId) => {
    return get(`${QUIZZES_BASE_PATH}/${quizId}/reviews`);
};

export const createReview = (quizId, reviewData) => {
    const payload = { ...reviewData, quizId: Number(quizId) };
    return post(`${QUIZZES_BASE_PATH}/${quizId}/reviews`, payload, {}); 
};

export const getReviewById = (reviewId) => {
    return get(`${REVIEWS_BASE_PATH}/${reviewId}`);
};

export const updateReview = (reviewId, reviewData) => {
    const studentNickname = getCurrentStudentNickname();
    const customHeaders = {}; 
    if (studentNickname) {
        customHeaders['X-Student-Nickname'] = studentNickname;
    }

    const payload = {
        studentNickname: reviewData.studentNickname,
        rating: reviewData.rating,
        reviewText: reviewData.reviewText,
        quizId: reviewData.quizId
    };
    
    return put(`${REVIEWS_BASE_PATH}/${reviewId}`, payload, { headers: customHeaders });
};

export const deleteReview = (reviewId) => {
    const studentNickname = getCurrentStudentNickname();
    const customHeaders = {};
    if (studentNickname) {
        customHeaders['X-Student-Nickname'] = studentNickname;
    }
    
    return del(`${REVIEWS_BASE_PATH}/${reviewId}`, { headers: customHeaders });
};

const reviewService = {
    getReviewsForQuiz,
    createReview,
    getReviewById,
    updateReview,
    deleteReview,
    getCurrentStudentNickname
};

export default reviewService;