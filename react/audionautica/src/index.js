import React from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegisterPage from './Pages/register';
import LoginPage from './Pages/login';

import Menu from './Pages/menu';

const App = () => {
  return (
    <Router> 
      <Menu />
      <Routes> 
        <Route path="/register" Component={RegisterPage} />
        <Route path="/login" Component={LoginPage} />
      </Routes>
    </Router>
  );
}

createRoot(document.getElementById('root')).render(<App />);