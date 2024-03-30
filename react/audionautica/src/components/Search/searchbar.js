import React, { useState, useEffect } from 'react';
import BackURL from '../../urls';
import token from '../../token';
import LoaderIcon from './download';
import "./searchbar.css"


const SearchBar = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

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

  const handleTrackClick = (trackId) => {
    console.log(trackId) 
  };

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
          <button
            className="result"
            key={result.id}
            onClick={() => handleTrackClick(result.id)}
          >
            <div>
              <p>{result.title}</p>
              <p>{result.artist}</p>
            </div>
          </button>
        ))}
      </div>
    )}
  </div>
);
};

export default SearchBar;