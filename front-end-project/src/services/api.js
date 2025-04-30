import axios from 'axios';
import { handleApiError, processResponse } from './apiUtils';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

/**
 * Create and configure an axios instance with default settings
 * @param {Object} options - Configuration options
 * @returns {AxiosInstance} - Configured axios instance
 */
const createApiClient = (options = {}) => {
  const client = axios.create({
    baseURL: API_BASE_URL,
    headers: {
      'Content-Type': 'application/json',
      ...options.headers
    },
    ...options
  });

  // Request interceptor
  client.interceptors.request.use(
    (config) => {
      console.log(`Request: ${config.method.toUpperCase()} ${config.url}`);
      
      // Inspect data payload for POST and PUT requests
      if ((config.method === 'post' || config.method === 'put') && config.data) {
        // Log the raw data object
        console.log('Request payload (object):', config.data);
        
        // Log the stringified data to see exactly what's being sent
        console.log('Request payload (stringified):', JSON.stringify(config.data));
        
        // Check if title field exists specifically
        if (typeof config.data === 'object') {
          console.log('Title field present:', 'title' in config.data);
          console.log('Title value:', config.data.title);
          
          // Make sure the content type is set correctly
          if (!config.headers['Content-Type']) {
            config.headers['Content-Type'] = 'application/json';
            console.log('Setting Content-Type header to application/json');
          }
        }
      }
      
      return config;
    },
    (error) => {
      console.error('Request error:', error);
      return Promise.reject(error);
    }
  );

  // Response interceptor
  client.interceptors.response.use(
    (response) => {
      return processResponse(response);
    },
    (error) => {
      handleApiError(error);
      return Promise.reject(error);
    }
  );

  return client;
};

/**
 * Main API client for general use
 * Follows Single Responsibility Principle by centralizing API configuration.
 */
const api = createApiClient();

/**
 * Create request methods for a specific resource
 * @param {string} resource - The resource path
 * @returns {Object} - Object with CRUD methods for the resource
 */
export const createResourceApi = (resource) => {
  // Remove any leading slashes to avoid path duplication issues
  const resourcePath = resource.startsWith('/') ? resource.substring(1) : resource;
  
  return {
    getAll: () => api.get(resourcePath),
    getById: (id) => api.get(`${resourcePath}/${id}`),
    create: (data) => {
      console.log(`API Request to ${resourcePath}:`, data);
      return api.post(resourcePath, data);
    },
    update: (id, data) => api.put(`${resourcePath}/${id}`, data),
    delete: (id) => api.delete(`${resourcePath}/${id}`),
    
    // Custom actions beyond CRUD
    customAction: (id, action, data = null) => 
      data ? api.post(`${resourcePath}/${id}/${action}`, data) : api.post(`${resourcePath}/${id}/${action}`),
      
    // Custom queries
    findBy: (params) => api.get(resourcePath, { params })
  };
};

export default api; 