import React, { useState, useEffect} from 'react';
import BackURL from '../../urls.js';
import token from '../../token.js';
import LoaderIcon from './download.js';
import Track from '../track/track.js';
import useQueue from '../queue/queue.js';
import "./searchbar.css"


const SearchBar = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const {setQueueTrack} = useQueue()

  useEffect(() => {
    const fetchSearchResults = async () => {
      setLoading(true);
      console.log(token.getToken())
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
    };

    if (query.trim() === '') {
      setResults([]);
      return;
    }

    const searchTimer = setTimeout(async() => {
        await fetchSearchResults();
    }, 500); // Ждем пока пользователь закончит ввод

    return () => clearTimeout(searchTimer);
  }, [query]);

  const handleInputChange = (event) => {
    setQuery(event.target.value);
  }; 

  const onPlayClick = async (id, title, artist) => {
    setQueueTrack({id, title, artist})
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
    </div>
    {loading && <LoaderIcon/>}
    {!loading && results.length === 0 && <p className="no-results">Нет результатов</p>}
    {!loading && results.length > 0 && (
      <div className="results-container">
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
  </div>
);
};

export default SearchBar;