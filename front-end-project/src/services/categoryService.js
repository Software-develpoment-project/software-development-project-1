import { get, post, put, del } from './api';
import { createApiHandler } from './apiUtils';

const API_BASE_PATH = 'categories';

export const getAllCategories = createApiHandler(
  () => get(API_BASE_PATH),
  'Failed to fetch categories'
);

export const getCategoryById = createApiHandler(
  (id) => get(`${API_BASE_PATH}/${id}`),
  'Failed to fetch category'
);

export const createCategory = createApiHandler(
  (categoryData) => post(API_BASE_PATH, categoryData),
  'Failed to create category'
);

export const updateCategory = createApiHandler(
  (id, categoryData) => put(`${API_BASE_PATH}/${id}`, categoryData),
  'Failed to update category'
);

export const deleteCategory = createApiHandler(
  (id) => del(`${API_BASE_PATH}/${id}`),
  (id) => `Failed to delete category with ID ${id}` 
);

export const getPublishedQuizzesByCategoryId = createApiHandler(
  (categoryId) => get(`${API_BASE_PATH}/${categoryId}/quizzes`),
  'Failed to fetch quizzes for category'
);

const categoryServiceObject = {
  getAllCategories,
  getCategoryById,
  createCategory,
  updateCategory,
  deleteCategory,
  getPublishedQuizzesByCategoryId,
};

export default categoryServiceObject;