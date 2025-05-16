/**
 * Utility functions for API requests.
 */
export const handleApiError = (error, defaultMessage = 'An error occurred') => {
  if (error.response) {
    const { status, data } = error.response;

    if (data && data.message) {
      return `Error: ${data.message}`; 
    }
    if (data && data.error) { 
      return `Error: ${data.error}`;
    }

    // Generic messages based on status
    if (status === 400) return 'Invalid input data. Please check your entries.';
    if (status === 401) return 'Authentication failed or is required.';
    if (status === 403) return 'You do not have permission to perform this action.';
    if (status === 404) return 'The requested resource could not be found.';
    if (status >= 500) return 'A server error occurred. Please try again later.'; 

  } else if (error.request) {
    return 'No response from the server. Please check your network connection.';
  }
 
  if (error.message) return error.message;

  return defaultMessage;
};


export const processResponse = (response) => {
  return response.data;
};

/**
 * Maps frontend model fields to backend DTO fields.
 */
export const mapToBackendDTO = (data, type = 'generic') => {
  if (!data) return data;
  const mappedData = { ...data };

  switch (type.toLowerCase()) {
    case 'quiz':
      if (mappedData.name !== undefined && mappedData.title === undefined) {
        mappedData.title = mappedData.name;
        delete mappedData.name;
      }
      break;
    case 'question':
      if (mappedData.content !== undefined && mappedData.questionText === undefined) {
        mappedData.questionText = mappedData.content;
        delete mappedData.content; 
      }
      break;
  }
  return mappedData;
};

/**
 * Maps backend DTO fields to frontend model fields.
 */
export const mapToFrontendModel = (data, type = 'generic') => {
  if (!data) return data;
  const mappedData = { ...data };

  switch (type.toLowerCase()) {
    case 'quiz':
      break;
    case 'question':
      if (mappedData.questionText !== undefined && mappedData.content === undefined) {
        mappedData.content = mappedData.questionText;
      }
      break;
  }
  return mappedData;
};


export const createApiHandler = (apiCall, errorMessageContext) => {
  return async (...args) => {
    try {
      return await apiCall(...args);
    } catch (error) {
      throw error;
    }
  };
};