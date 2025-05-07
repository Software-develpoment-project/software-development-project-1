import { handleApiError, processResponse } from './apiUtils';

// Define the base URL for the API
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';

/**
 * Core fetch function to handle API requests.
 * @param {string} method - HTTP method (GET, POST, PUT, DELETE, etc.)
 * @param {string} url - Relative URL path
 * @param {object|null} data - Request body data (for POST, PUT, etc.)
 * @param {object} options - Additional fetch options (headers, etc.)
 * @returns {Promise<any>} - Promise resolving with parsed response data or rejecting with an error
 */
async function fetchApi(method, url, data = null, options = {}) {
  const fullUrl = `${API_BASE_URL}/${url.startsWith('/') ? url.substring(1) : url}`;
  
  const headers = {
    // Default headers
    ...options.headers,
  };

  const config = {
    method,
    headers,
    ...options, // Spread other fetch options like mode, cache, credentials
  };

  // Set Content-Type and stringify body for relevant methods
  if (data) {
    if (!(data instanceof FormData) && typeof data === 'object') {
      // Set JSON content type only if not already set and not FormData
      if (!headers['Content-Type']) {
        headers['Content-Type'] = 'application/json';
      }
      // Only stringify if it's JSON
      if (headers['Content-Type'] === 'application/json') {
         config.body = JSON.stringify(data);
      }
    } else {
        // Handle FormData or other body types directly
        config.body = data;
    }
  }

  try {
    const response = await fetch(fullUrl, config);

    // Check if the response is successful (status 200-299)
    if (!response.ok) {
      let errorData = null;
      let errorMessage = `Request failed with status ${response.status}`;
      try {
        // Attempt to parse error response body
        errorData = await response.json();
        // Use handleApiError from apiUtils to format the error
        errorMessage = handleApiError({ response: { data: errorData, status: response.status } });
      } catch (e) {
        // Failed to parse error body or handleApiError failed, use default message
        console.error('Could not parse error response:', e);
        // Use handleApiError with just status if body parsing fails
        errorMessage = handleApiError({ response: { status: response.status } });
      }
      throw new Error(errorMessage);
    }

    // Handle No Content response
    if (response.status === 204) {
      return undefined; // Or null, depending on desired behavior
    }

    // Parse JSON response for successful requests with content
    const responseData = await response.json();
    // Process response if needed (e.g., unwrapping data key)
    return processResponse({ data: responseData }); // Pass data in expected structure to processResponse

  } catch (error) {
      // Handle network errors or errors thrown above
      console.error('API call failed:', error);
      // If it's not already an Error object with a formatted message, format it.
      if (!(error instanceof Error)) { 
          throw new Error(handleApiError(error, 'An unexpected network error occurred.'));
      } else {
          // Rethrow the already formatted error
          throw error;
      }
  }
}

// Export convenience methods for common HTTP verbs
export const get = (url, options) => fetchApi('GET', url, null, options);
export const post = (url, data, options) => fetchApi('POST', url, data, options);
export const put = (url, data, options) => fetchApi('PUT', url, data, options);
export const del = (url, options) => fetchApi('DELETE', url, null, options); // Renamed to 'del' to avoid reserved keyword

/**
 * Creates standardized request methods for a specific resource path.
 * @param {string} resource - The base resource path (e.g., 'quizzes')
 * @returns {Object} - Object with CRUD methods for the resource
 */
export const createResourceApi = (resource) => {
  const resourcePath = resource.startsWith('/') ? resource.substring(1) : resource;
  
  return {
    getAll: (options) => get(resourcePath, options),
    getById: (id, options) => get(`${resourcePath}/${id}`, options),
    create: (data, options) => post(resourcePath, data, options),
    update: (id, data, options) => put(`${resourcePath}/${id}`, data, options),
    delete: (id, options) => del(`${resourcePath}/${id}`, options), // Use 'del' here
    
    // Custom actions beyond CRUD (example)
    customAction: (id, action, data = null, options = {}) => 
      data 
        ? post(`${resourcePath}/${id}/${action}`, data, options) 
        : post(`${resourcePath}/${id}/${action}`, null, options),
      
    // Custom queries (example)
    findBy: (params, options = {}) => get(resourcePath, { ...options, params }), // Note: fetch doesn't directly support 'params' like axios, needs url construction or passed in options
  };
}; 