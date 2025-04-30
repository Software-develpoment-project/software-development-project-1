/**
 * Debug utility for testing direct API calls
 */
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const testDirectQuizCreation = async () => {
  try {
    console.log('Making direct API call to create quiz...');
    const testData = {
      title: 'Test Quiz Direct',
      description: 'Testing direct API call'
    };
    
    console.log('Request payload:', testData);
    
    const response = await axios.post(`${API_URL}/quizzes`, testData, {
      headers: {
        'Content-Type': 'application/json'
      }
    });
    
    console.log('Direct API call successful', response.data);
    return response.data;
  } catch (error) {
    console.error('Direct API call failed:', error.response?.data || error.message);
    console.log('Request that failed:', error.config?.data);
    throw error;
  }
};

export default {
  testDirectQuizCreation
}; 