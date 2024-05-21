import React, { useState, useEffect} from 'react';
import BackURL from '../../urls.js';
import token from '../../token.js';
import LoaderIcon from './download.js';
import Track from '../track/track.js';
import useQueue from '../queue/queue.js';
import { Link } from 'react-router-dom';
import "./searchbar.css"
import Playlist from '../playlist.js';


const SearchBar = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const {setQueueTrack} = useQueue()
  const [searchMode, setSearchMode] = useState(false) 
  const [isSearchModeOpen, setIsSearchModeOpen] = useState(false)

  useEffect(() => {
    const fetchSearchResults = async () => {
      setLoading(true);
      if (searchMode){
        try {
          const response = await fetch(BackURL + "/PlayList/PlayListSearch/", {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'token': token.getToken()
            },
            body: JSON.stringify({ query })
          });
          const data = await response.json();
          setResults(data.playlists); 

        } catch (error) {
          console.error('Ошибка при выполнении запроса:', error);
        } finally {
          setLoading(false);
        }
      }
      else{
        try {
          const response = await fetch(BackURL + "/Music/Search/", {

            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'token': token.getToken()
            },
            body: JSON.stringify({ query })
          });
          const data = await response.json();
          setResults(data.tracks); 

        } catch (error) {
          console.error('Ошибка при выполнении запроса:', error);
        } finally {
          setLoading(false);
        }
      }
    };

    if (query.trim() === '') {
      setResults([]);
      return;
    }

    const searchTimer = setTimeout(async() => {
        await fetchSearchResults();
    }, 500); // Ждем пока пользователь закончит ввод

    return () => clearTimeout(searchTimer);
  }, [query, searchMode]);

  useEffect(() => {
    setResults([])
  },[searchMode]);

  const handleInputChange = (event) => {
    setQuery(event.target.value);
  }; 

  const onPlayClick = async (id, title, artist) => {
    setQueueTrack({id, title, artist, cover: null})
  }
  
  return (
    <div className="search-bar">
    <div className="input-container">
      <input
        type="text"
        value={query}
        onChange={handleInputChange}
        placeholder="Поиск..."
        className="search-input"
      />
      <div className="search-button">
      <button className="search-mode-button" 
        onClick={() => setSearchMode(!searchMode)}
        onMouseEnter={() => setIsSearchModeOpen(true)}
        onMouseLeave={() => setIsSearchModeOpen(false)}
      >
        {searchMode ? 'Плейлисты' : 'Треки'}
      </button>
      {isSearchModeOpen && (<div className="search-mode-dropdown" 
          onMouseEnter={() => setIsSearchModeOpen(true)}
          onMouseLeave={() => setIsSearchModeOpen(false)}
        >
        <button onClick={() => setSearchMode(true)}>Поиск по плейлистам</button>
        <button onClick={() => setSearchMode(false)}>Поиск по трекам</button>
      </div>)}
    </div>
    </div>
    {loading && <LoaderIcon/>}
    {!loading && results.length === 0 && !searchMode && <p className="no-results">Не нашли что искали? <Link to="/loadtrack">Загрузите свой трек на сервер</Link></p>}
    {!loading && results.length === 0 && searchMode && <p className="no-results">Не нашли что искали? <Link to="/createplaylist">Создайте свой плейлист</Link></p>}
    {!loading && results.length > 0 && (
      <>
        {!searchMode && results[0].tags && (<div className="results-container">
          {results.map((result, index) => (
            <Track
              key={index}
              id = {result.id}
              title = {result.title}
              artist = {result.artist}
              onPlayClick={onPlayClick}
            />
          ))}
        </div>
        )}      
        {searchMode && !results[0].tags && (<div className="results-playlist-container">
          {results.map((playlist, index) => (
            <Playlist key={index} 
              cover = {BackURL + "/PlayList/GetPoster/?id=" + playlist.id + "&token=" + token.getToken()}
              title = {playlist.title}
              id = {playlist.id}
            />
          ))}
        </div> 
        )}
      </>     
    )}
  </div>
);
};

export default SearchBar;