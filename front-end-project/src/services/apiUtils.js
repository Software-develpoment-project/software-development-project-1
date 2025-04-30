/**
 * Utility functions for API requests.
 * Follows Single Responsibility Principle by centralizing request/response handling logic.
 */

/**
 * Handles API errors consistently.
 * @param {Error} error - The error object caught from an API request
 * @param {string} defaultMessage - Default message to show when no meaningful error is available
 * @returns {string} A user-friendly error message
 */
export const handleApiError = (error, defaultMessage = 'An error occurred') => {
  console.error('API Error:', error);
  
  if (error.response) {
    // The request was made and the server responded with a status code
    // that falls out of the range of 2xx
    const { status, data } = error.response;
    
    console.error(`Status: ${status}`);
    console.error('Error data:', data);
    
    if (data && data.message) {
      return `Error: ${data.message}`;
    }
    
    if (data && data.error) {
      return `Error: ${data.error}`;
    }
    
    if (status === 400) {
      return 'Invalid input data';
    }
    
    if (status === 401) {
      return 'Authentication required';
    }
    
    if (status === 403) {
      return 'You do not have permission to perform this action';
    }
    
    if (status === 404) {
      return 'The requested resource was not found';
    }
    
    if (status === 500) {
      return 'Server error, please try again later';
    }
  } else if (error.request) {
    // The request was made but no response was received
    console.error('No response received from server');
    return 'No response from server. Please check your connection';
  }
  
  return defaultMessage;
};

/**
 * Creates a consistent request configuration object.
 * @param {Object} options - Additional request options
 * @returns {Object} Request configuration
 */
export const createRequestConfig = (options = {}) => {
  // Default options that can be overridden
  const defaultOptions = {
    headers: {
      'Content-Type': 'application/json',
    },
  };
  
  return {
    ...defaultOptions,
    ...options,
    headers: {
      ...defaultOptions.headers,
      ...options.headers,
    },
  };
};

/**
 * Process response data consistently.
 * @param {Object} response - The API response object
 * @returns {any} The processed response data
 */
export const processResponse = (response) => {
  console.log(`Response: ${response.status} from ${response.config.url}`);
  return response.data;
};

/**
 * Map frontend model fields to backend DTO fields
 * This ensures proper field mapping between frontend and backend
 * 
 * @param {Object} data - The data object to transform
 * @param {string} type - The entity type (e.g., 'quiz', 'question')
 * @returns {Object} - The mapped data object
 */
export const mapToBackendDTO = (data, type = 'generic') => {
  if (!data) return data;
  
  // Clone the data to avoid modifying the original object
  const mappedData = { ...data };
  
  // Specific mappings for each entity type
  switch (type.toLowerCase()) {
    case 'quiz':
      // Ensure field names match backend expectations
      if (mappedData.name !== undefined && mappedData.title === undefined) {
        mappedData.title = mappedData.name;
        delete mappedData.name;
      }
      break;
      
    case 'question':
      // Map content field to questionText for backend compatibility
      if (mappedData.content !== undefined) {
        mappedData.questionText = mappedData.content;
        delete mappedData.content;
      }
      break;
      
    case 'answer':
      // Add answer-specific mappings here if needed
      break;
      
    default:
      // No specific mappings for other types
      break;
  }
  
  return mappedData;
};

/**
 * Map backend DTO fields to frontend model fields
 * 
 * @param {Object} data - The data object from backend
 * @param {string} type - The entity type (e.g., 'quiz', 'question')
 * @returns {Object} - The mapped data object for frontend
 */
export const mapToFrontendModel = (data, type = 'generic') => {
  if (!data) return data;
  
  // Clone the data to avoid modifying the original object
  const mappedData = { ...data };
  
  // Apply specific mappings based on entity type
  switch (type.toLowerCase()) {
    case 'quiz':
      // No current need for backend-to-frontend mapping
      break;
      
    case 'question':
      // Ensure questionText is mapped to content for frontend use
      if (mappedData.questionText !== undefined && mappedData.content === undefined) {
        mappedData.content = mappedData.questionText;
      }
      
      // Debug the question data mapping
      console.log('Question data being mapped to frontend:', mappedData);
      break;
      
    default:
      // No specific mappings for other types
      break;
  }
  
  return mappedData;
}; 