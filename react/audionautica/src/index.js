import React, {useState} from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegisterPage from './Pages/register';
import LoginPage from './Pages/login';
import Menu from './Pages/menu';

export const Context = React.createContext();

const App = () => {
  const [isLoggedIn, setisLoggedIn] = useState(false);
  
  return (
    <Router> 
      <Context.Provider value={[isLoggedIn, setisLoggedIn]}>
        <Menu />
        <Routes> 
          <Route path="/register" Component={RegisterPage} />
          <Route path="/login" Component={LoginPage} />
        </Routes>
      </Context.Provider>
    </Router>
  );
}

createRoot(document.getElementById('root')).render(<App />);