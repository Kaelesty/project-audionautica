import React, { useState, useEffect } from 'react';
import BackURL from '../urls';
import "./searchbar.css"

const SearchBar = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (query.trim() === '') {
      setResults([]);
      return;
    }

    const searchTimer = setTimeout(async() => {
        await fetchSearchResults();
    }, 500); // Ждем пока пользователь закончит ввод

    return () => clearTimeout(searchTimer);
  }, [query]);

  const fetchSearchResults = async () => {
    setLoading(true);
    try {
      const response = await fetch(BackURL + "/Music/Search/", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
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
    {loading && <p className="loading">Загрузка...</p>}
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