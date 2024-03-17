import React, {useState} from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegisterPage from './Pages/register';
import LoginPage from './Pages/login';
import Menu from './Pages/menu';
import HomePage from './Pages/home';
import Recomendations from './components/recomendations';
import SearchBar from './components/searchbar';


export const Context = React.createContext();

const App = () => {
  const [isLoggedIn, setisLoggedIn] = useState(false);
  
  return (
    <Router> 
      <Context.Provider value={[isLoggedIn, setisLoggedIn]}>
        <Menu />
        <Routes> 
          <Route path="/register" element={<RegisterPage/>} />
          <Route path="/login" element={<LoginPage/>} />
          <Route path="/home/" element={<HomePage/>} >
            <Route path="rec/" element={<Recomendations/>} />
            <Route path="search/" element={<SearchBar/>} />
          </Route>
          
        </Routes>
      </Context.Provider>
    </Router>
  );
}

createRoot(document.getElementById('root')).render(<App />);