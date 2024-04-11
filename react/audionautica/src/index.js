import React, {useState} from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegisterPage from './pages/register';
import LoginPage from './pages/login';
import Menu from './pages/menu';
import HomePage from './pages/home';
import Recomendations from './components/recomendations';
import SearchBar from './components/search/searchbar';
import Playlist from './components/playlist';
import AlbumCover from './components/icons/nopic.png';



export const Context = React.createContext();

const App = () => { 
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [currentTrack, setCurrentTrack] = useState(null)
  const [currTrackTitle, setCurrTrackTitle] = useState("")
  const [currTrackArtist, setCurrTrackArtist] = useState("")

  const contextValue = {isLoggedIn, setIsLoggedIn, 
                        currentTrack, setCurrentTrack,
                        currTrackTitle, setCurrTrackTitle,
                        currTrackArtist, setCurrTrackArtist}
  
  return (
    <Router> 
      <Context.Provider value={contextValue}>
        <Menu />
        <Routes> 
          <Route path="/register" element={<RegisterPage/>} />
          <Route path="/login" element={<LoginPage/>} />
          <Route path="/home/" element={<HomePage/>} >
            <Route path="rec/" element={<Recomendations/>} />
            <Route path="search/" element={<SearchBar/>} />
            <Route path="test/" element={<Playlist title={"Title"} creator={"Creator"} cover={AlbumCover}/>} />
          </Route>
          
        </Routes>
      </Context.Provider>
    </Router>
  );
}

createRoot(document.getElementById('root')).render(<App />);