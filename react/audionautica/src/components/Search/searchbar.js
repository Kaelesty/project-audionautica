import React, { useState, useEffect, useContext } from 'react';
import BackURL from '../../urls';
import token from '../../token';
import LoaderIcon from './download';
import getTrackById from '../../gettrack';
import {Context} from '../../index.js'
import "./searchbar.css"


const SearchBar = () => {
  const [query, setQuery] = useState('');
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
 
  const {currentTrack, setCurrentTrack} = useContext(Context)
  const {setCurrTrackTitle} = useContext(Context)
  const {setCurrTrackArtist} = useContext(Context)
  

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

 
  /* const handleTrackClick = async (trackId, trackTitle, trackArtist) => {
    try {
      const trackBlob = await getTrackById(trackId);
      const trackUrl = URL.createObjectURL(trackBlob);
      const newTrack = new Audio(trackUrl);
  
      newTrack.addEventListener('loadedmetadata', () => {
        if (currentTrack){
          currentTrack.pause();
        }
        newTrack.play();
      });
  
      setCurrentTrack(newTrack);
      setCurrTrackTitle(trackTitle)
      setCurrTrackArtist(trackArtist)

    } catch (error) {
      console.error("Ошибка при получении трека", error);
    }
  }; */
  


  const handleTrackClick = async (id, trackTitle, trackArtist) => {
    try {
      const newController = new AbortController();
      const mediaSource = new MediaSource();
      const audio = document.createElement('audio');
      audio.src = URL.createObjectURL(mediaSource);

      await new Promise((resolve, reject) => {
        mediaSource.addEventListener('sourceopen', () => {
          resolve();
        });
        mediaSource.addEventListener('error', (e) => {
          reject(e);
        });
      });
  
      const sourceBuffer = mediaSource.addSourceBuffer('audio/mpeg');
  
      const { signal } = newController;
      const response = await fetch(BackURL + "/Music/GetTrack/", {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'token': token.getToken()
        },
        body: JSON.stringify({ id }),
        signal,
      });
  
      if (!response.ok) {
        throw new Error('Failed to fetch track');
      }
  
      const reader = response.body.getReader();
      const totalSize = +response.headers.get('Content-Length');
      const initialChunkSize = 300 * 300;
      let loaded = 0;

      let firstChankLoaded = false
      
      const appendData = async () => {
        
        const { done, value } = await reader.read();

        if (done) {
          console.log('Track loaded, duration', audio.duration );
          return;
        }
  
        await new Promise((resolve) => {
          sourceBuffer.addEventListener('updateend', () => {
            resolve();
          }, { once: true });
          sourceBuffer.appendBuffer(value);
        });
  
        loaded += value.length;

        if (loaded < initialChunkSize) {
          appendData();
        } else if (!firstChankLoaded) {
          if (currentTrack){          
            currentTrack.pause()
          }
          audio.play();
          console.log("first chunk appended, progress ", loaded / totalSize)
          setCurrentTrack(audio)
          setCurrTrackTitle(trackTitle)
          setCurrTrackArtist(trackArtist)
          appendData();
          firstChankLoaded = true
        }
        else{
          appendData();
          console.log("data appended, progress ", loaded / totalSize,)
        }
      };
  
      appendData();
    } catch (error) {
      if (error.name === 'AbortError') {
        console.log('Request aborted');
      } else {
        console.error('Error handling track click:', error);
      }
    }
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
            onClick={() => handleTrackClick(result.id, result.title, result.artist)}
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