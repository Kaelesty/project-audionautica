import React, {useState} from 'react';
import { createRoot } from 'react-dom/client';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegisterPage from './pages/register';
import LoginPage from './pages/login';
import Menu from './pages/menu';
import ConstPage from './pages/const';
import Recomendations from './components/recomendations';
import SearchBar from './components/search/searchbar';
import HomePage from './pages/home';
import PlaylistPage from './pages/playlistpage';
import NotFoundPage from './pages/notfound';
import PlaylistForm from './pages/createplaylistpage';

export const Context = React.createContext();

const App = () => { 
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [currentTrack, setCurrentTrack] = useState(null)
  const [currTrackTitle, setCurrTrackTitle] = useState("")
  const [currTrackArtist, setCurrTrackArtist] = useState("")
  const [createdPlaylists, setCreatedPlaylists] = useState([])
  const [savedPlaylists, setSavedPlaylists] = useState([])

  const contextValue = {isLoggedIn, setIsLoggedIn, 
                        currentTrack, setCurrentTrack,
                        currTrackTitle, setCurrTrackTitle,
                        currTrackArtist, setCurrTrackArtist,
                        createdPlaylists, setCreatedPlaylists,
                        savedPlaylists, setSavedPlaylists}
  
  return (
    <Router> 
      <Context.Provider value={contextValue}>
        <Menu />
        <Routes> 
          <Route path="/register" element={<RegisterPage/>} />
          <Route path="/login" element={<LoginPage/>} />
          <Route path="/" element={<ConstPage/>} >
            <Route path="home/" element={<HomePage/>} />
            <Route path="rec/" element={<Recomendations/>} />
            <Route path="search/" element={<SearchBar/>} />
            <Route path="playlist/:id" element={<PlaylistPage/>}/>
            <Route path="createplaylist/" element={<PlaylistForm/>}/>
          </Route>
          <Route path='*' element={<NotFoundPage/>}/>
        </Routes>
      </Context.Provider>
    </Router>
  );
}

createRoot(document.getElementById('root')).render(<App />);