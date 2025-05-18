import { handleApiError, processResponse } from './apiUtils';

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api').replace(/\/$/, "");

function joinUrlSegments(...segments) {
    return segments.map(segment => segment.replace(/^\/+|\/+$/g, ''))
                   .filter(segment => segment)
                   .join('/');
}

async function fetchApi(method, relativePath, data = null, options = {}) {
  const fullUrl = `${API_BASE_URL}/${joinUrlSegments(relativePath)}`;

  let requestBody = data;
  const finalHeaders = {
    'Accept': 'application/json', 

  };

 
  if (options.headers) {
    for (const key in options.headers) {
      if (options.headers.hasOwnProperty(key)) {
        finalHeaders[key] = options.headers[key];
      }
    }
  }


  if (data && (method === 'POST' || method === 'PUT' || method === 'PATCH')) {
    if (data instanceof FormData) {
      delete finalHeaders['Content-Type'];
      requestBody = data; // FormData is sent as-is
    } else if (typeof data === 'object') {

      finalHeaders['Content-Type'] = 'application/json';
      requestBody = JSON.stringify(data);
    } else {
      if (!finalHeaders['Content-Type']) {
        finalHeaders['Content-Type'] = 'text/plain;charset=UTF-8'; 
      }
      requestBody = data;
    }
  } else {

    delete finalHeaders['Content-Type'];
  }


  const config = {
    method,
    headers: finalHeaders,
  };

  if (requestBody !== null && method !== 'GET' && method !== 'HEAD') {
    
      config.body = requestBody;
  }


  try {
    const response = await fetch(fullUrl, config);

    if (!response.ok) {
      let errorData = null;
      let errorMessage = `Request failed for ${method} ${fullUrl} with status ${response.status}`;
      try {
        errorData = await response.json();
        errorMessage = handleApiError({ response: { data: errorData, status: response.status, statusText: response.statusText, url: fullUrl } }, errorMessage);
      } catch (e) {
        try {
            const errorText = await response.text();
            errorMessage = handleApiError({ response: { data: { message: errorText }, status: response.status, statusText: response.statusText, url: fullUrl } }, errorMessage);
        } catch (textError) {
            errorMessage = handleApiError({ response: { status: response.status, statusText: response.statusText, url: fullUrl } }, errorMessage);
        }
      }
      throw new Error(errorMessage);
    }

    if (response.status === 204) {
      return undefined;
    }

    const responseData = await response.json();
    return processResponse({ data: responseData });

  } catch (error) {
      if (!(error instanceof Error)) {
          throw new Error(handleApiError(error, `An unexpected network error occurred for ${fullUrl}.`));
      } else {
          throw error;
      }
  }
}

export const get = (url, options = {}) => fetchApi('GET', url, null, options);
export const post = (url, data, options = {}) => fetchApi('POST', url, data, options);
export const put = (url, data, options = {}) => fetchApi('PUT', url, data, options);
export const del = (url, data = null, options = {}) => fetchApi('DELETE', url, data, options);


export const createResourceApi = (resource) => {
  const resourcePath = resource.startsWith('/') ? resource.substring(1) : resource;
  return {
    getAll: (options) => get(resourcePath, options),
    getById: (id, options) => get(`${resourcePath}/${id}`, options),
    create: (data, options) => post(resourcePath, data, options),
    update: (id, data, options) => put(`${resourcePath}/${id}`, data, options),
    delete: (id, data = null, options) => del(`${resourcePath}/${id}`, data, options),
    customAction: (id, action, data = null, options = {}) =>
      data
        ? post(`${resourcePath}/${id}/${action}`, data, options)
        : post(`${resourcePath}/${id}/${action}`, null, options),
    findBy: (params, options = {}) => get(resourcePath, { ...options, params }),
  };
};