import React, { useState } from 'react';
import './playlist.css';
import nopic from './icons/nopic.png'; 

const Playlist = ({ cover, title, creator }) => {
  const [isHovered, setIsHovered] = useState(false);

  const playlistClick = () => {
    console.log("playlist click")
  }
  const playClick = () => {
    console.log("play click")
  }


  if (cover === null){  
    cover = nopic
  }
  return (
    <div
      className={`playlist-container ${isHovered ? 'hovered' : ''}`}
      onMouseEnter={() => setIsHovered(true)}
      onMouseLeave={() => setIsHovered(false)}
      onClick={playlistClick}
    >
      <img className="playlist-cover" src={cover} alt="Playlist Cover" />

      <div className="playlist-info">
        <h3 className="playlist-title">{title}</h3>
        <p className="playlist-creator">{creator}</p>
      </div>

      {isHovered && (
        <button className="play-button" onClick={(event) => {
          event.stopPropagation(); 
          playClick();
        }}>
           <img className='playpic' alt=''
            src={require('./icons/play-button.png')} 
            />
        </button>
      )}
    </div>
  );
};

export default Playlist;