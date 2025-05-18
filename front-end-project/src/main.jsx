import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';

// A basic MUI theme
const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2', // Example primary color (MUI default blue)
    },
    secondary: {
      main: '#dc004e', // Example secondary color (MUI default pink)
    },
  },
});

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <App />
    </ThemeProvider>
  </React.StrictMode>
);
